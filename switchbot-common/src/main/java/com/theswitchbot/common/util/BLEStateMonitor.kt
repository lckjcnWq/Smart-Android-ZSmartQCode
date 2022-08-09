package com.theswitchbot.common.util

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

/**
 * 蓝牙状态监听器
 */
object BLEStateMonitor {

    private val enableState = MutableLiveData(false)

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)

            when (state) {
                BluetoothAdapter.STATE_OFF -> {
                    enableState.value = false
                }

                BluetoothAdapter.STATE_ON -> {
                    enableState.value = true
                }
            }

        }
    }

    fun init(app: Application) {
        val bleAdapter = BluetoothAdapter.getDefaultAdapter()
        enableState.value = bleAdapter?.isEnabled ?: false
        app.registerReceiver(receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    fun isEnable(): Boolean = enableState.value ?: false

    fun observe(lifecycleOwner: LifecycleOwner, observer: (enable: Boolean) -> Unit) {
        enableState.observe(lifecycleOwner, observer)
    }
}