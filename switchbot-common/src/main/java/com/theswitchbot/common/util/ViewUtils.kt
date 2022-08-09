package com.theswitchbot.common.util

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

object ViewUtils {

    /**
     * 手动测量view的尺寸
     */
    fun measure(view: View): Array<Int> {
        val lp = view.layoutParams
        val context = view.context
        view.measure(getWidth(lp, context), getHeight(lp, context))

        return arrayOf(view.measuredWidth, view.measuredHeight)
    }

    private fun getWidth(lp: ViewGroup.LayoutParams, context: Context): Int {
        if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return View.MeasureSpec.makeMeasureSpec((1 shl 30) - 1, View.MeasureSpec.AT_MOST)
        } else if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            val screenWidth = getScreenWidth(context)
            return View.MeasureSpec.makeMeasureSpec(screenWidth, View.MeasureSpec.EXACTLY)
        }
        return View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY)
    }

    private fun getHeight(lp: ViewGroup.LayoutParams, context: Context): Int {
        if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return View.MeasureSpec.makeMeasureSpec((1 shl 30) - 1, View.MeasureSpec.AT_MOST)
        } else if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            val screenHeight = getScreenHeight(context)
            return View.MeasureSpec.makeMeasureSpec(screenHeight, View.MeasureSpec.EXACTLY)
        }
        return View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY)
    }

    private fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    private fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }
}