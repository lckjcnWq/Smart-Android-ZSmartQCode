package com.theswitchbot.common.util

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import com.theswitchbot.common.R

object DialogUtils {

    fun startAnim(view: View, @AnimRes anim: Int, onEnd: () -> Unit) {
        val animation = AnimationUtils.loadAnimation(view.context, anim)
        animation.fillAfter = true
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                view.post { onEnd() }
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        view.startAnimation(animation)
    }

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
}