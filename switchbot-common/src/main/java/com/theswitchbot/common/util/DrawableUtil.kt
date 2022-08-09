package com.theswitchbot.common.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Drawable?.changeSvgColor(context: Context, @ColorRes colorRes: Int) = this?.run {
    try {
        DrawableCompat.setTint(mutate(), ContextCompat.getColor(context, colorRes))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    this
}

fun @receiver:ColorInt Int.toDrawable(context: Context, @ColorRes colorRes: Int? = null): Drawable? = try {
    val drawable = ContextCompat.getDrawable(context, this)
    colorRes?.run {
        drawable.changeSvgColor(context, this)
    } ?: drawable
} catch (e: Exception) {
    null
}


