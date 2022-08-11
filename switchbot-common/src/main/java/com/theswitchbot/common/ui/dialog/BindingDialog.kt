package com.theswitchbot.common.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.theswitchbot.common.R
import com.theswitchbot.common.util.DialogUtils
import com.theswitchbot.common.util.ViewBindingUtils
import com.theswitchbot.common.util.resetSystemBar
import com.theswitchbot.common.util.setupSystemBar
import com.theswitchbot.common.widget.loadingbutton.utils.addLifecycleObserver

/**
 * Author zrh
 * Date 2022/4/29 2:45 下午
 * Description
 * 1. 支持ViewBinding
 * 2. 支持设置进出场动画
 */
abstract class BindingDialog<VB : ViewBinding>(protected val activity: Activity, theme: Int) : Dialog(activity, theme),
    LifecycleObserver {

    protected lateinit var binding: VB
    private lateinit var mMaskView: View
    private lateinit var mContentLayout: FrameLayout

    // 点击外部或返回键是否可以关闭
    private var isCancelable = true

    init {
        onInit()
    }

    private fun onInit() {

        val layoutInflater = LayoutInflater.from(activity)
        val root = layoutInflater.inflate(R.layout.dialog_view_binding, null)
        mMaskView = root.findViewById(R.id.maskView)
        mContentLayout = root.findViewById(R.id.contentLayout)
        mContentLayout.fitsSystemWindows = fitsSystemWindows()

        binding = ViewBindingUtils.binding(this, layoutInflater, mContentLayout, true)
        val lp = binding.root.layoutParams as FrameLayout.LayoutParams
        lp.gravity = contentGravity()

        setContentView(root)

        mMaskView.setOnClickListener {
            if (isCancelable)
                dismiss()
        }

        binding.root.setOnClickListener { }

        setOnCancelListener { }
        setOnDismissListener { }
    }

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        super.setOnCancelListener {
            resetSystemBar(activity)
            listener?.onCancel(this)
        }
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener {
            resetSystemBar(activity)
            listener?.onDismiss(this)
        }
    }

    override fun setCancelable(flag: Boolean) {
        super.setCancelable(flag)
        isCancelable = flag
    }

    override fun show() {
        super.show()
        activity.addLifecycleObserver(this)

        val window = window
        val p = window!!.attributes
        p.width = WindowManager.LayoutParams.MATCH_PARENT
        p.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = p

        onSetupSystemBar()

        DialogUtils.startAnim(mMaskView, R.anim.dialog_alpha_in) { }
        DialogUtils.startAnim(mContentLayout, getInAnim()) { onEnterAnimEnd() }
    }

    /**
     * 入场动画执行完成回调
     */
    protected open fun onEnterAnimEnd() {

    }

    override fun dismiss() {
        DialogUtils.startAnim(mMaskView, R.anim.dialog_alpha_out) { }
        DialogUtils.startAnim(mContentLayout, getOutAnim()) {
            onOutAnimEnd()
            superDismiss()
        }
    }

    public fun dismissNoAnim(){
        superDismiss()
    }

    private fun superDismiss(){
        super.dismiss()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onActivityDestroy(){
        superDismiss()
    }

    /**
     * 退场动画执行完成回调
     */
    protected open fun onOutAnimEnd() {

    }

    override fun cancel() {
        dismiss()
    }

    /**
     * 配置系统状态栏和导航栏，子类可重写
     */
    protected open fun onSetupSystemBar() {
        setupSystemBar(
            activity,
            statusBarColor = R.color.transparent,
            statusBarDarkFont = true,
            isFitSystem = false,
            navBarColor = navBarColor(),
            navDarkFont = navDarkFont(),
            enableKeyboard = true,
            isFullScreen = isFullScreen(),
            keyboardMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }

    /**
     * 底部导航栏颜色
     */
    protected abstract fun navBarColor(): Int

    /**
     * 底部导航栏字体颜色
     */
    protected abstract fun navDarkFont(): Boolean

    /**
     * 是否去除导航栏占位
     */
    protected abstract fun isFullScreen(): Boolean

    /**
     * 状态栏是否占位
     */
    protected abstract fun fitsSystemWindows(): Boolean

    /**
     * 内容布局的对齐方式
     */
    protected abstract fun contentGravity(): Int

    /**
     * 进场动画
     */
    protected abstract fun getInAnim(): Int

    /**
     * 退场动画
     */
    protected abstract fun getOutAnim(): Int
}