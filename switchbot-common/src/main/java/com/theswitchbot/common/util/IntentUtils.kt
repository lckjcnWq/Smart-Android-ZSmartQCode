package com.theswitchbot.common.util

import android.content.Intent
import android.os.Parcelable

fun Intent.putParcelable(key: String, value: Parcelable){
    putExtra(key, value)
}