package com.theswitchbot.common.widget.customdialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.theswitchbot.common.R

/**
 * Created by hfei on 2015/10/20.
 */
open class BaseDialog(protected var context: Context?) {
    protected var dialog: Dialog? = context?.let { Dialog(it, R.style.customDialog) }
    protected var listener: View.OnClickListener? = null
    private var widthRatio=0.90f
     var canCancel=true

    fun addView(vv: View?) {
        val dm = DisplayMetrics()
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(dm)
        val params = LinearLayout.LayoutParams(
            (dm.widthPixels * widthRatio).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.addContentView(vv!!, params)
        dialog?.setCanceledOnTouchOutside(canCancel)
    }

    val isShowing: Boolean
        get() = dialog!!.isShowing

    fun showDialog() {
        if (dialog != null) {
            if (context is Activity) {
                if ((context as Activity).isFinishing) return
            }
            dialog!!.show()
        }
    }

    fun dismiss() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

     fun setCanCancelable(canCancel: Boolean) {
        this.canCancel = canCancel
    }

    fun setWidthRatio(widthRatio: Float) {
        this.widthRatio = widthRatio
    }
}