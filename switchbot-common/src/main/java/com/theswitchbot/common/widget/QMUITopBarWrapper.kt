package com.theswitchbot.common.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.view.marginRight
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.theswitchbot.common.R
import com.theswitchbot.common.util.dp2px
import com.theswitchbot.common.util.load
import com.theswitchbot.common.util.loadRound

/**
 * 自定义标题栏
 */
class QMUITopBarWrapper(
    val leftImageView: ImageView,
    val centerTextView: TextView,
    val rightTextView: TextView,
    val topBar: QMUITopBarLayout
) {

    var rightImageView: ImageView? = null

    /**
     * 设置topBar的显示和隐藏
     */
    fun setTopBarVisibility(vis: Int) {
        try {
            topBar.visibility = vis
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 重置标题栏左边的图片
     */
    fun resetLeftImageOnClickListener(onClickListener: View.OnClickListener) {
        try {
            leftImageView.setOnClickListener(onClickListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 重置标题栏左边的图片
     */
    fun resetLeftDrawable(drawable: Drawable?) {
        try {
            leftImageView.apply {
                setImageDrawable(drawable)
                visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 重置标题栏左边的图片
     */
    fun resetLeftImage(@DrawableRes imgRes: Int) {
        try {
            leftImageView.apply {
                setImageResource(imgRes)
                visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 重置标题栏右边的图片
     */
    fun resetRightImage(@DrawableRes imgRes: Int) {
        try {
            rightImageView?.apply {
                setImageResource(imgRes)
                visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置标题栏左边图标的显示或者隐藏
     */
    fun setLeftImageVisibility(vis: Int) {
        try {
            leftImageView.apply {
                visibility = vis
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 重置标题栏中间的文字
     */
    fun resetCenterText(@StringRes stringRes: Int) {
        try {
            centerTextView.apply {
                setText(stringRes)
                visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 重置标题栏中间的文字
     */
    fun resetCenterText(string: String?) {
        try {
            centerTextView.apply {
                text = string
                visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置标题栏中间文字的显示或者隐藏
     */
    fun setCenterTextVisibility(vis: Int) {
        try {
            centerTextView.apply {
                visibility = vis
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置标题栏的透明度
     */
    fun setAlpha(alpha: Int) {
        setAlpha(alpha.toFloat())
    }

    /**
     * 设置标题栏的透明度
     */
    fun setAlpha(alpha: Float) {
        topBar.alpha = alpha
    }

    fun setBackgroundResource(@DrawableRes resId: Int) {
        topBar.setBackgroundResource(resId)
    }

    /**
     * 根据id设置view是否可显示
     */
    fun showChildViewById(@IdRes id: Int, show: Boolean) {

        try {
            if (show) {
                topBar.findViewById<View>(id).visibility = View.VISIBLE
            } else {
                topBar.findViewById<View>(id).visibility = View.GONE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 根据id找到子view
     */
    fun findChildViewById(@IdRes id: Int): View? {
        try {
            return topBar.findViewById(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 根据id 重新加载图片
     */
    fun resetImageView(@IdRes id: Int, path: String) {
        try {
            topBar.findViewById<ImageView>(id).load(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 根据id 重新加载图片
     */
    fun resetImageViewRound(@IdRes id: Int, path: String, radius: Int) {
        try {
            topBar.findViewById<ImageView>(id).loadRound(path, radius)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 根据id 重新加载图片
     */
    fun resetImageView(@IdRes id: Int, @DrawableRes drawableRes: Int) {
        try {
            topBar.findViewById<ImageView>(id).setImageResource(drawableRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 重置标题栏右边的文字
     */
    fun resetRightText(@StringRes stringRes: Int) {
        try {
            rightTextView.apply {
                setText(stringRes)
                setTextColor(resources.getColor(R.color.cc4))
                visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置标题栏右边文字的显示或者隐藏
     */
    fun setRightTextVisibility(vis: Int) {
        try {
            rightTextView.apply {
                visibility = vis
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun resetRightTextEnable(enable: Boolean) {
        try {
            rightTextView.isEnabled = enable
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearRightView() {
        try {
            topBar.removeAllRightViews()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置标题栏右边文字的颜色
     */
    fun setRightTextColor(context: Context, @ColorRes color: Int) {
        try {
            rightTextView.setTextColor(ContextCompat.getColor(context, color))
            rightTextView.textSize = 16f
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置标题栏右边文字的颜色
     */
    fun resetRightTextColorState(context: Context, @ColorRes colorRes: Int) {
        try {
            val colorStateList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.resources.getColorStateList(colorRes, context.theme)
            } else {
                context.resources.getColorStateList(colorRes)
            }

            rightTextView.setTextColor(colorStateList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置标题栏右边文字的颜色
     */
    fun setRightTextEnabled(boolean: Boolean) {
        rightTextView.isEnabled = boolean
    }

    /**
     * 改变标题栏颜色
     */
    fun translucentTopBar(activity: Activity, @ColorRes bgColorRes: Int) {
        topBar.setBackgroundColor(ContextCompat.getColor(activity, bgColorRes))
        QMUIStatusBarHelper.translucent(activity, ContextCompat.getColor(activity, bgColorRes))
    }

    /**
     * 状态栏字体深色或亮色
     *
     * @param isDarkFont true 深色
     * @return the immersion bar
     */
    fun statusBarDarkFont(activity: Activity, isDarkFont: Boolean) {
        if (isDarkFont) {
            //设置状态栏黑色字体图标
            QMUIStatusBarHelper.setStatusBarLightMode(activity)
        } else {
            //设置状态栏白色字体图标
            QMUIStatusBarHelper.setStatusBarDarkMode(activity)
        }
    }

    /**
     * 标题栏左边添加视图
     */
    fun addLeftImageView(context: Context, imageValue: QMUITopBarNewImageValue) {
        val imageView = createImageView(
            context,
            imageValue = imageValue
        )
        topBar.addLeftView(imageView, imageValue.viewId)
    }

    /**
     * 标题栏左边添加视图
     */
    fun addLeftView(@IdRes id: Int, view: View) {
        topBar.addLeftView(view, id)
    }

    /**
     * 标题栏右边添加视图
     */
    fun addRightTextView(context: Context, textValue: QMUITopBarNewTextValue) {
        try {
            val textView = createTextView(context, textValue = textValue)
            topBar.addRightView(textView, textValue.viewId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 标题栏右边添加视图
     */
    fun addRightImageView(context: Context, imageValue: QMUITopBarNewImageValue) {
        try {
            val imageView = createImageView(context, imageValue = imageValue)
            topBar.addRightView(imageView, imageValue.viewId)
            val layoutParams=(imageView.layoutParams as RelativeLayout.LayoutParams)
            layoutParams.marginEnd=QMUIDisplayHelper.dpToPx(20)
            imageView.layoutParams=layoutParams
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 标题栏左边添加视图
     */
    fun addRightView(@IdRes id: Int, view: View) {
        topBar.addRightView(view, id)
    }

    /**
     * 把标题栏左边的图标替换成文字
     *
     * @param context context
     * @param textRes 文字
     * @param clickListener 点击事件
     */
    fun replaceLeftImage2Text(
        context: Context,
        @StringRes textRes: Int,
        @Nullable clickListener: View.OnClickListener? = null
    ) {

        val textValue =
            QMUITopBarNewTextValue(
                R.id.top_bar_left_text,
                textRes = textRes,
                textSize = 16,
                clickListener = clickListener
            )

        replaceLeftImage2Text(context, listOf(textValue))
    }

    /**
     * 把标题栏左边的图标替换成文字
     *
     * @param context context
     * @param textValueList 需要设置的文字属性集合
     */
    fun replaceLeftImage2Text(context: Context, textValueList: List<QMUITopBarNewTextValue>) {

        topBar.removeAllLeftViews()

        for (textValue in textValueList) {
            try {
                val textView = createTextView(context, textValue = textValue)
                topBar.addLeftView(textView, textValue.viewId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 替换centerView
     */
    fun replaceCenterView(view: View) {
        topBar.removeCenterViewAndTitleView()
        topBar.setCenterView(view)
    }

    /**
     * 把标题栏右边的图标替换成文字
     *
     * @param context context
     * @param textRes 文字
     * @param clickListener 点击事件
     */
    fun replaceRightImage2Text(
            context: Context,
            @StringRes textRes: Int,
            @Nullable clickListener: View.OnClickListener? = null
    ) {
        val textValue =
                QMUITopBarNewTextValue(
                        R.id.top_bar_right_text,
                        textRes = textRes,
                        clickListener = clickListener
                )
        replaceRightImage2Text(context, listOf(textValue))
    }

    /**
     * 把标题栏右边的图标替换成文字
     *
     * @param context context
     * @param textValueList 需要设置的文字属性集合
     */
    fun replaceRightImage2Text(context: Context, textValueList: List<QMUITopBarNewTextValue>) {
        topBar.removeAllRightViews()
        for (textValue in textValueList) {
            try {
                val textView = createTextView(context, textValue = textValue)
                topBar.addRightView(textView, textValue.viewId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     *  把标题栏右边的文字替换为图标
     *
     *  @param context context
     *  @param rightImageRes 图片id
     *  @param onClickListener 点击事件
     */
    fun replaceRightText2Image(
        context: Context, @DrawableRes rightImageRes: Int,
        @Nullable onClickListener: View.OnClickListener? = null
    ) {
        val imageValue = QMUITopBarNewImageValue(
            viewId = R.id.top_bar_right_icon,
            drawableRes = rightImageRes,
            clickListener = onClickListener
        )
        replaceRightView2Images(context, listOf(imageValue))
    }

    /**
     *  把标题栏右边的文字替换为图标
     *
     *  @param context context
     *  @param rightDrawable 图片
     *  @param onClickListener 点击事件
     */
    fun replaceRightText2Image(
        context: Context, rightDrawable: Drawable?,
        @Nullable onClickListener: View.OnClickListener? = null
    ) {
        val imageValue = QMUITopBarNewImageValue(
            viewId = R.id.top_bar_right_icon,
            drawable = rightDrawable,
            clickListener = onClickListener
        )
        replaceRightView2Images(context, listOf(imageValue))
    }


    /**
     * 把标题栏左边的文字替换为图标
     * 按照集合的顺序，从左往右添加图标
     *
     * @param context context
     * @param newImageValueList 需要添加的ImageView的各种属性设置，具体查看[QMUITopBarNewImageValue]
     */
    fun replaceLeftView2Images(
        context: Context,
        newImageValueList: List<QMUITopBarNewImageValue>
    ) {

        topBar.removeAllLeftViews()

        for (imageValue in newImageValueList) {
            try {
                val imageView = createImageView(
                    context,
                    imageValue = imageValue
                )
                topBar.addLeftView(imageView, imageValue.viewId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 把标题栏右边的view替换为图标
     * 按照集合的顺序，从右往左添加图标
     *
     * @param context context
     * @param newImageValueList 需要添加的ImageView的各种属性设置，具体查看[QMUITopBarNewImageValue]
     */
    fun replaceRightView2Images(
        context: Context,
        newImageValueList: List<QMUITopBarNewImageValue>
    ) {

        topBar.removeAllRightViews()

        for (imageValue in newImageValueList) {
            try {
                val imageView = createImageView(
                    context,
                    imageValue = imageValue
                )
                topBar.addRightView(imageView, imageValue.viewId)
                rightImageView = imageView
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 生成一个TextView
     *
     * @param context context
     * @param textValue 自定义的属性
     */
    private fun createTextView(context: Context, textValue: QMUITopBarNewTextValue): TextView {
        val padding = context.dp2px(16)

        return with(QMUIAlphaTextView(context)) {
            val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.CENTER_VERTICAL)
            layoutParams = params
            setOnClickListener(textValue.clickListener)

            if (textValue.text.isNullOrEmpty()) {
                try {
                    setText(textValue.textRes)
                } catch (e: Exception) {
                }
            } else {
                text = textValue.text
            }

            try {
                setTextColor(ContextCompat.getColor(context, textValue.textColorRes))
            } catch (e: Exception) {
            }

            setTextSize(TypedValue.COMPLEX_UNIT_SP, textValue.textSize.toFloat())
            //左右都设置，增加点击事件能够响应的范围
            setPadding(padding, 0, padding, 0)
            this
        }
    }

    /**
     * 生成一个ImageView
     * @param context context
     * @param  imageValue ImageView属性
     */
    fun createImageView(
        context: Context,
        imageValue: QMUITopBarNewImageValue
    ): ImageView {

        val viewWidth = context.dp2px(imageValue.viewWidth)
        val viewHeight = context.dp2px(imageValue.viewHeight)
        val paddingStart = context.dp2px(imageValue.paddingStart)
        val paddingTop = context.dp2px(imageValue.paddingTop)
        val paddingEnd = context.dp2px(imageValue.paddingEnd)
        val paddingBottom = context.dp2px(imageValue.paddingBottom)

        return with(QMUIAlphaImageButton(context)) {
            val params = RelativeLayout.LayoutParams(viewWidth, viewHeight)
            params.addRule(RelativeLayout.CENTER_VERTICAL)
            params.marginStart = context.dp2px(imageValue.marginStart)
            layoutParams = params
            scaleType = imageValue.scaleType

            if (imageValue.drawableRes > 0) {
                setImageResource(imageValue.drawableRes)
            }

            if (imageValue.drawable != null) {
                setImageDrawable(imageValue.drawable)
            }

            setOnClickListener(imageValue.clickListener)
            //左右都设置，增加点击事件能够响应的范围
            setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
            this
        }
    }
}

/**
 * 标题栏添加ImageView时设置的属性
 * @param viewId 所添加的ImageView的id，后期如果需要隐藏/显示这个ImageView就通过id来找到对应的ImageView
 * @param drawableRes ImageView显示的图片id
 * @param viewWidth ImageView宽度
 * @param viewHeight ImageView高度
 * @param marginStart marginStart
 * @param scaleType scaleType
 * @param clickListener 点击事件响应，可空
 */
data class QMUITopBarNewImageValue(
    @IdRes val viewId: Int,
    @DrawableRes val drawableRes: Int = Int.MIN_VALUE,
    val drawable: Drawable? = null,
    val viewWidth: Int = 30,
    val viewHeight: Int = 30,
    val marginStart: Int = 0,
    val paddingStart: Int = 16,
    val paddingEnd: Int = 16,
    val paddingTop: Int = 0,
    val paddingBottom: Int = 0,
    val scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER,
    @Nullable val clickListener: View.OnClickListener? = null
)

/**
 * 标题栏添加TextView时设置的属性
 *
 * @param viewId 所添加的TextView的id,后期如果需要隐藏/显示这个TextView就通过id来找到对应的TextView
 * @param textRes 显示的字符串
 * @param text 显示的字符串（优先）
 * @param textSize 文字大小，默认14
 * @param clickListener 点击事件响应，可空
 */
data class QMUITopBarNewTextValue(
    @IdRes val viewId: Int,
    @StringRes val textRes: Int,
    val text: String? = null,
    val textSize: Int = 14,
    @ColorRes val textColorRes: Int = R.color.black,
    @Nullable val clickListener: View.OnClickListener? = null
)