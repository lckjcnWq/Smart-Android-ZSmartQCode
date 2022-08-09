package com.theswitchbot.common.util

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.theswitchbot.common.R

/**
 * 设置状态栏和导航栏
 * @param statusBarColor 状态栏颜色，有颜色时isFitSystem=true，透明时isFitSystem=false
 * @param statusBarDarkFont 状态栏字体是否为黑色
 * @param isFitSystem 是否占用状态栏位置
 * @param navBarColor 导航栏颜色
 * @param navDarkFont 导航栏字体颜色是否为黑色
 * @param enableKeyboard 是否使用软键盘
 * @param keyboardMode 软键盘模式
 */
fun Activity.setupSystemBar(
    @ColorRes statusBarColor: Int = R.color.white,
    statusBarDarkFont: Boolean = true,
    isFitSystem: Boolean = true,
    @ColorRes navBarColor: Int = R.color.white,
    navDarkFont: Boolean = true,
    enableKeyboard: Boolean = false,
    isFullScreen: Boolean = false,
    keyboardMode: Int = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
) {
    val bar = ImmersionBar.with(this)
        .statusBarColor(statusBarColor)
        .statusBarDarkFont(statusBarDarkFont)
        .fitsSystemWindows(isFitSystem)
        .keyboardEnable(enableKeyboard, keyboardMode)

    if (isFullScreen) {
        bar.transparentNavigationBar()
    } else {
        bar.navigationBarColor(navBarColor).navigationBarDarkIcon(navDarkFont)
    }
    bar.init()
}

fun Fragment.setupSystemBar(
    @ColorRes statusBarColor: Int = R.color.white,
    statusBarDarkFont: Boolean = true,
    isFitSystem: Boolean = true,
    @ColorRes navBarColor: Int = R.color.white,
    navDarkFont: Boolean = true,
    enableKeyboard: Boolean = false,
    isFullScreen: Boolean = false,
    keyboardMode: Int = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
) {
    val bar = ImmersionBar.with(this)
        .statusBarColor(statusBarColor)
        .statusBarDarkFont(statusBarDarkFont)
        .fitsSystemWindows(isFitSystem)
        .keyboardEnable(enableKeyboard, keyboardMode)

    if (isFullScreen) {
        bar.transparentNavigationBar()
    } else {
        bar.navigationBarColor(navBarColor).navigationBarDarkIcon(navDarkFont)
    }
    bar.init()
}

/**
 * dialog设置系统栏样式
 */
fun Dialog.setupSystemBar(
    activity: Activity,
    @ColorRes statusBarColor: Int = R.color.white,
    statusBarDarkFont: Boolean = true,
    isFitSystem: Boolean = true,
    @ColorRes navBarColor: Int = R.color.white,
    navDarkFont: Boolean = true,
    enableKeyboard: Boolean = false,
    isFullScreen: Boolean = false,
    keyboardMode: Int = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
) {
    val bar = ImmersionBar.with(activity, this)
        .statusBarColor(statusBarColor)
        .statusBarDarkFont(statusBarDarkFont)
        .fitsSystemWindows(isFitSystem)
        .keyboardEnable(enableKeyboard, keyboardMode)

    if (isFullScreen) {
        bar.transparentNavigationBar()
    } else {
        bar.navigationBarColor(navBarColor).navigationBarDarkIcon(navDarkFont)
    }
    bar.init()
}

/**
 * 在window退出时重置dialog的系统栏样式
 */
fun Dialog.resetSystemBar(activity: Activity) {
//    ImmersionBar.destroy(activity, this)
}



/**
 * 获取底部导航栏高度
 */
fun Activity.getNavBarHeight(): Int {
    // 正确获取高度需要在页面布局完成后
    val windowInsetsCompat = ViewCompat.getRootWindowInsets(window.decorView)
    val insets = windowInsetsCompat?.getInsets(WindowInsetsCompat.Type.navigationBars())
    if (insets != null) {
        val height = insets.bottom - insets.top
        if (height > 0)
            return height
    }
    return ImmersionBar.getNavigationBarHeight(this)
}