package com.theswitchbot.common.util

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*
import android.os.Build
import android.os.Bundle

import android.os.LocaleList

import android.util.DisplayMetrics
import android.util.Log
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * 判断是否某地区示例
 * 是否简体中文：LocaleUtil.getLocale().toLanguageTag()==Locale.SIMPLIFIED_CHINESE.toLanguageTag()
 * 是否台湾省繁体中文：LocaleUtil.getLocale().toLanguageTag()==Locale.TRADITIONAL_CHINESE.toLanguageTag()
 * 是否日语：LocaleUtil.getLocale().toLanguageTag()==Locale.JAPAN.toLanguageTag()
 * 是否美式英语：LocaleUtil.getLocale().toLanguageTag()==Locale.US.toLanguageTag()
 * etc.....
 */
object LocaleUtil {

    private val activities = ArrayList<Activity>()
    private const val DEFAULT_TAG = "default"
    val DEFAULT_LOCALE: Locale = Locale.forLanguageTag(DEFAULT_TAG)
    // 更新应用的语言会导致Locale.getDefault()也被更新，这里直接记录未更新时的系统语言
    private var SYSTEM_LOCALE = Locale.getDefault()
    private var count = 0

    //是否进入后台
    var isBackground = false


    fun init(app: Application) {
        updateLocale(app)
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activities.add(activity)
                updateLocale(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                if (count == 0) {
                    isBackground=false
                   // Log.e("activity= ", ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle")
                }
                count++
            }

            override fun onActivityResumed(activity: Activity) {
                updateLocale(activity)
            }

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                count--
                if (count == 0) {
                    isBackground = true
                  //  Log.e("activity= ", ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle")
                }

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                activities.remove(activity)
            }

        })

        app.registerComponentCallbacks(object : ComponentCallbacks {
            /**
             * 系统设置修改语言时会回调
             */
            override fun onConfigurationChanged(newConfig: Configuration) {
                SYSTEM_LOCALE = Locale.getDefault()
                updateLocale(app)
                activities.forEach { updateLocale(it) }
            }

            override fun onLowMemory() {

            }

        })
    }

    /**
     * 设置语言地区
     */
    fun setLocale(context: Context, locale: Locale) {
        SpAccessor.put(SpAccessor.LANGUAGE_SETTINGS, locale.toLanguageTag())
        updateLocale(context)
    }

    private fun updateLocale(context: Context) {
        val locale = getLocale()
        setAppLanguage(context, locale)
        setAppLanguage(context.applicationContext, locale)
    }


    /**
     * 更新应用语言（核心）
     *
     * 注意：这里更新语言会导致Locale.getDefault()也会更新
     */
    private fun setAppLanguage(context: Context, locale: Locale) {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        val configuration: Configuration = resources.configuration
        //Android 7.0以上的方法
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                configuration.setLocale(locale)
                configuration.setLocales(LocaleList(locale))
                context.createConfigurationContext(configuration)
                resources.updateConfiguration(configuration, metrics)
            }
            else -> {
                configuration.setLocale(locale)
                resources.updateConfiguration(configuration, metrics)
            }
        }
    }


    /**
     * 获取语言地区
     */
    fun getLocale(): Locale {
        val languageSettings = SpAccessor.getString(SpAccessor.LANGUAGE_SETTINGS, DEFAULT_TAG)
        return if (languageSettings.isEmpty() || languageSettings == DEFAULT_TAG) {
            getSystemLocale()
        } else {
            Locale.forLanguageTag(languageSettings)
        }
    }

    fun getSystemLocale(): Locale {
        return SYSTEM_LOCALE
    }

    /**
     * 用于匹配选中的语言，获取正确的locale请使用getLocale()
     */
    fun getSelectedLocale():Locale{
        val languageSettings = SpAccessor.getString(SpAccessor.LANGUAGE_SETTINGS, DEFAULT_TAG)
        return if (languageSettings.isEmpty() || languageSettings == DEFAULT_TAG) {
            DEFAULT_LOCALE
        } else {
            Locale.forLanguageTag(languageSettings)
        }
    }

    fun equals(locale1: Locale, locale2: Locale): Boolean {
        return locale1.toLanguageTag() == locale2.toLanguageTag()
    }

    /**
     * 将lang转换为服务器目前支持的格式
     */
    fun lang2ServerLang(lang:String):String {
        return when(lang) {
            "zh-CN" -> "cn" //简中
            "zh-TW" -> "zh" //繁中
            "ja" -> "ja"
            "ko" -> "ko"
            "en" -> "en"
            else -> {
                //default状态下有地区标识，en-AU,zh-Hans-CN之类
                if (lang.startsWith("zh")) {
                    if (lang.endsWith("CN")) {
                        "cn"
                    } else {
                        "zh"
                    }
                } else if (lang.startsWith("en")) {
                    "en"
                } else if (lang.startsWith("ja")) {
                    "ja"
                } else if (lang.startsWith("ko")) {
                    "ko"
                } else {
                    "en"
                }
            }
        }
    }

    /**
     * 语言是否为中文或日文
     */
    fun isZhJaLang():Boolean {
        val languageTag = getLocale().toLanguageTag()
        return languageTag.startsWith("zh") || languageTag.startsWith("ja")
    }
}