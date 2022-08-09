package com.theswitchbot.common.util

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

object LocationStateMonitor {

    private val enableState = MutableLiveData(false)

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            enableState.value = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
    }

    fun init(app: Application) {
        val locationManager = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        enableState.value = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        app.registerReceiver(receiver, IntentFilter(LocationManager.MODE_CHANGED_ACTION))
    }

    fun isEnable(): Boolean = enableState.value ?: false

    fun observe(lifecycleOwner: LifecycleOwner, observer: (enable: Boolean) -> Unit) {
        enableState.observe(lifecycleOwner, observer)
    }

}