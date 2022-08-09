package com.theswitchbot.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * 解决在ViewPager2 中嵌套SwipeRefreshLayout滑动冲突问题
 */
class PagerSwipeRefreshLayout :SwipeRefreshLayout{
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    private var startX = 0
    private var beginScroll = false //是否开始滑动
    private var startY: Int = 0
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()
                val disX = Math.abs(endX - startX)
                val disY: Int = Math.abs(endY - startY)
                if (disX > disY) {
                    if (!beginScroll)
                        parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    beginScroll = true
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
                beginScroll=false
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}