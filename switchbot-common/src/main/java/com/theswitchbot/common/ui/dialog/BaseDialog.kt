package com.theswitchbot.common.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.theswitchbot.common.R
import com.theswitchbot.common.util.ViewBindingUtils
import com.theswitchbot.common.util.resetSystemBar
import com.theswitchbot.common.util.setupSystemBar


abstract class BaseDialog<VB:ViewBinding>(val activity: Activity): Dialog(activity, R.style.BaseDialogStyle) {

    protected var mBinding: VB
    private var mMaskView: View
    private var mBottomLayout: FrameLayout

    init {
        val layoutInflater = LayoutInflater.from(activity)
        val root = layoutInflater.inflate(R.layout.dialog_base, null)
        mMaskView = root.findViewById(R.id.maskView)
        mBottomLayout = root.findViewById(R.id.bottomLayout)

        mBinding = ViewBindingUtils.binding(this, layoutInflater, mBottomLayout, true)
        val lp = mBinding.root.layoutParams as FrameLayout.LayoutParams
        lp.gravity = Gravity.BOTTOM

        setContentView(root)

        mMaskView.setOnClickListener {
            dismiss()
        }

        mBinding.root.setOnClickListener {  }
    }

    override fun show() {
        super.show()
        val window = window
        val p = window!!.attributes
        p.width = WindowManager.LayoutParams.MATCH_PARENT
        p.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = p

        startBottomInAnim(mMaskView, mBottomLayout) {}
    }

    override fun dismiss() {
        startBottomOutAnim(mMaskView, mBottomLayout) {
            super.dismiss()
        }
    }

    override fun cancel() {
        dismiss()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onSetupSystemBar()
    }

    protected open fun onSetupSystemBar(){
        setupSystemBar(
            activity,
            statusBarColor = R.color.transparent,
            statusBarDarkFont = true,
            isFitSystem = false,
            navBarColor = R.color.background,
            navDarkFont = true,
            enableKeyboard = true,
            keyboardMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        resetSystemBar(activity)
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