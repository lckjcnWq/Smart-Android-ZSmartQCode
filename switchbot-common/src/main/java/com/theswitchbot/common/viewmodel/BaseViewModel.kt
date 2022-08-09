package com.theswitchbot.common.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {
    /**
     * 整个界面的loading窗口状态监听
     */
    var loadingState = MutableLiveData<Boolean>()
}