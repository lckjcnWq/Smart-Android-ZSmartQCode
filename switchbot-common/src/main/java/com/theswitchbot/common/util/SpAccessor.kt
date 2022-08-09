package com.theswitchbot.common.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.lang.reflect.Type

object SpAccessor {
    private const val FILE_NAME = "share_data"
    private lateinit var app:Application

    //token信息
    const val TOKEN="token_info"
    //用户账号密码
    const val USER_ACCOUNT="user_info"
    //当前选中家庭id
    const val CURRENT_FAMILY_ID="current_family_id"

    //当前选中家庭的所有者的用户id
    const val CURRENT_FAMILY_OWNER_ID = "current_family_owner_id"

    //当前使用蓝牙底层库
    const val CURRENT_BLE_LIB="current_ble_lib"
    //MQtt证书
    const val MQTT_CERT="mqtt_cert"

    //设备密码存储前缀
    const val BOT_PWD_PREFIX="bot-pwd:"

    //温度单位
    const val METER_UNIT="meter_unit"
    //IPC截图时间
    const val IPC_SNAPSHOT_PREFIX="ipc_snapshot:"
    //IPC截图路径
    const val IPC_SNAPSHOT_PATH_PREFIX="ipc_snapshot_uri:"

    //motion sensor打开灯，tip提示
    const val MOTION_LED_TIP_KEY="MOTION_LED_TIP_KEY"

    //contact sensor离家模式，tip提示
    const val CONTACT_GO_OUT_TIP_KEY = "CONTACT_GO_OUT_TIP_KEY"
    //hub灯光设置状态
    const val HUB_LIGHT_STATUS="hub_light:"
    //hub灯光颜色值列表
    const val HUB_LIGHT_LIST="hub_light_list"
    //Curtain设备光感数据来源
    const val CURTAIN_LIGHT_SOURCE="curtain_light_source:"
    //登陆渠道
    const val LOGIN_CHANNEL = "sp_login_channel"

    //设置订阅邮件值
    const val USER_SUB_EMAIL="user_sub_email"
    //第三方登陆邮箱
    const val LOGIN_EMAIL = "sp_login_email"
    //用户昵称
    const val LOGIN_NICKNAME = "sp_login_nickname"
    //用户数据 例如设备升级版本号等
    const val USER_DATA="user_data"
    //OTA包下载路径等信息
    const val OTA_BASE_URL="ota_base_url"
    //pingTop bot置顶在首页的bot设备列表
    const val PING_TOP_BOT="bot_ping_top"
    //是否显示开发者选项
    const val SHOW_DEVELOP_ITEM="show_development"
    //语言设置
    const val LANGUAGE_SETTINGS="language"
    //开发者openToken
    const val DEVELOPMENT_TOKEN="development_token"
    //显示过启动页的versionCode
    const val WELCOME_SHOWED_CODE = "welcome_showed_version_code"

    //----------门锁-----------
    //启用远程开锁确认提示
    const val UNLOCK_REMOTE_TIP_ENABLE = "unlockRemoteTipEnable"

    //----------remote-----------
    //添加Other遥控器教程不再提示
    const val REMOTE_OTHER_COURSE_TIP_KEY= "REMOTE_OTHER_COURSE_TIP_KEY"
    //添加遥控器教程不再提示
    const val REMOTE_COURSE_TIP_KEY = "REMOTE_COURSE_TIP_KEY"
    //添加Other遥控器教程不再提示
    const val SP_REMOTE_OTHER_COURSE_TIP_KEY = "SP_REMOTE_OTHER_COURSE_TIP_KEY"
    //添加自定义遥控器教程不再提示
    const val SP_REMOTE_CUSTOMIZE_COURSE_TIP_KEY = "SP_REMOTE_CUSTOMIZE_COURSE_TIP_KEY"

    //----------remote-----------

    //----------widget-----------
    const val WIDGET_OPEN_API_TOKEN = "widget_open_api_token"
    const val WIDGET_SINGLE_SCENE_DATA = "widget_single_scene_data"
    const val WIDGET_SINGLE_ACTION_DATA = "widget_single_action_data"
    const val WIDGET_DOUBLE_ACTION_DATA = "widget_double_action_data"
    //----------widget-----------


    //-----------家庭角色----------
    const val FAMILY_ROLE="FAMILY_ROLE_VALUE"
    //meter本地数据上次上传到云端时间
    const val METER_DATA_UPLOAD_TIME = "meter_data_upload_time"

    const val FIREBASE_TOKEN = "firebase_token"
    //旧版app数据是否需要迁移
    const val NEED_RUN_MIGRATE = "need_handle_legacy_info"
    //小组件迁移需要分两步，用单独的tag记录
    const val NEED_HANDLE_LEGACY_WIDGET_INFO = "need_handle_legacy_widget_info"
    //修改密碼后需要重新登錄
    const val RESET_PWD_SHOULD_RE_LOGIN = "reset_pwd_should_re_login"

    //当前远程需要远程音视频通话的验证信息
    const val P2P_VERIFY_INFO="p2p_verify_info"

    fun init(app: Application){
        SpAccessor.app=app
    }


    private fun getSp(): SharedPreferences {
        return app.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 清除所有数据
     */
    fun clear() {
        val editor = getSp().edit()
        editor.clear()
        editor.apply()
    }

    fun <T> put(key: String, value: T) {
        val sp = getSp()
        val editor = sp.edit()
        when (value) {
            is String -> {
                editor.putString(key, value)
            }
            is Int -> {
                editor.putInt(key, value)
            }
            is Boolean -> {
                editor.putBoolean(key, value)
            }
            is Float -> {
                editor.putFloat(key, value)
            }
            is Long -> {
                editor.putLong(key, value)
            }
            else -> {
                editor.putString(key, gson.toJson(value))
            }
        }
        editor.apply()
    }

    fun delete(key: String){
        getSp().edit().remove(key).apply()
    }

    fun <T> put(context: Context, key: String, value: T) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        when (value) {
            is String -> {
                editor.putString(key, value)
            }
            is Int -> {
                editor.putInt(key, value)
            }
            is Boolean -> {
                editor.putBoolean(key, value)
            }
            is Float -> {
                editor.putFloat(key, value)
            }
            is Long -> {
                editor.putLong(key, value)
            }
            else -> {
                editor.putString(key, gson.toJson(value))
            }
        }
        editor.apply()
    }

    fun getLong(key: String, defVal: Long): Long {
        return getSp().getLong(key, defVal)
    }


    fun getInt(key: String, defVal: Int): Int {
        return getSp().getInt(key, defVal)
    }

    fun getString(key: String, defVal: String=""): String {
        return getSp().getString(key, defVal) ?: defVal
    }

    fun getBool(key: String, defVal: Boolean): Boolean {
        return getSp().getBoolean(key, defVal)
    }

    fun getFloat(key: String, defVal: Float): Float {
        return getSp().getFloat(key, defVal)
    }

    fun <T> getObject(key: String, clazz: Class<T>): T? {
        val str = getString(key, "")
        if (str.isNullOrEmpty()) {
            return null
        }
        return gson.fromJson(str, clazz)
    }

    /**
     * 泛型对象
     */
    fun <T> getTObject(key: String, type: Type): T? {
        val str = getString(key, "")
        if (str.isNullOrEmpty()) {
            return null
        }
        return gson.fromJson(str, type)
    }






}