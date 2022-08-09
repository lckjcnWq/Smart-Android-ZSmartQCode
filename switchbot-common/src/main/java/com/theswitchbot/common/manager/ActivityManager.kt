package com.theswitchbot.common.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.model.RouteMeta
import com.alibaba.android.arouter.launcher.ARouter
import com.theswitchbot.common.logger.Logger
import java.util.HashMap

/**
 * 管理应用启动和销毁的Activity实例
 */
object ActivityManager : Application.ActivityLifecycleCallbacks {

    private val activities = ArrayList<Activity>()

    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activities.add(activity)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        activities.remove(activity)
    }

    /**
     * 结束所有已启动的activity
     */
    @JvmStatic
    fun finishAll() {
        val list = ArrayList(activities)
        list.forEach { it.finish() }
        activities.clear()
    }


    /**
     * 结束除MainActivity的所有已启动的activity
     */
    @JvmStatic
    fun finishExceptMainActivity(activityName: String) {
        val list = ArrayList(activities)
        list.filter {
            !it.componentName.className.contains(activityName)
        }.forEach {
            it.finish()
        }
        activities.filter {
            !it.componentName.className.contains(activityName)
        }.toMutableList().clear()
    }
    /**
     * 获取顶部的activity
     */
    @JvmStatic
    fun getTop(): Activity? {
        return getActivity(1)
    }

    /**
     * 获取自顶向下排序的activity
     */
    @JvmStatic
    fun getActivity(order: Int): Activity?{

        if (activities.size >= order){
            return activities[activities.size - order]
        }

        return null
    }



    /**
     * 判断某个路由所指向的activity是否存在栈内
     */
    fun isOpenActivity(path: String): Boolean {
        try {
            val destClass = getActivityNameByPath(path)

            for (activity in activities) {
                if (activity::class.java == destClass) {
                    return true
                }
            }
            return false
        } catch (ex: Exception) {
            Logger.e(ex, "")
            return false
        }
    }


    /**
     * 一直退出，直到某个Activity
     * @param path ARouter 的路径
     * @param isFinishIncludeLast 是否将所指定的Activity也一并finish掉
     */
    fun finishToActivity(path: String, isFinishIncludeLast: Boolean) {
        try {
            val destClass = getActivityNameByPath(path)

            //先判断是否存在终点
            var hasEndPoint = false
            for (activity in activities) {
                if (activity::class.java == destClass) {
                    hasEndPoint = true
                }
            }
            if (!hasEndPoint) {
                Logger.e(Exception("销毁activity失败，不存在销毁终点"), "")
                return
            }

            for (i in activities.size - 1 downTo 0) {
                val tempActivity = activities[i]

                if (destClass == tempActivity::class.java) {
                    if (isFinishIncludeLast) {
                        tempActivity.finish()
                    }
                    return
                }

                tempActivity.finish()
            }

        } catch (ex: Exception) {
            Logger.e(ex, ex.message)
        }

    }


    /**
     * 将ActivityA到ActivityB之间的Activity都销毁
     * 包含1 不包含2
     */
    fun finishActivityFromRange(path1: String, path2: String) {
        try {
            val destClass1 = getActivityNameByPath(path1)
            val destClass2 = getActivityNameByPath(path2)

            //先判断是否存在终点
            var hasStartPoint = false
            var hasEndPoint = false
            for (i in activities.size - 1 downTo 0) {
                val temp = activities[i]
                if (temp::class.java == destClass1) {
                    hasStartPoint = true
                }

                if (temp::class.java == destClass2) {
                    //需要保证起点早于终点
                    if (hasStartPoint) {
                        hasEndPoint = true
                    }
                }
            }
            if (!hasStartPoint || !hasEndPoint) {
                Logger.e(Exception("销毁activity失败，不存在有效的起点/终点"), "")
                return
            }

            //开始销毁操作
            var begin = false
            for (i in activities.size - 1 downTo 0) {
                val tempActivity = activities[i]

                if (destClass1 == tempActivity::class.java) {
                    begin = true
                }

                if (destClass2 == tempActivity::class.java) {
                    return
                }

                if (begin) {
                    tempActivity.finish()
                }
            }
        } catch (ex: java.lang.Exception) {
            Logger.e(ex.message, ex)
        }
    }


    /**
     * 获取Activity路由映射表
     */
    @Suppress("UNCHECKED_CAST")
    private fun getRouterMapper(): HashMap<String, RouteMeta>? {
        val wareHouseClass = Class.forName("com.alibaba.android.arouter.core.Warehouse")
        val constructor = wareHouseClass.getDeclaredConstructor()
        constructor.isAccessible = true
        val wareHouse = constructor.newInstance()
        val field = wareHouse.javaClass.getDeclaredField("routes")
        field.isAccessible = true

        return field.get(wareHouse) as? HashMap<String, RouteMeta>
    }


    /**
     * 根据路由拿到对应的Activity类名
     */
    private fun getActivityNameByPath(path: String): Class<*> {
        val postCard = ARouter.getInstance().build(path)
        LogisticsCenter.completion(postCard)
        return postCard.destination
    }


}