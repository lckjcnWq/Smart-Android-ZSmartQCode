/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.theswitchbot.common.net.wrapper


import com.theswitchbot.common.net.wrapper.Status.*


/**
 * 网络请求数据封装类：结果主要分为三种状态：成功，失败，自定义的加载状态
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, val message: String?, val code: Int?) {
    companion object {

        fun <T> success(data: T? = null, message: String? = null): Resource<T> {
            return Resource(SUCCESS, data, message, null)
        }

        fun <T> error(msg: String? = null, code: Int? = null, data: T? = null): Resource<T> {
            return Resource(ERROR, data, msg, code)
        }

        fun <T> showLoading(data: T? = null): Resource<T> {
            return Resource(LOADING_START, data, null, null)
        }

        fun <T> hideLoading(data: T? = null): Resource<T> {
            return Resource(LOADING_END, data, null, null)
        }


    }

    inline fun handleSuccess(doSuccess: (data: T?, msg: String?) -> Unit): Resource<T> {
        if (status == SUCCESS) {
            doSuccess.invoke(data, message)
        }
        return this
    }

    inline fun handleError(doError: (msg: String?, code: Int?, data: T?) -> Unit): Resource<T> {
        if (status == ERROR) {
            doError.invoke(message, code, data)
        }
        return this
    }

    inline fun handleLoading(doLoading: (isShow: Boolean) -> Unit): Resource<T> {
        if (status == LOADING_START) {
            doLoading.invoke(true)
        } else if (status == LOADING_END) {
            doLoading.invoke(false)
        }
        return this
    }
}
