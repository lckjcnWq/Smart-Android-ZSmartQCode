package com.theswitchbot.common

import android.app.Application
import android.content.Context
import com.theswitchbot.common.app.DefaultApp
import com.theswitchbot.common.net.NetworkStatusCallback

class CommonApp: DefaultApp() {
    companion object{
        val sysExcepHandler = Thread.getDefaultUncaughtExceptionHandler()
        lateinit var instance:Application
    }

    override fun onAppCreate(app: Application) {
        super.onAppCreate(app)
        instance=app
        NetworkStatusCallback.init(app)
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun getPriority(): Int = Int.MAX_VALUE
}