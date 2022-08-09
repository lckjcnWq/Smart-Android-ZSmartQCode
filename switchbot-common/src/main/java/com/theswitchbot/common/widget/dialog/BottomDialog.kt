package com.theswitchbot.common.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.theswitchbot.common.util.ViewBindingUtils
import com.theswitchbot.common.util.resetSystemBar
import com.theswitchbot.common.util.setupSystemBar
import com.theswitchbot.common.R
import com.theswitchbot.common.util.DialogUtils

/**
 * Author zrh
 * Date 2022/2/17 6:49 下午
 * Description 底部弹窗
 */
abstract class BottomDialog<VB:ViewBinding>(val activity: Activity): Dialog(activity, R.style.BottomDialogStyle) {

    protected var binding: VB
    private var mMaskView: View
    private var mBottomLayout: FrameLayout
    // 点击外部或返回键是否可以关闭
    private var isCancelable = true

    init {
        val layoutInflater = LayoutInflater.from(activity)
        val root = layoutInflater.inflate(R.layout.dialog_bottom, null)
        mMaskView = root.findViewById(R.id.maskView)
        mBottomLayout = root.findViewById(R.id.bottomLayout)
        mBottomLayout.fitsSystemWindows = fitsSystemWindows()

        binding = ViewBindingUtils.binding(this, layoutInflater, mBottomLayout, true)
        val lp = binding.root.layoutParams as FrameLayout.LayoutParams
        lp.gravity = Gravity.BOTTOM

        setContentView(root)

        mMaskView.setOnClickListener {
            if (isCancelable)
                dismiss()
        }

        binding.root.setOnClickListener {  }
    }

    override fun setCancelable(flag: Boolean) {
        super.setCancelable(flag)
        isCancelable = flag
    }

    override fun show() {
        super.show()
        val window = window
        val p = window!!.attributes
        p.width = WindowManager.LayoutParams.MATCH_PARENT
        p.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = p

        DialogUtils.startBottomInAnim(mMaskView, mBottomLayout) {}
    }

    override fun dismiss() {
        DialogUtils.startBottomOutAnim(mMaskView, mBottomLayout) {
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

    /**
     * 配置系统状态栏和导航栏，子类可重写
     */
    protected open fun onSetupSystemBar(){
        setupSystemBar(
            activity,
            statusBarColor = R.color.transparent,
            statusBarDarkFont = true,
            isFitSystem = false,
            navBarColor = navBarColor(),
            navDarkFont = true,
            enableKeyboard = true,
            keyboardMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }

    /**
     * 底部导航栏颜色
     */
    protected open fun navBarColor():Int{
        return R.color.white
    }

    protected open fun fitsSystemWindows():Boolean{
        return true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        resetSystemBar(activity)
    }

}