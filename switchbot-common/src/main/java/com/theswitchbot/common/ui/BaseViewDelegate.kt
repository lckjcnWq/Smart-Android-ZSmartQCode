package com.theswitchbot.common.ui

import android.app.Activity
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.theswitchbot.common.R
import com.theswitchbot.common.logger.Logger
import com.theswitchbot.common.util.TipUtil
import java.lang.Exception

/**
 * BaseView 的一个委托实现类，
 * 独立出来是因为BaseView接口不能存储变量，但又不想把这部分代码写在BaseActivity或者BaseFragment里
 * 如果需要可随意替换当前委托类实现替换不同LoadingDialog的目的
 */
class BaseViewDelegate: BaseView {
    var loading: QMUITipDialog? = null

    override fun showLoading(activity: Activity) {
        try {
            loading = TipUtil.showLoading(activity, R.string.text_loading)
        } catch (ex: Exception) {
            Logger.e(ex, ex.message)
        }
    }

    override fun hideLoading() {
        try {
            if (loading?.isShowing == true) {
                loading?.dismiss()
            }
        } catch (ex: Exception) {
            Logger.e(ex, ex.message)
        }
    }
}