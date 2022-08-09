package com.theswitchbot.common.ext

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.theswitchbot.common.CommonApp

/**
 * 资源文件相关的扩展函数
 */

fun getResources(): Resources {
    return CommonApp.instance.resources
}

/**
 * colorResId 转color
 */
fun Int.resIdToColor() = ContextCompat.getColor(CommonApp.instance, this)

/**
 * drawableResId 转drawable
 */
fun Int.resIdToDrawable() = ContextCompat.getDrawable(CommonApp.instance, this)

/**
 * dimensResId 转 dimens
 */
fun Int.resIdToDimens() = getResources().getDimension(this)

/**
 * 获取字符串数组
 */
fun Int.resIdToStringArray(): Array<String> = getResources().getStringArray(this)


/**
 * 获取string.xml下的字符串资源
 *
 * @param formatArgs
 * @return
 */
fun Int.resIdToString(vararg formatArgs: Any): String {
    return getResources().getString(this, *formatArgs)
}

/**
 * 获取string.xml下的复数字符串资源
 *
 * @param formatArgs
 * @return
 */
fun Int.resIdToQuantityString(count:Int, vararg formatArgs: Any): String {
    return getResources().getQuantityString(this, count, *formatArgs)
}

fun Drawable.changeIconColor(color: Int, action: ((drawable: Drawable) -> Unit)? = null) {
    val tintDrawable = DrawableCompat.wrap(this).mutate()
    DrawableCompat.setTint(tintDrawable, color)
    action?.invoke(tintDrawable)
}