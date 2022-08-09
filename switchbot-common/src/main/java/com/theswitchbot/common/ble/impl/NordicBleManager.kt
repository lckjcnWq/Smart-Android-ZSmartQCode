package com.theswitchbot.common.ble.impl

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import com.theswitchbot.common.ble.IBleManager
import com.theswitchbot.common.ble.ScanCallBack
import com.theswitchbot.common.ble.ScanRule
import com.theswitchbot.common.ble.dto.WoBleDevice
import com.theswitchbot.common.ble.impl.nordic.RealNordicBleManager

class NordicBleManager private constructor() : IBleManager() {
    private lateinit var manager: RealNordicBleManager
    private lateinit var context:Application

    companion object {
        private val bleManager: NordicBleManager = NordicBleManager()

        internal fun getInstance(): IBleManager = bleManager
    }

    override fun notifyChannel(enable:Boolean){
        super.notifyChannel(enable)
        manager.notifyChannel(enable,object:RealNordicBleManager.NordicDataListener{
            override fun onData(data: ByteArray) {
                notifyDataChannel.trySend(data)
            }
        })
    }

    override fun init(app: Application) {
        context=app
        manager=RealNordicBleManager(app)
    }

    override fun isSupportBle(): Boolean =  context.applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

    override fun isBleEnabled(): Boolean =manager.isBleEnabled()

    override fun isConnected(mac: String): Boolean = manager.bluetoothDevice!=null&&mac==manager.bluetoothDevice!!.address&&manager.isConnected

    override suspend fun connect(mac: String, retryTimes: Int): Boolean = manager.connect(mac, retryTimes)

    override suspend fun disconnect(mac: String): Boolean =manager.disconnect(mac)

    override suspend fun write(
        mac: String,
        data: ByteArray,
        autoDisConnect:Boolean
    ): Pair<Boolean, ByteArray> = manager.write(mac,data)

    override suspend fun read(mac: String, service: String, characteristic: String): ByteArray? =manager.read(mac,service,characteristic)

    override fun startScan(rule: ScanRule?, callBack: ScanCallBack) =manager.startScan(rule,callBack)

    override fun stopScan(): Boolean = manager.stopScan()
}