package com.theswitchbot.common.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import com.blankj.utilcode.util.ActivityUtils
import org.apache.commons.lang3.ArrayUtils
import android.content.SharedPreferences
import com.blankj.utilcode.util.FileUtils
import com.theswitchbot.common.logger.Logger


object AppUtil {
    //google assistant app包名
    const val CHROME_CAST="com.google.android.apps.chromecast.app"
    //ifttt app包名
    const val IFTTT="com.ifttt.ifttt"
    //line app包名
    const val LINE="com.linecorp.clova"

    const val ALEXA_CODE_URL = "https://app.home.switch-bot.com/?code="
    const val ALEXA_WEB_CANCEL = "https://app.home.switch-bot.com/?error_description=Access+not+permitted.&state=111&error=access_denied"

    /**
     * Open another app.
     *
     * @param context     current Context, like Activity, App, or Service
     * @param packageName the full package name of the app to open
     * @param tryMarket   打开失败时是否在应用市场搜索app
     * @return true if likely successful, false if unsuccessful
     */
    fun openApp(context: Context, packageName: String,tryMarket:Boolean): Boolean {
        val manager = context.packageManager
        return try {
            val i = manager.getLaunchIntentForPackage(packageName)
            //throw new ActivityNotFoundException();
            i?.addCategory(Intent.CATEGORY_LAUNCHER)
            context.startActivity(i)
            true
        } catch (e: Exception) {
            if (tryMarket){
                openMarketApp(context,packageName)
            }
            false
        }
    }

    /**
     * 在应用市场中打开app
     */
    fun openMarketApp(context: Context, packageName: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }


    /**
     * 查找系统安装的浏览器
     */
    @SuppressLint("WrongConstant")
    fun findBrowser(context: Context):String{
        val browserList = arrayOf("com.android.browser", "com.huawei.browser", "com.android.chrome", "org.mozilla.firefox")

        val pm = context.packageManager
        val intent = Intent("android.intent.action.VIEW").apply {
            addCategory("android.intent.category.DEFAULT")
            addCategory("android.intent.category.BROWSABLE")
            data = Uri.parse("http://")
        }

        val list = pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS)
        for (resolveInfo in list) {
            if (ArrayUtils.contains(browserList, resolveInfo.activityInfo.packageName)) {
                return resolveInfo.activityInfo.packageName
            }
        }
        for (resolveInfo in list) {
            if (resolveInfo.activityInfo.packageName.contains("browser")) {
                return resolveInfo.activityInfo.packageName
            }
        }
        return "com.android.chrome"
    }

    fun isAlexaAppInstall(context: Context): Boolean {
        val alexaAppPkg = "com.amazon.dee.app"
        val requiredMinVerCode = 866607211
        val packageManager: PackageManager = context.packageManager
        try {
            val info: PackageInfo = packageManager.getPackageInfo(alexaAppPkg, 0)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.longVersionCode > requiredMinVerCode
            } else {
                true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    fun finishToActivity(className:String,includeSelf:Boolean){
        ActivityUtils.finishToActivity(Class.forName(className) as Class<out Activity>,includeSelf)
    }

    fun finishActivity(clz: Class<out Activity?>) {
        ActivityUtils.finishActivity(clz, false)
    }

    /**
     * 清空SP存储的内容
     */
    fun clearSp(context: Context,spName:String){
        try {
            val pref: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.clear()
            editor.apply()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    /**
     * 清除APP内所有数据
     */
    fun clearAllData(context: Context){
        FileUtils.deleteAllInDir("/data/data/${context.packageName}/app_MqttConnection")
        FileUtils.deleteAllInDir("/data/data/${context.packageName}/app_textures")
        FileUtils.deleteAllInDir("/data/data/${context.packageName}/cache")
        FileUtils.deleteAllInDir("/data/data/${context.packageName}/code_cache")
        FileUtils.deleteAllInDir("/data/data/${context.packageName}/files")
        FileUtils.deleteAllInDir("/data/data/${context.packageName}/lib-main")
        FileUtils.deleteAllInDir("/data/data/${context.packageName}/no_backup")
        val needRunMigrate = SpAccessor.getBool(SpAccessor.NEED_RUN_MIGRATE,true)
        val isLegacyWidgetNeedHandle = SpAccessor.getBool(SpAccessor.NEED_HANDLE_LEGACY_WIDGET_INFO,true)
        //清除数据前缓存营销通知开关提示标记，避免重复提示，6.3以后版本可删除
        FileUtils.deleteAllInDir("/data/data/${context.packageName}/shared_prefs")
        SpAccessor.put(SpAccessor.NEED_RUN_MIGRATE,needRunMigrate)
        SpAccessor.put(SpAccessor.NEED_HANDLE_LEGACY_WIDGET_INFO,isLegacyWidgetNeedHandle)
        Logger.d("已清除APP内所有缓存数据")
    }
}