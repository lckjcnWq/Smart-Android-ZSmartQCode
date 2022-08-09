package com.theswitchbot.common.ext

import com.google.gson.JsonSyntaxException
import com.theswitchbot.common.logger.Logger
import com.theswitchbot.common.net.dto.NetResponse
import com.theswitchbot.common.util.gson
import java.lang.RuntimeException
import java.lang.reflect.Type

/**
 * 提取网络请求响应结果的内容
 * 根据响应数据的code是否是成功来判断
 */
inline fun <T> toApiResult(defVal: T? = null, block: ()-> NetResponse<T>?): T? {
    try {
        val response = block.invoke()
        if (response != null && response.isSuccessBiz()) {
           return response.body
        }
    } catch (ex: Exception) {
        Logger.e(ex, ex.message)
    }
    return defVal
}



fun <T> String.fromJson(clazz: Class<T>): T? {
    return try {
        gson.fromJson(this, clazz)
    } catch (e: JsonSyntaxException) {
        Logger.e(e, e.message)
        null
    }
}

fun <T> String.fromJson(type: Type): T? {
    return try {
        gson.fromJson<T>(this, type)
    } catch (e: JsonSyntaxException) {
        Logger.e(e, e.message)
        null
    }
}

fun Any.toJson(): String {
    return gson.toJson(this)
}

/**
 * 常见于Json解析成LinkedHashMap，但具体使用却需要转换为另一个实体的情况
 */
fun <T> Any.toObject(clazz: Class<T>): T? {
    return try {
        this.toJson().fromJson(clazz)
    } catch (ex: java.lang.Exception) {
        Logger.e(ex, ex.message)
        null
    }
}

/**
 * see [Any.toObject]
 */
fun <T> Any.toObject(type: Type): T? {
    return try {
        this.toJson().fromJson(type)
    } catch (ex: java.lang.Exception) {
        Logger.e(ex, ex.message)
        null
    }
}