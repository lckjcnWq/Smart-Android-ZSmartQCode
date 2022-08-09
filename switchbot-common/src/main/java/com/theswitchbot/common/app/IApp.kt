package com.theswitchbot.common.app

import android.app.Application
import android.content.Context

interface IApp {
    companion object{
        const val APP_META_KEY="appInjectKey"
    }

    fun getPriority():Int

    fun onAppCreate(app:Application)

    fun attachBaseContext(base: Context)
}