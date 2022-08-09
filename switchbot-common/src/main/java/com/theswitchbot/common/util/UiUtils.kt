package com.theswitchbot.common.util

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView
import com.theswitchbot.common.R
import com.theswitchbot.common.widget.QMUITopBarWrapper
import com.theswitchbot.common.widget.seekbar.Utils.dp2px


/**
 * ui相关的工具类
 */
class UiUtils {
    companion object {
        fun setImageResource(
            imageView: ImageView,
            @DrawableRes drawableResId: Int,
            colorResId: Int
        ) {
            imageView.setSvgImage(drawableResId, colorResId)
        }
    }
}
inline fun Fragment.initTitleBar(topBar: QMUITopBarLayout,
                                                      @Nullable @StringRes centerTextRes: Int? = null,
                                                      @Nullable @ColorRes centerTextColor: Int? = null,
                                                      @Nullable centerText:String?=null,
                                                      @DrawableRes leftImageRes: Int? = R.mipmap.btn_back_normal,
                                                      @Nullable @StringRes rightTextRes: Int? = null,
                                                      @ColorRes rightTextColor: Int = R.color.cc2,
                                                      @ColorRes bgColorRes: Int = R.color.white,
                                                      isStatusFontDark: Boolean = true,
                                                      useMultipleStatusLayout: Boolean = false,
                                                      crossinline rightTextClick: (view: View) -> Unit = {}):QMUITopBarWrapper{
    return requireActivity().initTitleBar(topBar, centerTextRes, centerTextColor,centerText, leftImageRes,rightTextRes, rightTextColor, bgColorRes, isStatusFontDark, useMultipleStatusLayout, rightTextClick)
}

/**
 * 初始化标题栏
 * 为了减少这个方法的复杂度，该方法中有的是基础的标题栏设置，如果想要做一些其他的定制，请使用[QMUITopBarWrapper]，
 * 例如[QMUITopBarWrapper.replaceRightText2Image] 方法，可以把右边的文字替换为图片
 *
 * @param topBar 顶部标题栏控件
 * @param centerTextRes 可空，标题栏中间的文字
 * @param centerTextColor 可空，标题栏中间文字的颜色，如果不填的话，就是系统默认的颜色
 * @param leftImageRes 标题栏坐标显示的图片
 * @param rightTextRes 可空，标题栏最右侧的文字
 * @param rightTextColor 标题栏最右侧文字颜色
 * @param bgColorRes 顶部标题栏背景色，会同步修改系统状态栏颜色
 * @param isStatusFontDark 系统状态栏字体和图标的颜色是否为黑色，需要根据[bgColorRes]来设置，默认是黑色
 * @param useMultipleStatusLayout 是否使用了 [MultipleStatusLayout]布局，使用了这个布局的话，需要特殊处理
 * @param rightTextClick 可空,标题栏最右侧文字的点击响应事件
 *
 * @return 返回[QMUITopBarWrapper]，用来对标题栏做一些定制化
 */
