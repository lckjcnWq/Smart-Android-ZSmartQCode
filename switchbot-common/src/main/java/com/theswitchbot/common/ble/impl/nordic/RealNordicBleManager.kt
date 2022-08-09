package com.theswitchbot.common.ble.impl.nordic

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import com.theswitchbot.common.ble.BleUuid
import com.theswitchbot.common.ble.ScanCallBack
import com.theswitchbot.common.ble.ScanRule
import com.theswitchbot.common.ble.dto.WoBleDevice
import com.theswitchbot.common.ble.impl.awaitWithTimeout
import com.theswitchbot.common.logger.Logger
import com.theswitchbot.common.util.toHex
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.ConnectionPriorityRequest
import no.nordicsemi.android.ble.callback.SuccessCallback
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.coroutines.suspendCoroutine

class RealNordicBleManager(context: Context) : BleManager(context) {
    private val bufferSize=20
    private var mUseLongWrite = false
    private val bleAdapter = BluetoothAdapter.getDefaultAdapter()
    private var currentMac: String? = null
    private var scanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner()
    private var scanCallBack: NordicScanCallback?=null
    private var scanRule: ScanRule? = null
    private var mRXCharacteristic: BluetoothGattCharacteristic? = null
    private var mTXCharacteristic: BluetoothGattCharacteristic? = null
    private var bleGatt:BluetoothGatt?=null
    private val scanDeviceList=ArrayList<WoBleDevice>()

    private val disconnectChannel=Channel<Boolean>(bufferSize,onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val connectChannel= Channel<Boolean>(bufferSize,onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val dataChannel= Channel<ByteArray>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private var enableNotify=false
    private var dataListener:NordicDataListener?=null

    private var serviceUUID:String=BleUuid.WOFINGER_SERVICE_UUID


    override fun getGattCallback(): BleManagerGattCallback {
        return object : BleManagerGattCallback() {

            override fun initialize() {
                super.initialize()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    requestConnectionPriority(ConnectionPriorityRequest.CONNECTION_PRIORITY_HIGH)
                        .enqueue()
                }
                setNotificationCallback(mTXCharacteristic).with { device, data ->
                    if (data.value!=null){
                        dataChannel.trySend(data.value!!)
                        if (enableNotify){
                            dataListener?.onData(data.value!!)
                        }
                    }
                }
                //            requestMtu(260).enqueue();
                enableNotifications(mTXCharacteristic)
                    .enqueue()
            }

            override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
                var service = gatt.getService(UUID.fromString(BleUuid.WOFINGER_SERVICE_UUID))
                if(service==null){
                    service = gatt.getService(UUID.fromString(BleUuid.NEW_WOFINGER_SERVICE_UUID))
                    serviceUUID=BleUuid.NEW_WOFINGER_SERVICE_UUID
                }
                if (service != null) {
                    mRXCharacteristic =
                        service.getCharacteristic(UUID.fromString(BleUuid.WOFINGER_RX_CHAR_UUID))
                    mTXCharacteristic =
                        service.getCharacteristic(UUID.fromString(BleUuid.WOFINGER_TX_CHAR_UUID))
                }

                var writeRequest = false
                var writeCommand = false
                if (mRXCharacteristic != null) {
                    val rxProperties: Int = mRXCharacteristic!!.properties
                    writeRequest = rxProperties and BluetoothGattCharacteristic.PROPERTY_WRITE > 0
                    writeCommand =
                        rxProperties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE > 0
                    if (writeRequest) {
                        mRXCharacteristic!!.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                    } else {
                        mUseLongWrite = false
                    }
                }
                bleGatt=gatt
                return mRXCharacteristic != null && mTXCharacteristic != null && (writeRequest || writeCommand)
            }

            override fun onServicesInvalidated() {

            }

            override fun onDeviceReady() {
                super.onDeviceReady()
                if (bluetoothDevice!=null&&bluetoothDevice.address == currentMac) {
                    connectChannel.trySend(true)
                }
            }
        }
    }

    fun notifyChannel(enable:Boolean,dataListener:NordicDataListener?){
        if (enable) {
            this.dataListener = dataListener
        }else{
            this.dataListener =null
        }
        enableNotify=enable
    }

    override fun log(priority: Int, message: String) {
        Logger.log(priority, null, message, null)

    }


    suspend fun connect(mac: String, retryTimes: Int): Boolean {
        currentMac = mac
        connect(bleAdapter.getRemoteDevice(mac)).retry(retryTimes).enqueue()
        //5s连接超时
        var success=false
        withTimeoutOrNull(8 * 1000) {
            success=connectChannel.receive()
        }
        return success
    }

