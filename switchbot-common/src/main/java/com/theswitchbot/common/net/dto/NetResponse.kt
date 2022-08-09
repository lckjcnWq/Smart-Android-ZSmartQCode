package com.theswitchbot.common.net.dto

import com.theswitchbot.common.net.BizStatus

data class NetResponse<T>(var statusCode:Int, val body:T, val message:String) {

    fun isSuccessBiz(): Boolean {
        return statusCode == BizStatus.success.code
    }

    companion object{
        fun otherError(t:Throwable):NetResponse<Any>{
            return NetResponse(BizStatus.unknownError.code,Any(),t.message?:"")
        }
    }

}