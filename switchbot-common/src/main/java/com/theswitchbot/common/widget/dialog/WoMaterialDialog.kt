package com.theswitchbot.common.widget.dialog

import android.content.Context
import androidx.annotation.UiThread
import com.afollestad.materialdialogs.MaterialDialog
import com.theswitchbot.common.R
import com.theswitchbot.common.logger.Logger

/**
 * 针对MaterialDialog的进一步封装，使其增加新功能
 */
class WoMaterialDialog(builder: Builder): MaterialDialog(builder) {
    override fun show() {
        setDialogCorner()
        super.show()
    }


    class Builder(context: Context): MaterialDialog.Builder(context) {

        @UiThread
        override fun build(): WoMaterialDialog {
            return WoMaterialDialog(this).setDialogCorner()
        }

        @UiThread
        override fun show(): WoMaterialDialog? {
            val dialog = build()
            dialog.setDialogCorner().show()
            return dialog
        }
    }


    /**
     * 给弹窗添加圆角
     */
    private fun setDialogCorner(): WoMaterialDialog {
        //todo 后面再确定要不要统一给弹窗加圆角，然后再反注释这段代码
        try {
            view.setBackgroundResource(R.drawable.shape_corner_white_r10)
            view.rootView.setBackgroundResource(R.color.transparent)
        } catch (ex: Exception) {
            Logger.e(ex, ex.message)
        }

        return this
    }
}