    suspend fun disconnect(mac: String): Boolean {
        currentMac = mac
        disconnect().fail{ _,_ -> disconnectChannel.trySend(false) }.done { disconnectChannel.trySend(true) }.enqueue()
        //5s断开连接超时
        var result=false
        withTimeoutOrNull(8 * 1000) {
            result=disconnectChannel.receive()
        }
        return result
    }

    fun isBleEnabled(): Boolean {
        return bleAdapter?.isEnabled ?: false
    }

    /**
     * 注意目前暂无法指定 notifyCharacteristic
     */
    suspend fun write(
        mac: String,
        data: ByteArray
    ): Pair<Boolean, ByteArray> {
        if (bleGatt==null){
            return Pair(false, byteArrayOf())
        }
        val serviceCharacteristic=bleGatt!!.getService(UUID.fromString(serviceUUID))
        val writeCharacteristic=serviceCharacteristic.getCharacteristic(UUID.fromString(BleUuid.WOFINGER_RX_CHAR_UUID))

        val notifyCharacteristic=serviceCharacteristic.getCharacteristic(UUID.fromString(BleUuid.WOFINGER_TX_CHAR_UUID))

        val syncJob =GlobalScope.async {
            //避免通道中有遗留数据 先将其消费掉
            if (!dataChannel.isEmpty){
                dataChannel.receive()
            }
            writeCharacteristic(writeCharacteristic,data).with { device, data -> }.enqueue()
            Pair(true,dataChannel.receive())
        }
        val result= syncJob.awaitWithTimeout(10000) ?: Pair(false, byteArrayOf())
//        disconnect(mac)
        return result
    }


    suspend fun read(mac: String, service: String, characteristic: String): ByteArray? {
        TODO("Not yet implemented")
    }

    fun startScan(rule: ScanRule?, callBack: ScanCallBack) {
        stopScan()
        // Scanning settings
        val settings = ScanSettings.Builder()
            .setLegacy(false)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(5)
            .setUseHardwareBatchingIfSupported(false)
            .setUseHardwareFilteringIfSupported(false)
            .build()
        scanRule = rule
        scanCallBack= NordicScanCallback(callBack)
        Logger.d("蓝牙扫描开始")
        scanDeviceList.clear()
        scanner.startScan(null, settings, scanCallBack!!)
        GlobalScope.async {
            delay(rule?.scanTimeOut?:10000)
            stopScan()
            callBack.onScanFinished()
            Logger.d("蓝牙扫描结束")
        }
    }

    fun stopScan(): Boolean {
        return if (scanCallBack!=null) {
            scanner.stopScan(scanCallBack!!)
            true
        }else{
            false
        }
    }

    inner class NordicScanCallback(val callBack: ScanCallBack) : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (filterDevice(result)){
                val device=convertToWoBle(result)
                scanDeviceList.add(device)
//                Logger.d("扫描到设备：${result.device.address}")
                callBack.onScanning(device)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            Logger.d("onBatchScanResults：${results.size}")
            results.filter { filterDevice(it) }.map { convertToWoBle(it) }.forEach {
                scanDeviceList.add(it)
                callBack.onScanning(it)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Logger.d("扫描失败，errorCode：${errorCode}")
        }
    }

    fun filterDevice(result: ScanResult):Boolean{
        if(scanRule==null) {
            return true
        }else{
            if (scanRule!!.serviceUuids!=null) {
                if (result.scanRecord?.serviceUuids?.containsAll(scanRule!!.serviceUuids!!.map { ParcelUuid(it) }) != true){
                    return false
                }
            }
            if (scanRule!!.deviceNames.isNotEmpty()){
                if (!scanRule!!.deviceNames.contains(result.device.name)){
                    return false
                }
            }
            if (scanRule!!.deviceMac!=null){
                if (scanRule!!.deviceMac!=result.device.address){
                    return false
                }
            }
        }
        return true
    }


    fun convertToWoBle(device: ScanResult): WoBleDevice {
        return device.run {
            WoBleDevice(
                device.device.address,
                scanData = device.scanRecord?.bytes,
                rssi=device.rssi,
                timestampNanos = device.timestampNanos,
                deviceName = device.device.name
            )
        }
    }

    fun convertToWoBle(devices: List<ScanResult>?): List<WoBleDevice> {
        if (devices == null) {
            return emptyList()
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            devices.stream().map { convertToWoBle(it) }
                .collect(Collectors.toList())
        } else {
            val newDevices =
                ArrayList<WoBleDevice>(com.clj.fastble.BleManager.getInstance().allConnectedDevice.size)
            for (bleDevice in devices) {
                newDevices.add(convertToWoBle(bleDevice))
            }
            newDevices
        }
    }

    public interface NordicDataListener{
        fun onData(data:ByteArray)
    }

}