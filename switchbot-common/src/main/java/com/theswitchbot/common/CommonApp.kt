package com.theswitchbot.common

import android.app.Application
import android.content.Context
import com.theswitchbot.common.app.DefaultApp

class CommonApp: DefaultApp() {
    companion object{
        lateinit var instance:Application
    }

    override fun onAppCreate(app: Application) {
        super.onAppCreate(app)
        instance=app
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun getPriority(): Int = Int.MAX_VALUE
}