inline fun Activity.initTitleBar(
    topBar: QMUITopBarLayout,
    @Nullable @StringRes centerTextRes: Int? = null,
    @Nullable @ColorRes centerTextColor: Int? = null,
    @Nullable centerText:String?=null,
    @DrawableRes leftImageRes: Int? = R.mipmap.btn_back_normal,
    @Nullable @StringRes rightTextRes: Int? = null,
    @ColorRes rightTextColor: Int = R.color.cc4,
    @ColorRes bgColorRes: Int = R.color.white,
    isStatusFontDark: Boolean = true,
    useMultipleStatusLayout: Boolean = false,
    crossinline rightTextClick: (view: View) -> Unit = {}
): QMUITopBarWrapper {

    //设置标题栏中间的文字
    val centerTextView = TextView(this)
    centerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.toFloat())
    centerTextView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)//加粗
    if (centerTextRes != null) {
        centerTextView.text = getString(centerTextRes)
        centerTextView.visibility = View.VISIBLE
    } else {
        if (centerText!=null){
            centerTextView.text=centerText
        }else {
            centerTextView.text = ""
            centerTextView.visibility = View.GONE
        }
    }
    //如果文字太多的话，就隐藏一部分中间的字
    centerTextView.maxWidth = getScreenWidth() / 2
    centerTextView.ellipsize = TextUtils.TruncateAt.END
    centerTextView.maxLines = 1

    if (centerTextColor != null) {
        centerTextView.setTextColor(ContextCompat.getColor(this, centerTextColor))
    }
    topBar.setCenterView(centerTextView)

    //设置标题栏左边的返回图标
    val leftImageView = QMUIAlphaImageButton(this)
    val params = RelativeLayout.LayoutParams(dp2px(40), dp2px(40))
    params.addRule(RelativeLayout.CENTER_VERTICAL)
    leftImageView.layoutParams = params
    if (leftImageRes!=null) {
        leftImageView.setImageResource(leftImageRes)
        leftImageView.setOnClickListener {
            onBackPressed()
        }
    }

    //左右的padding都设置，增加点击事件能够响应的范围
    leftImageView.setPadding(dp2px(8), dp2px(8), dp2px(8), dp2px(8))
    topBar.addLeftView(leftImageView, R.id.left_back_icon)


    //设置标题栏右边的文字
    val rightText = QMUIAlphaTextView(this)
    rightText.setTextColor(ContextCompat.getColor(this, rightTextColor))
    if (rightTextRes != null) {
        rightText.text = getString(rightTextRes)
        rightText.visibility = View.VISIBLE
    } else {
        rightText.text = ""
        rightText.visibility = View.GONE
    }

    //左右的padding都设置，增加点击事件能够响应的范围
    rightText.setPadding(
        QMUIDisplayHelper.dp2px(this, 20),
        0,
        QMUIDisplayHelper.dp2px(this, 15),
        0
    )
    val layoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.MATCH_PARENT
    )
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
    rightText.gravity = Gravity.CENTER_VERTICAL
    rightText.setOnClickListener {
        rightTextClick.invoke(it)
    }
    topBar.addRightView(rightText, R.id.top_bar_right_text, layoutParams)

    //设置标题栏和状态栏的颜色
    try {
        topBar.setBackgroundColor(ContextCompat.getColor(this, bgColorRes))
        QMUIStatusBarHelper.translucent(this, ContextCompat.getColor(this, bgColorRes))
    } catch (e: Exception) {
        e.printStackTrace()
    }

    if (useMultipleStatusLayout) {
        //这是固定的步骤，否则标题栏会显示不完整
        topBar.addStatusBarTopPadding()
        QMUIStatusBarHelper.translucent(this)
    }

    if (isStatusFontDark) {
        //设置状态栏黑色字体图标
        QMUIStatusBarHelper.setStatusBarLightMode(this)
    } else {
        //设置状态栏白色字体图标
        QMUIStatusBarHelper.setStatusBarDarkMode(this)
    }

    return QMUITopBarWrapper(
        leftImageView,
        centerTextView,
        rightText,
        topBar
    )
}

/**
 * 使用QMUIDisplayHelper
 * 获取屏幕宽度
 */
fun Context.getScreenWidth(): Int {
    return QMUIDisplayHelper.getScreenWidth(this)
}

/**
 * 使用QMUIDisplayHelper
 * 获取屏幕高度
 */
fun Context.getScreenHeight(): Int {
    return QMUIDisplayHelper.getScreenHeight(this)
}

/**
 * 使用QMUIDisplayHelper
 * 获取状态栏的高度
 */
fun Context.getStatusBarHeight(): Int {
    return QMUIStatusBarHelper.getStatusbarHeight(this)
}

/**
 * 计算还没有显示的view的宽高
 */
fun View.unDisplayViewSize(): IntArray {
    val size = IntArray(2)
    val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    this.measure(width, height)
    size[0] = this.measuredWidth
    size[1] = this.measuredHeight
    return size
}

/**
 * 给视图顶部的view留足状态栏的高度
 */
fun View.addStatusBarTopMargin() {
    val params = layoutParams as? MarginLayoutParams
    params?.let {
        it.topMargin = context.getStatusBarHeight()
        layoutParams = it
    }
}

/**
 * 给视图顶部的view留足状态栏的高度
 */
