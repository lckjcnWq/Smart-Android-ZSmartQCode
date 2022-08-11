package com.theswitchbot.common.widget.dialog

import android.app.Activity
import android.view.Gravity
import androidx.viewbinding.ViewBinding
import com.theswitchbot.common.R
import com.theswitchbot.common.ui.dialog.BindingDialog

/**
 * Author zrh
 * Date 2022/4/29 3:58 下午
 * Description 中间弹窗
 */
abstract class CenterDialog<VB : ViewBinding>(activity: Activity) : BindingDialog<VB>(activity, R.style.CenterDialogStyle) {
    override fun navBarColor(): Int = R.color.transparent

    override fun navDarkFont(): Boolean = false

    override fun fitsSystemWindows(): Boolean = false

    override fun contentGravity(): Int = Gravity.CENTER

    override fun getInAnim(): Int = R.anim.dialog_alpha_in

    override fun getOutAnim(): Int = R.anim.dialog_alpha_out

    final override fun isFullScreen(): Boolean = true
}