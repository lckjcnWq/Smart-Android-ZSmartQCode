package com.theswitchbot.common.manager

import android.app.Activity
import android.text.TextUtils

/**
 * Activity管理类，将需要添加管理的activity加入到HashMap中
 *
 * 小心使用，易造成内存泄漏
 */
object ActivityListManager {
    //存储需要销毁的activity
    private val destoryActivityMap: MutableMap<String, Activity> = HashMap()

    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */
    fun addDestoryActivity(activity: Activity, activityName: String) {
        destoryActivityMap[activityName] = activity
    }

    /**
     * 销毁指定Activity
     * @param activityTag:需要删除的activity
     * @param isAllDelete：是否全部删除
     */
    @JvmStatic
    fun destoryActivity(activityTag: String, isAllDelete: Boolean) {
        val keySet: Set<String> = destoryActivityMap.keys
        if (keySet != null) {
            if (isAllDelete) {
                for (key in keySet) {
                    destoryActivityMap[key]!!.finish()
                }
                destoryActivityMap.clear()
            } else {
                if (!TextUtils.isEmpty(activityTag)) {
                    for (key in keySet) {
                        if (key == activityTag) {
                            destoryActivityMap[key]!!.finish()
                            destoryActivityMap.remove(key)
                            break
                        }
                    }
                }
            }
        }
    }
}
