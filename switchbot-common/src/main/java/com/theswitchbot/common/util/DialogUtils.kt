package com.theswitchbot.common.util

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.browser.customtabs.CustomTabsIntent
import com.theswitchbot.common.R
import com.theswitchbot.common.widget.web.CustomTabActivityHelper
import com.theswitchbot.common.widget.web.WebviewFallback

object DialogUtils {

    fun startBottomInAnim(maskView: View, contentView: View, onEnd: () -> Unit) {
        val bottomAnim = AnimationUtils.loadAnimation(maskView.context, R.anim.push_bottom_in)
        bottomAnim.fillAfter = true
        contentView.startAnimation(bottomAnim)

        val alphaAnim = AnimationUtils.loadAnimation(maskView.context, R.anim.dialog_alpha_in)
        alphaAnim.fillAfter = true
        alphaAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                onEnd()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        maskView.startAnimation(alphaAnim)
    }


    fun startBottomOutAnim(maskView: View, contentView: View, onEnd: () -> Unit) {
        val bottomAnim = AnimationUtils.loadAnimation(maskView.context, R.anim.push_bottom_out)
        bottomAnim.fillAfter = true
        contentView.startAnimation(bottomAnim)

        val alphaAnim = AnimationUtils.loadAnimation(maskView.context, R.anim.dialog_alpha_out)
        alphaAnim.fillAfter = true
        alphaAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                onEnd()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        maskView.startAnimation(alphaAnim)
    }
    //Wi-Fi类产品配网失败dialog弹框
    fun showConfigWifiErrorDialog(context:Activity){
        TipUtil.showConfirmDialog(context,title = context.getString(R.string.nfc_mini_not_device_title),
            message = context.getString(R.string.config_wifi_faq_tip),okStr = R.string.view_faqs,okListener = {
                val uri = Uri.Builder()
                    .scheme("https")
                    .appendEncodedPath(context.getString(R.string.wifi_config_error_faq))
                    .build()
                val customTabsIntent = CustomTabsIntent.Builder().build()
                CustomTabActivityHelper.openCustomTab(context, customTabsIntent, uri, WebviewFallback())
            })
    }
}