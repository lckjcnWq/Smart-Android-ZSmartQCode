package com.theswitchbot.common.ble

import android.app.Application
import com.theswitchbot.common.ble.dto.WoBleDevice
import com.theswitchbot.common.ble.impl.FastBleManager
import com.theswitchbot.common.ble.impl.NordicBleManager
import com.theswitchbot.common.util.SpAccessor
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import java.util.*
import kotlin.collections.LinkedHashSet

/**
 * 蓝牙管理器抽象，对第三方库进行再次封装，便于切换底层蓝牙实现
 *
 * 特别注意 回调器处理相关逻辑需要自行实现，可参见FastBleManager中对callBacks的处理
 */
abstract class IBleManager {
    protected val bufferSize=1
    //用于notify的数据通道
    val notifyDataChannel=Channel<ByteArray>(bufferSize,BufferOverflow.DROP_OLDEST)
    //是否启用Notify  使用完毕后务必记得正确关闭，否则可能导致数据出错
    protected var enableNotify:Boolean=false

    protected val callBacks = Collections.synchronizedCollection(LinkedHashSet<BleCallback>())

    companion object{
        /**
         * 获取蓝牙实现库,可切换底层
         */
        fun getInstance():IBleManager{
            return when(getImplLib()){
                BLE_LIB.FAST_BLE->FastBleManager.getInstance()
                BLE_LIB.NORDIC_BLE->NordicBleManager.getInstance()
                else->FastBleManager.getInstance()
            }
        }

        /**
         * 获取当前设置的底层蓝牙库   参看DebugKitActivity中对库切换时的设置
         */

        fun getImplLib():BLE_LIB?{
            val index=SpAccessor.getInt(SpAccessor.CURRENT_BLE_LIB,0)
            return when(index){
                0->BLE_LIB.FAST_BLE
                1->BLE_LIB.NORDIC_BLE
                else->null
            }
        }
    }

    /**
     * 启用或关闭notify通道  请谨慎使用
     */
    open fun notifyChannel(enable:Boolean){
        enableNotify=enable
        if (!enable){
            //禁用notify时清空通道
            while (!notifyDataChannel.isEmpty) {
                notifyDataChannel.tryReceive()
            }
        }
    }

    //初始化
    abstract fun init(app: Application)

    //设备是否支持BLE
    abstract fun isSupportBle(): Boolean

    //蓝牙是否已打开
    abstract fun isBleEnabled(): Boolean

    //添加回调处理
    fun addCallBack(callback: BleCallback) = callBacks.add(callback)

    //移除回调处理
    fun removeCallBack(callback: BleCallback) = callBacks.remove(callback)

    //清空回调处理
    fun clearCallBack() = callBacks.clear()

    //判断设备是否已连接
    abstract fun isConnected(mac: String): Boolean

    //断开与指定设备连接
    abstract suspend fun disconnect(mac: String): Boolean

    //连接到指定设备  可重试次数 默认1
    abstract suspend fun connect(mac: String, retryTimes: Int = 1): Boolean

    //写设备
    abstract suspend fun write(
        mac: String,
        data: ByteArray,
        autoDisConnect:Boolean=false
    ): Pair<Boolean,ByteArray>

    //读设备
    abstract suspend fun read(
        mac: String,
        service: String,
        characteristic: String
    ): ByteArray?

    //开始扫描
    abstract fun startScan(rule:ScanRule?=null,callBack: ScanCallBack)

    //停止扫描
    abstract fun stopScan(): Boolean
}

interface BleCallback {
    fun onDeviceConnected(device: WoBleDevice)
    fun onDeviceDisconnected(device: WoBleDevice)
}

interface ScanCallBack{
    fun onScanning(device: WoBleDevice)
    fun onScanFinished()
}

class ScanRule(
    var serviceUuids: Array<UUID>? = null,
    var deviceNames: Array<String> = arrayOf(),
    var deviceMac: String? = null,
    var autoConnect: Boolean = false,
    var fuzzy: Boolean = false,
    var scanTimeOut:Long = 30000
)
//蓝牙底层实现
enum class BLE_LIB{
    FAST_BLE,
    NORDIC_BLE
}