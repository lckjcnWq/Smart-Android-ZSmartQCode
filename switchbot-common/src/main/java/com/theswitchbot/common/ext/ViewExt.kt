package com.theswitchbot.common.ext

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.theswitchbot.common.CommonApp
import com.theswitchbot.common.logger.Logger
import com.theswitchbot.common.util.TipUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * 设置组件左边drawable
 */
fun View.setDrawableLeft(
    drawableId: Int? = -1,
    drawable: Drawable? = null,
    width: Int = 0,
    height: Int = 0,
    color: Int = 0
) {
    try {

        var drawableImg: Drawable? = null
        val res = this.context.resources
        if (drawableId != null && drawableId != -1) {
            drawableImg = ResourcesCompat.getDrawable(res, drawableId, null)
        }
        if (drawable != null) {
            drawableImg = drawable
        }

        //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        if (width != 0 && height != 0) {
            drawableImg?.setBounds(0, 0, width, height)
        } else if (width != 0 && height == 0) {
            drawableImg?.setBounds(0, 0, width, drawableImg.minimumHeight)
        } else if (width == 0 && height != 0) {
            drawableImg?.setBounds(0, 0, drawableImg.minimumWidth, height)
        } else {
            drawableImg?.setBounds(0, 0, drawableImg.minimumWidth, drawableImg.minimumHeight)
        }
        if (color != 0) {
            drawableImg?.changeIconColor(color) {
                drawableImg = it
            }
        }
        (this as? Button)?.setCompoundDrawables(
            drawableImg, this.compoundDrawables[1],
            this.compoundDrawables[2], this.compoundDrawables[3]
        )
        (this as? TextView)?.setCompoundDrawables(
            drawableImg, this.compoundDrawables[1],
            this.compoundDrawables[2], this.compoundDrawables[3]
        )
        (this as? EditText)?.setCompoundDrawables(
            drawableImg, this.compoundDrawables[1],
            this.compoundDrawables[2], this.compoundDrawables[3]
        )

    } catch (e: Exception) {
        Logger.d("etDrawableLeft", e)
    }

}


/**
 * 设置组件右边边drawable
 */
fun View.setDrawableRight(drawableId: Int, width: Int = 0, height: Int = 0, color: Int = 0) {
    try {
        var drawableImg: Drawable?
        val res = this.context.resources
        if (drawableId != 0) {
            drawableImg = ResourcesCompat.getDrawable(res, drawableId, null)
            //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            if (width != 0 && height != 0) {
                drawableImg?.setBounds(0, 0, width, height)
            } else if (width != 0 && height == 0) {
                drawableImg?.setBounds(0, 0, width, drawableImg.minimumHeight)
            } else if (width == 0 && height != 0) {
                drawableImg?.setBounds(0, 0, drawableImg.minimumWidth, height)
            } else {
                drawableImg?.setBounds(0, 0, drawableImg.minimumWidth, drawableImg.minimumHeight)
            }
        } else {
            drawableImg = null
        }
        if (color != 0) {
            drawableImg?.changeIconColor(color) {
                drawableImg = it
            }
        }
        (this as? Button)?.setCompoundDrawables(
            this.compoundDrawables[0], this.compoundDrawables[1],
            drawableImg, this.compoundDrawables[3]
        )
        (this as? TextView)?.setCompoundDrawables(
            this.compoundDrawables[0], this.compoundDrawables[1],
            drawableImg, this.compoundDrawables[3]
        )
        (this as? EditText)?.setCompoundDrawables(
            this.compoundDrawables[0], this.compoundDrawables[1],
            drawableImg, this.compoundDrawables[3]
        )

    } catch (e: Exception) {
        Logger.d("setDrawableRight", e)
    }
}


/**
 * 改变背景图颜色
 * @param colorId
 */
@Suppress("DEPRECATION")
fun View.filterColorBg(@ColorInt colorId: Int) {
    background?.setColorFilter(colorId, PorterDuff.Mode.SRC_IN)
}


val View.isVisible: Boolean
    get() = visibility == View.VISIBLE

val View.isInvisible: Boolean
    get() = visibility == View.INVISIBLE

val View.isGone: Boolean
    get() = visibility == View.GONE

/***
 * 设置延迟时间的View扩展
 * @param delay Long 延迟时间，默认300毫秒
 * @return T
 */
fun <T : View> T.withTrigger(delay: Long = 300): T {
    triggerDelay = delay
    return this
}

/***
 * 点击事件的View扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    if (clickEnable()) {
        block(this)
    }
}

/***
 * 带延迟过滤的点击事件View扩展
 * @param time Long 延迟时间，默认300毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(time: Long = 300, block: (T) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(this)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}


/**
 * 通过 view 获取 activity
 */
fun View.getActivityFromView(): Activity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}


/**
 * BaseQuickAdapter 的 setOnItemChildClickListener 防抖动点击
 * 快速点击频率最高为300ms/s
 */
fun <T, VH : BaseViewHolder> BaseQuickAdapter<T, VH>.setOnItemChildClickListenerTrigger(
    delay: Long = 300,
    listener: OnItemChildClickListener?
) {
    setOnItemChildClickListener { adapter, view, position ->
        view.withTrigger(delay)
        if (view.clickEnable()) {
            listener?.onItemChildClick(adapter, view, position)
        }
    }
}

/**
 * BaseQuickAdapter 的 setOnItemClickListener 防抖动点击
 */
fun <T, VH : BaseViewHolder> BaseQuickAdapter<T, VH>.setOnItemClickListenerTrigger(
    delay: Long = 300,
    listener: OnItemClickListener?
) {
    setOnItemClickListener { adapter, view, position ->
        view.withTrigger(delay)
        if (view.clickEnable()) {
            listener?.onItemClick(adapter, view, position)
        }
    }
}


/**
 * 全局可用弹吐司
 */
fun showToast(msg: String) {
    try {
        CoroutineScope(Dispatchers.Main).launch {
            TipUtil.showToast(CommonApp.instance, msg)
        }
    } catch (ex: Exception) {
        Logger.e(ex.message, ex)
    }
}

/**
 * 全局可用弹吐司
 */
fun showToast(resId: Int) {
    showToast(resId.resIdToString())
}