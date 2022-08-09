package com.theswitchbot.common.util

import java.util.*

object ZoneUtils {
    /**
     * 获取当前时区
     * @return
     */
    fun getCurrentTimeZone() :String{
        val tz = TimeZone.getDefault();
        return tz.getDisplayName(false, TimeZone.SHORT)
    }
}