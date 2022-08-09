package com.theswitchbot.common.ui

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.theswitchbot.common.viewmodel.BaseViewModel


interface BaseView {

    /**
     * 弹出loading窗口
     */
    fun showLoading(activity: Activity)

    /**
     * 取消loading窗口
     */
    fun hideLoading()

    /**
     * 监听BaseViewModel 的事件
     */
    fun observerLoadingStatus(lifecycleOwner: LifecycleOwner, viewModel: BaseViewModel) {
        //监听baseViewModel的loading事件
        viewModel.loadingState.observe(lifecycleOwner) {
            if (it != null) {
                if (lifecycleOwner is Activity) {
                    if (it) showLoading(lifecycleOwner) else hideLoading()
                } else if (lifecycleOwner is Fragment) {
                    if (it) showLoading(lifecycleOwner.requireActivity()) else hideLoading()
                }
            }
        }
    }
}