fun View.addStatusBarTopPadding() {
    setPadding(paddingStart, paddingTop + context.getStatusBarHeight(), paddingEnd, paddingBottom)
}

/**
 * 使用QMUIDisplayHelper
 * 把dp转换成px值
 */
fun Context.dp2px(dp: Int): Int {
    return QMUIDisplayHelper.dp2px(this, dp)
}

/**
 * 使用QMUIDisplayHelper
 * 把sp转换为px值
 */
fun Context.sp2px(sp: Int): Int = QMUIDisplayHelper.sp2px(this, sp)

/**
 * 使用QMUIDisplayHelper
 * 把dp转换成px值
 */
fun Context.px2dp(dp: Int): Int {
    return QMUIDisplayHelper.px2dp(this, dp)
}


/**
 * 加载图片
 */
fun ImageView.load(
    path: String?,
) {
    Glide.with(this).load(path).into(this)
}

/**
 * 加载图片
 */
fun ImageView.load(
    resId: Int?,
) {
    Glide.with(this).load(resId).into(this)
}

/**
 * 加载圆角  4个角圆角半径一致 半径单位dp
 */
fun ImageView.loadRound(
    path: String?,
    cornerRadiusInDp: Int
) {
    Glide.with(this).load(path).apply(RequestOptions.bitmapTransform(RoundedCorners(QMUIDisplayHelper.dpToPx(cornerRadiusInDp)))).into(this)
}

fun ImageView.setSvgImage(@DrawableRes drawableResId: Int, colorResId: Int = R.color.c1) {
    try {
        context?.apply {
            setImageDrawable(drawableResId.toDrawable(this, colorResId))
        }
    } catch (e: Exception) {
    }
}

fun Int.dp2Px() = QMUIDisplayHelper.dpToPx(this)
fun Float.dp2Px()=QMUIDisplayHelper.dpToPx(this.toInt())
fun Context.getDimen(@DimenRes resId:Int) = this.resources.getDimension(resId).toInt()
fun Fragment.getDimen(@DimenRes resId:Int) = this.resources.getDimension(resId).toInt()


/**
 * @param root         最外层布局，需要调整的布局
 * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
 */
fun controlKeyboardLayout(root: View, scrollToView: View) {
    root.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        // 获取root在窗体的可视区域
        root.getWindowVisibleDisplayFrame(rect)
        // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
        val rootInvisibleHeight = root.rootView.height - rect.bottom
        // 若不可视区域高度大于100，则键盘显示 华为手机会有100多的默认区域高度 所以需要写大一点
        if (rootInvisibleHeight > 200) {
            val location = IntArray(2)
            // 获取scrollToView在窗体的坐标
            scrollToView.getLocationInWindow(location)
            // 计算root滚动高度，使scrollToView在可见区域
            val srollHeight: Int = location[1] + scrollToView.height + dp2px(root.context,10f) - rect.bottom
            //直播结束的弹窗会把这个值变为负数导致软件盘下去 所以加一个判断阻止
            if (srollHeight >= 0) {
                //布局抬高
                root.scrollTo(0, srollHeight + 125)
            }
        } else {
            // 键盘隐藏
            //布局恢复
            root.scrollTo(0, 0)
        }
    }
}

/**
 * 更新QMUITipDialog的消息  由于库本身未提供API故使用此方法   若库更新导致此方法不可用，请及时修正
 */
fun QMUITipDialog.updateMsg(msg:String){
    try {
        (window?.decorView as ViewGroup?)?.getAllChildren()?.forEach {
            if (it is QMUISpanTouchFixTextView){
                it.text=msg
            }
        }
    }catch (e:Exception){
        e.printStackTrace()
    }
}
fun getTextHeight(textPaint: Paint): Float {
    return -textPaint.ascent() - textPaint.descent()
}
/**
 * 遍历ViewGroup获得所有子view
 */
fun ViewGroup.getAllChildren():List<View>{
    val result=ArrayList<View>()
    result.addAll(children)
    for (child in children) {
        if (child is ViewGroup){
            result.addAll(child.getAllChildren())
        }
    }
    return result
}