package com.kandao.smartqrcode

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.*
import com.theswitchbot.common.app.IApp
import com.theswitchbot.common.logger.*
import com.theswitchbot.common.manager.ActivityManager
import com.theswitchbot.common.util.*

class App : Application() {
    private val subApps = ArrayList<IApp>()
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        ARouterUtil.init(BuildConfig.DEBUG, this@App)
        SpAccessor.init(this)
        parseManifest()
        ActivityManager.init(this)
    }

    private fun parseManifest() {
        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        appInfo.metaData.keySet().forEach {
            val metaValue = appInfo.metaData[it]
            if (metaValue is String && metaValue == IApp.APP_META_KEY) {
                val iapp: Class<IApp> = Class.forName(it) as Class<IApp>
                subApps.add(iapp.newInstance())
            }
        }
        subApps.sortWith { iApp: IApp, iApp1: IApp -> iApp1.getPriority() - iApp.getPriority() }
        subApps.forEach { it.onAppCreate(this) }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        subApps.forEach { it.attachBaseContext(base) }
    }
}