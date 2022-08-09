package com.theswitchbot.common.app

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes

open class DefaultApp:IApp {

    override fun getPriority(): Int = 0

    override fun onAppCreate(app: Application) {
    }

    override fun attachBaseContext(base: Context) {

    }
}

fun Application.getString(@StringRes resId:Int):String=getString(resId)