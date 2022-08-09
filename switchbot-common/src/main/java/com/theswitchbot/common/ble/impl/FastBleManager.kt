package com.theswitchbot.common.ble.impl

import android.app.Application
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattService
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import com.blankj.utilcode.util.DeviceUtils
import com.clj.fastble.BleManager
import com.clj.fastble.callback.*
import com.clj.fastble.data.BleDevice
import com.clj.fastble.data.BleScanState
import com.clj.fastble.exception.BleException
import com.clj.fastble.scan.BleScanRuleConfig
import com.theswitchbot.common.ble.BleUuid
import com.theswitchbot.common.ble.IBleManager
import com.theswitchbot.common.ble.ScanCallBack
import com.theswitchbot.common.ble.ScanRule
import com.theswitchbot.common.ble.dto.WoBleDevice
import com.theswitchbot.common.logger.Logger
import com.theswitchbot.common.util.toHex
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import java.lang.Exception
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


//基于fastble实现的蓝牙设备管理类
class FastBleManager private constructor() : IBleManager(){
    val TAG=FastBleManager::class.simpleName
    //写数据等待回应中  等待写入回复数据时不接收连续notify数据  避免数据跑串
    @Volatile
    private  var waitingReply=false
    companion object {
        private val bleManager: FastBleManager = FastBleManager()

        fun convertToWoBle(device: BleDevice): WoBleDevice {
            return device.run {
                WoBleDevice(
                    device.device.address,
                    scanData = device.scanRecord,
                    rssi=device.rssi,
                    timestampNanos = device.timestampNanos,
                    deviceName = device.name
                )
            }
        }

        fun convertToWoBle(devices: List<BleDevice>?): List<WoBleDevice> {
            if (devices == null) {
                return emptyList()
            }
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                devices.stream().map { convertToWoBle(it) }
                    .collect(Collectors.toList())
            } else {
                val newDevices =
                    ArrayList<WoBleDevice>(BleManager.getInstance().allConnectedDevice.size)
                for (bleDevice in devices) {
                    newDevices.add(convertToWoBle(bleDevice))
                }
                newDevices
            }
        }


