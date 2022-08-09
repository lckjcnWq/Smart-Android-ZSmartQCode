package com.theswitchbot.common.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.theswitchbot.common.R
import java.io.Serializable

object ARouterUtil {
    lateinit var aRouter: ARouter
    private val memDataMap = HashMap<String, Any>()

    fun init(debug: Boolean, context: Application) {
        if (debug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(context)
        aRouter = ARouter.getInstance()
    }


    /**
     * 使用 [ARouter] 根据 `path` 跳转到对应的页面, 这个方法因为没有使用 [Activity]跳转
     * 所以 [ARouter] 会自动给 [android.content.Intent] 加上 Intent.FLAG_ACTIVITY_NEW_TASK
     * 如果不想自动加上这个 Flag 请使用 [ARouter.getInstance] 并传入 [Activity]
     *
     * @param path `path`
     */
    fun navigation(path: String?): Any? {
        return aRouter.build(path).navigation()
    }

    /**
     * 使用 [ARouter] 根据 `path` 跳转到对应的页面, 如果参数 `context` 传入的不是 [Activity]
     * [ARouter] 就会自动给 [android.content.Intent] 加上 Intent.FLAG_ACTIVITY_NEW_TASK
     * 如果不想自动加上这个 Flag 请使用 [Activity] 作为 `context` 传入
     *
     * @param context
     * @param path
     */
    @JvmStatic
    fun navigation(context: Context, path: String) {
        aRouter.build(path).navigation(context)
    }

    fun navigation(
        activity: Activity?,
        path: String,
        requestCode: Int,
        putExtra: (Postcard) -> Unit = {}
    ) {
        if (activity == null) {
            return
        }
        val postcard = aRouter.build(path)
        putExtra.invoke(postcard)
        postcard.navigation(activity, requestCode)
    }


    /**
     * 跳转转场动画
     */
    fun navigation(
        activity: Activity?,
        path: String,
        requestCode: Int,
        compat: ActivityOptionsCompat,
        putExtra: (Postcard) -> Unit = {}
    ) {
        if (activity == null) {
            return
        }
        val postcard = aRouter.build(path)
        putExtra.invoke(postcard)
        //转场动画
        postcard.optionsBundle
        postcard.withOptionsCompat(compat)
        postcard.navigation(activity, requestCode)
    }


    fun navigation(
        context: Context?,
        path: String?,
        navigationCallback: NavigationCallback?
    ) {
        if (context == null) {
            return
        }

        aRouter.build(path).navigation(context, navigationCallback)
    }

    fun navigation(
        context: Context?,
        path: String?,
        putExtra: (Postcard) -> Unit
    ): Any? {
        if (context == null) {
            return null
        }

        val postcard = aRouter.build(path)
        putExtra.invoke(postcard)
        return postcard.navigation(context)
    }

    fun navigation(
        context: Context?,
        path: String?,
        vararg objects: Any
    ) {

        if (context == null) {
            return
        }

        val postcard = aRouter.build(path)
        if (null != objects && objects.size % 2 == 0) {
            for (index in 0 until objects.size / 2) {
                val key = objects[index * 2].toString()
                val values = objects[index * 2 + 1] ?: continue
                if (values is String) {
                    postcard.withString(key, values.toString())
                    continue
                }
                if (values is Int) {
                    postcard.withInt(key, values)
                    continue
                }
                if (values is Long) {
                    postcard.withLong(key, values)
                    continue
                }
                if (values is Float) {
                    postcard.withFloat(key, values)
                    continue
                }
                if (values is Double) {
                    postcard.withDouble(key, values)
                    continue
                }
                if (values is Boolean) {
                    postcard.withBoolean(key, values)
                    continue
                }
                if (values is Parcelable) {
                    if (values is Bundle) {
                        postcard.withBundle(key, values)
                        continue
                    }
                    postcard.withParcelable(key, values)
                    continue
                }
                if (values is Serializable) {
                    postcard.withSerializable(key, values)
                } else {
                    postcard.withObject(key, values)
                }
            }
        }
        postcard.navigation(context)
    }

    fun navigation(
        context: Context?,
        flag: Int,
        path: String?,
        vararg objects: Any
    ) {

        if (context == null) {
            return
        }

        val postcard = aRouter.build(path)
        if (null != objects && objects.size % 2 == 0) {
            for (index in 0 until objects.size / 2) {
                val key = objects[index * 2].toString()
                val values = objects[index * 2 + 1] ?: continue
                if (values is String) {
                    postcard.withString(key, values.toString())
                    continue
                }
                if (values is Int) {
                    postcard.withInt(key, values)
                    continue
                }
                if (values is Long) {
                    postcard.withLong(key, values)
                    continue
                }
                if (values is Float) {
                    postcard.withFloat(key, values)
                    continue
                }
                if (values is Double) {
                    postcard.withDouble(key, values)
                    continue
                }
                if (values is Boolean) {
                    postcard.withBoolean(key, values)
                    continue
                }
                if (values is Parcelable) {
                    if (values is Bundle) {
                        postcard.withBundle(key, values)
                        continue
                    }
                    postcard.withParcelable(key, values)
                    continue
                }
                if (values is Serializable) {
                    postcard.withSerializable(key, values)
                } else {
                    postcard.withObject(key, values)
                }
            }
        }
        postcard.withFlags(flag)
        postcard.navigation(context)
    }

    fun navigation(
        context: Context?, flag: Int, enterAnim: Int, exitAnim: Int,
        path: String?, vararg objects: Any
    ) {

        if (context == null) {
            return
        }

        val postcard = aRouter.build(path)
        if (null != objects && objects.size % 2 == 0) {
            for (index in 0 until objects.size / 2) {
                val key = objects[index * 2].toString()
                val values = objects[index * 2 + 1] ?: continue
                if (values is String) {
                    postcard.withString(key, values.toString())
                    continue
                }
                if (values is Int) {
                    postcard.withInt(key, values)
                    continue
                }
                if (values is Long) {
                    postcard.withLong(key, values)
                    continue
                }
                if (values is Float) {
                    postcard.withFloat(key, values)
                    continue
                }
                if (values is Double) {
                    postcard.withDouble(key, values)
                    continue
                }
                if (values is Boolean) {
                    postcard.withBoolean(key, values)
                    continue
                }
                if (values is Parcelable) {
                    if (values is Bundle) {
                        postcard.withBundle(key, values)
                        continue
                    }
                    postcard.withParcelable(key, values)
                    continue
                }
                if (values is Serializable) {
                    postcard.withSerializable(key, values)
                } else {
                    postcard.withObject(key, values)
                }
            }
        }
        postcard.withFlags(flag)
        postcard.withTransition(enterAnim, exitAnim)
        postcard.navigation(context)
    }

    fun navigation(
        context: Context?,
        path: String?,
        navigationCallback: NavigationCallback?,
        vararg objects: Any
    ) {

        if (context == null) {
            return
        }

        val postcard = aRouter.build(path)
        if (null != objects && objects.size % 2 == 0) {
            for (index in 0 until objects.size / 2) {
                val key = objects[index * 2].toString()
                val values = objects[index * 2 + 1] ?: continue
                if (values is String) {
                    postcard.withString(key, values.toString())
                    continue
                }
                if (values is Int) {
                    postcard.withInt(key, values)
                    continue
                }
                if (values is Long) {
                    postcard.withLong(key, values)
                    continue
                }
                if (values is Float) {
                    postcard.withFloat(key, values)
                    continue
                }
                if (values is Double) {
                    postcard.withDouble(key, values)
                    continue
                }
                if (values is Boolean) {
                    postcard.withBoolean(key, values)
                    continue
                }
                if (values is Parcelable) {
                    if (values is Bundle) {
                        postcard.withBundle(key, values)
                        continue
                    }
                    postcard.withParcelable(key, values)
                    continue
                }
                if (values is Serializable) {
                    postcard.withSerializable(key, values)
                } else {
                    postcard.withObject(key, values)
                }
            }
        }
        postcard.navigation(context, navigationCallback)
    }

    /**
     * 页面间传递占大量内存的数据时使用此方法  其中dataKey为目标页面取数据时使用key  memData则是目标页面需要的数据
     *
     * 示例 页面A传递数据到页面B
     * 在A中调用此navigation
     * 在B中调用retrieveMemData即可
     *
     */
    fun navigationWithMemData(
        context: Context?,
        path: String?,
        dataKey: String,
        memData: Any?,
        vararg objects: Any
    ) {
        if (memData != null){
            memDataMap[dataKey] = memData
        }
        navigation(context, path, *objects)
    }


    fun navigationWithMemData(
        activity: Activity?,
        path: String?,
        dataKey: String,
        memData: Any?,
        requestCode: Int,
        putExtra: (Postcard) -> Unit
    ) {
        if (activity == null) {
            return
        }
        if (memData != null){
            memDataMap[dataKey] = memData
        }
        val postcard = aRouter.build(path)
        putExtra.invoke(postcard)
        postcard.navigation(activity, requestCode)
    }


    /**
     * 谨慎使用  对不同目的页面传递不同数据时尽量使用不同key，避免数据被覆盖
     * 取出后从内存中清除，不清除的版本见[getMemData]
     */
    @Synchronized
    @JvmStatic
    fun <T> retrieveMemData(dataKey: String): T? {
        val data = memDataMap[dataKey]
        memDataMap.remove(dataKey)
        return try {
            data as T?
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 谨慎使用  对不同目的页面传递不同数据时尽量使用不同key，避免数据被覆盖
     */
    @Synchronized
    @JvmStatic
    fun <T> getMemData(dataKey: String): T? {
        val data = memDataMap[dataKey]
        return try {
            data as T?
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 谨慎使用  对不同目的页面传递不同数据时尽量使用不同key，避免数据被覆盖
     */
    @Synchronized
    @JvmStatic
     fun putMemData(dataKey: String,value:Any){
        memDataMap[dataKey]=value
    }

}