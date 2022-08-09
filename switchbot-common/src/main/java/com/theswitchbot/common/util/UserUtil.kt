package com.theswitchbot.common.util

import android.util.Base64
import com.alibaba.fastjson.JSONObject
import com.theswitchbot.common.logger.Logger
import java.text.SimpleDateFormat
import java.util.*

object UserUtil {
    //用户是否已登录
    fun isLogin():Boolean=getToken()!=null


    fun getUserId():String{
        try {
            val username = getToken() ?: return ""
            val split = username.split(".").toTypedArray()
            val decode = Base64.decode(split[1], Base64.DEFAULT)
            val jsonObject = org.json.JSONObject(String(decode))
            return jsonObject.getString("sub")
        } catch (e: Exception) {
            Logger.e(e.message)
        }
        return ""
    }


    fun setLoginEmail(email: String) {
        SpAccessor.put(SpAccessor.LOGIN_EMAIL, email)
    }

    fun getLoginEmail(): String {
        return SpAccessor.getString(SpAccessor.LOGIN_EMAIL, "")
    }

    fun getToken():String?{
        val info= SpAccessor.getString(SpAccessor.TOKEN, "")
        if (info.isEmpty()) {
            return null
        }
        try {
            val obj = JSONObject.parseObject(info)
            val token = obj.getString("accessToken")
            return if (token.isBlank()) {
                null
            } else {
                token
            }
        }catch (e: Exception){

        }
        return null
    }

    fun getMqttClientId():String{
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
        val format = simpleDateFormat.format(Date())
        val phoneUUID = format + "-" + HexUtil.getRandomBase62Bytes() + HexUtil.getRandomBase62Bytes()
        val userSubID = try { getUserId()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            UUID.randomUUID().toString()
        }
        return "APP_Android_" + userSubID + "_" + phoneUUID
    }

    fun getRefreshToken():String?{
        val info= SpAccessor.getString(SpAccessor.TOKEN, "")
        if (info.isEmpty()){
            return null
        }
        try {
            val obj= JSONObject.parseObject(info)
            return obj.getString("refreshToken")
        }catch (e: Exception){

        }
        return null
    }

    fun getIdToken():String? {
        val info= SpAccessor.getString(SpAccessor.TOKEN, "")
        if (info.isEmpty()){
            return null
        }
        try {
            val obj= JSONObject.parseObject(info)
            return obj.getString("idToken")
        }catch (e: Exception){

        }
        return null
    }
    /**
     * 重置密码后需要处理
     * @param needHandle 是否还需要处理，重新登录
     */
    fun setResetPasswordNeedHandle(needHandle:Boolean) {
        SpAccessor.put(SpAccessor.RESET_PWD_SHOULD_RE_LOGIN, needHandle)
    }

    /**
     * 是否还需要重新登录
     */
    fun isResetPasswordNeedHandle():Boolean {
        if (isLogin()) {
            return SpAccessor.getBool(SpAccessor.RESET_PWD_SHOULD_RE_LOGIN,false)
        }
        return false
    }
}