        internal fun getInstance(): IBleManager = bleManager
    }

    private var connectedDevice:BleDevice?=null
    private val connectChannel= Channel<Boolean>(bufferSize,onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val notifyChannel=Channel<Boolean>(bufferSize,onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val dataChannel=Channel<ByteArray>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private var serviceUUID:String?=BleUuid.WOFINGER_SERVICE_UUID

    private val connectListener=object : BleGattCallback() {
        override fun onStartConnect() {}
        override fun onConnectFail(bleDevice: BleDevice, exception: BleException?) {
            BleManager.getInstance().disconnectAllDevice()
            connectChannel.trySend(false)
            Logger.d(TAG,"蓝牙连接失败-${bleDevice.mac}-----code=${exception?.code}  description=${exception?.description}")

        }

        override fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt?, status: Int) {
            invokeCallBack(convertToWoBle(bleDevice), CallBackType.CONNECTED)
            connectedDevice=bleDevice
            Logger.d(TAG,"蓝牙连接成功-${bleDevice.mac}")
            connectChannel.trySend(true)
        }

        override fun onDisConnected(isActiveDisConnected: Boolean, device: BleDevice, gatt: BluetoothGatt?, status: Int) {
            invokeCallBack(convertToWoBle(device), CallBackType.DISCONNECTED)
            Logger.d(TAG,"蓝牙断开连接-${device.mac}")
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (gatt?.getService(UUID.fromString(BleUuid.NEW_WOFINGER_SERVICE_UUID))!=null){
                    serviceUUID=BleUuid.NEW_WOFINGER_SERVICE_UUID;
                }
            }
        }
    }

    private val notifyListener=object : BleNotifyCallback() {
        override fun onNotifySuccess() {
            notifyChannel.trySend(true)
        }

        override fun onNotifyFailure(exception: BleException) {
            Logger.e(TAG,"onNotifyFailure:${exception.description}")
            notifyChannel.trySend(false)
        }

        override fun onCharacteristicChanged(data: ByteArray) {
            Logger.d(TAG,"接收蓝牙数据：${data.toHex()}      waitingReply=${waitingReply}")
            val firstByte=data[0].toInt()
            if (enableNotify&&!waitingReply&&firstByte==0x0f){
                notifyDataChannel.trySend(data)
            }else if(firstByte!=0x0f) {
                dataChannel.trySend(data)
            }
        }
    }

    override fun init(app: Application) {
        BleManager.getInstance().init(app)
        BleManager.getInstance().enableLog(false)
    }

    override fun isSupportBle(): Boolean = BleManager.getInstance().isSupportBle
    override fun isBleEnabled(): Boolean = BleManager.getInstance().isBlueEnable



    override suspend fun connect(mac: String, retryTimes: Int): Boolean{
        var currentTimes=0
        if (BleManager.getInstance().isConnected(mac)){
            return true
        }
        BleManager.getInstance().disconnectAllDevice()
        delay(10)
        while (currentTimes<retryTimes){
            connectedDevice=null
            currentTimes++
            BleManager.getInstance().connect(mac, connectListener)
            if(connectChannel.receive()){
                BleManager.getInstance().notify(connectedDevice,serviceUUID,BleUuid.WOFINGER_TX_CHAR_UUID,notifyListener)
                if(notifyChannel.receive()&&connectedDevice?.mac==mac){
                    return true
                }
            }
            Logger.e("蓝牙连接失败，重试中，当前重试次数$currentTimes")
        }
        return false
    }

    override suspend fun disconnect(mac: String): Boolean {
        val device = findConnectedDevice(mac)
        return if (device != null) {
            BleManager.getInstance().disconnect(device)
            connectedDevice=null
            invokeCallBack(convertToWoBle(device), CallBackType.DISCONNECTED)
            true
        } else {
            false
        }
    }

    //根据mac查找已经连接的fastBle设备
    private fun findConnectedDevice(mac: String): BleDevice? {
        return if (BleManager.getInstance().allConnectedDevice != null) {
            var device: BleDevice? = null
            for (bleDevice in BleManager.getInstance().allConnectedDevice) {
                if (bleDevice.mac.equals(mac)) {
                    device = bleDevice
                }
            }
            device
        } else {
            null
        }
    }


    override fun isConnected(mac: String): Boolean = BleManager.getInstance().isConnected(mac)

    @Synchronized
    override suspend fun write(
        mac: String,
        data: ByteArray,
        autoDisConnect:Boolean
    ): Pair<Boolean, ByteArray> {
        Logger.d("写蓝牙数据,mac:$mac    data:${data.toHex()}")
        val bleDevice = findConnectedDevice(mac)
        return if (bleDevice == null) {
            waitingReply=false
            Pair(false, byteArrayOf())
        } else {
            //写成功后等待notify最大超时时间10s
            var result=withTimeoutOrNull(10000){
                //避免通道中有遗留数据 先将其消费掉
                if (!dataChannel.isEmpty){
                    dataChannel.receive()
                }
                async { writeNoResp(mac, data) }
                val replyData = dataChannel.receive()
                waitingReply=false
                Pair(true, replyData)
            }
            if (result==null){
                Logger.e(TAG,"等待Notify超时")
                disconnect(mac)
                result=Pair(false, byteArrayOf())
                waitingReply=false
            }
            if (autoDisConnect) {
                disconnect(mac)
            }
            result
        }
    }


    @Synchronized
    suspend fun writeNoResp(
        mac: String,
        data: ByteArray
    ): Boolean {
        val bleDevice = findConnectedDevice(mac)
        return if (bleDevice == null) {
            false
        } else {
            suspendCoroutine {
                waitingReply=true
                BleManager.getInstance().write(bleDevice, serviceUUID, BleUuid.WOFINGER_RX_CHAR_UUID, data,
                    object : BleWriteCallback() {
                        override fun onWriteSuccess(
                            current: Int,
                            total: Int,
                            justWrite: ByteArray
                        ) {
                            try {
                                if (total == current) {
                                    it.resume(true)
                                }
                            }catch (e:Exception){
                                Logger.e("写蓝牙onWriteSuccess:${e}")
                            }
                        }
                        override fun onWriteFailure(exception: BleException) {
                            waitingReply=false
                            Logger.e("写蓝牙失败:${exception.description}")
                            it.resume(false)
                        }
                    })
            }
        }
    }

    override suspend fun read(
        mac: String,
        service: String,
        characteristic: String
    ): ByteArray? {
        val bleDevice = findConnectedDevice(mac)
        return if (bleDevice == null) {
            null
        } else {
            suspendCoroutine {
                BleManager.getInstance().read(
                    bleDevice,
//                    service,
//                    characteristic,
                    serviceUUID, BleUuid.WOFINGER_TX_CHAR_UUID,
                    object : BleReadCallback() {
                        override fun onReadSuccess(data: ByteArray) {
                            it.resume(data)
                        }

                        override fun onReadFailure(exception: BleException) {
                            it.resume(null)
                        }
                    })
            }
        }
    }

    suspend fun readNoResp(
        mac: String,
    ): ByteArray? {
        val bleDevice = findConnectedDevice(mac)
        return if (bleDevice == null) {
            null
        } else {
            suspendCoroutine {
                BleManager.getInstance().read(
                    bleDevice,
                    serviceUUID, BleUuid.WOFINGER_TX_CHAR_UUID,
                    object : BleReadCallback() {
                        override fun onReadSuccess(data: ByteArray) {
                            it.resume(data)
                        }

                        override fun onReadFailure(exception: BleException) {
                            it.resume(null)
                        }
                    })
            }
        }
    }

    @Synchronized
    override fun startScan(rule: ScanRule?, callBack: ScanCallBack) {
        stopScan()
        val scanRuleConfig = if (rule != null) {
            BleScanRuleConfig.Builder()
                .setServiceUuids(rule.serviceUuids)
                .setDeviceName(rule.fuzzy, *rule.deviceNames)
                .setDeviceMac(rule.deviceMac)
                .setAutoConnect(rule.autoConnect)
                .setScanTimeOut(rule.scanTimeOut)
                .build()

        } else {
            BleScanRuleConfig.Builder().build()
        }
        BleManager.getInstance().disconnectAllDevice()

        BleManager.getInstance().initScanRule(scanRuleConfig)
        BleManager.getInstance().scan(object : BleScanCallback() {
            override fun onScanStarted(success: Boolean) {
                Logger.d("蓝牙扫描开始，结果:${success}")

            }

            override fun onScanning(bleDevice: BleDevice) {
                callBack.onScanning(convertToWoBle(bleDevice))
            }

            override fun onScanFinished(scanResultList: List<BleDevice>?) {
                callBack.onScanFinished()
            }
        })

    }

    override fun stopScan(): Boolean {
        if (BleManager.getInstance().scanSate == BleScanState.STATE_SCANNING) {
            BleManager.getInstance().cancelScan()
        }
        return true
    }

    private fun invokeCallBack(device: WoBleDevice, type: CallBackType) {
        callBacks.iterator().forEach {
            when (type) {
                CallBackType.CONNECTED -> it.onDeviceConnected(device)
                CallBackType.DISCONNECTED -> it.onDeviceDisconnected(device)
            }
        }
    }

}

suspend fun <T> Deferred<T>.awaitWithTimeout(time: Long): T? = withTimeoutOrNull(time) {
    val t=await()
    t
}

enum class CallBackType {
    CONNECTED, DISCONNECTED
}