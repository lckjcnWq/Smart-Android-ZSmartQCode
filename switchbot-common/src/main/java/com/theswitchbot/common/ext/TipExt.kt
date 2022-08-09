package com.theswitchbot.common.ext

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.theswitchbot.common.util.TipUtil

/**
 * 弹出toast
 */
fun LifecycleOwner.showToast(text: String) {
    if (this is Activity) {
        TipUtil.showToast(this, text)
    } else if (this is Fragment) {
        TipUtil.showToast(requireContext(), text)
    }
}

/**
 * 弹出toast
 */
fun LifecycleOwner.showToast(resId: Int, vararg formatArgs: Any) {
    showToast(resId.resIdToString(formatArgs))
}

/**
 * 弹出loading窗口
 */
fun LifecycleOwner.showLoadingDialog() {
    if (this is Activity) {
        TipUtil.showLoadingDialog(this)
    } else if (this is Fragment) {
        TipUtil.showLoadingDialog(requireContext())
    }
}

/**
 * 取消loading窗口
 */
fun LifecycleOwner.dismissLoadingDialog() {
    if (this is Activity) {
        TipUtil.dismissLoadingDialog()
    } else if (this is Fragment) {
        TipUtil.dismissLoadingDialog()
    }
}