package com.theswitchbot.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.qmuiteam.qmui.layout.QMUIRelativeLayout
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.theswitchbot.common.R
import com.theswitchbot.common.util.dp2Px

/**
 * 设置页Item复用组合控件
 *
 * 控件大致布局：
 * leftIcon(ImageView) mainTv(TextView) rightIcon(ImageView) rightTv(TextView) rightSw(Switch) endIcon(ImageView)
 * divider(View)
 *
 *
 * xml中使用，提供基本属性配置
 * 每个控件可配置属性由 控件名+对应属性 组成
 * 例如：
 * app:leftIconVisibility
 * app:leftIconSrc
 * app:leftIconScaleType
 * 全部可配置属性参考R.styleable.ItemViewEvo
 * 如需额外的配置，可获取对应控件在代码中控制
 *
 * 目前switch默认开启状态的配置在style/ItemEvoSwitch中，
 * 颜射为colorAccent，确认统一风格后再调整。不要自行修改.
 *
 * 需要更多自定义配置时，可通过获取对应控件，自行在代码中配置
 * 或者将ItemViewEvo作为RelativeLayout使用
 *
 */
class ItemViewEvo : QMUIRelativeLayout {

    //设置为具体数值时需要转换为px
    private val defaultIconSize = ViewGroup.LayoutParams.WRAP_CONTENT
    private val defaultMargin = 16f
    private val defaultTextSize = 16

    private val typeRelativeLayout = 0
    private val typeLinearLayout = 1

    private val topDivider = 0
    private val bottomDivider = 1

    private val textStyleNormal = 0
    private val textStyleBold = 1
    private val textStyleItalic = 2

    private var switchListener:CompoundButton.OnCheckedChangeListener ?= null

    constructor(context: Context) : super(context) {
        initView(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context,
        attrs,
        defStyle) {
        initView(attrs, defStyle)
    }

    private fun initView(attrs: AttributeSet?, defStyle: Int) {
        View.inflate(context, R.layout.view_item_view_evo, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemViewEvo, defStyle, 0)
        initLeftIcon(typedArray)
        initRightIcon(typedArray)
        initEndIcon(typedArray)
        initMainTv(typedArray)
        initRightTv(typedArray)
        initSwitch(typedArray)
        initDivider(typedArray)
        typedArray.recycle()
    }

    private fun initLeftIcon(typedArray: TypedArray) {
        val icon = findViewById<AppCompatImageView>(R.id.leftIcon)
        val iconSrc = typedArray.getDrawable(R.styleable.ItemViewEvo_leftIconSrc)
        val iconVisibility =
            typedArray.getInt(R.styleable.ItemViewEvo_leftIconVisibility, View.VISIBLE)
        val iconWith =
            typedArray.getLayoutDimension(R.styleable.ItemViewEvo_leftIconWith, defaultIconSize)
        val iconHigh =
            typedArray.getLayoutDimension(R.styleable.ItemViewEvo_leftIconHigh, defaultIconSize)
        val iconMarginStart =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_leftIconMarginStart,
                defaultMargin.dp2Px())
        val iconMarginEnd =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_leftIconMarginEnd, 0)
        val iconScaleType = typedArray.getInt(R.styleable.ItemViewEvo_leftIconScaleType,
            ImageView.ScaleType.FIT_CENTER.ordinal)
        //设置图片资源
        iconSrc?.let { icon.setImageDrawable(it) }
        icon.visibility = iconVisibility
        adjustSize(icon, width = iconWith, height = iconHigh)
        adjustMargin(icon, typeRelativeLayout, start = iconMarginStart, end = iconMarginEnd)
        icon.scaleType = ImageView.ScaleType.values()[iconScaleType]
    }

    private fun initRightIcon(typedArray: TypedArray) {
        val icon = findViewById<AppCompatImageView>(R.id.rightIcon)
        val iconSrc = typedArray.getDrawable(R.styleable.ItemViewEvo_rightIconSrc)
        val iconVisibility =
            typedArray.getInt(R.styleable.ItemViewEvo_rightIconVisibility, View.GONE)
        val iconWith =
            typedArray.getLayoutDimension(R.styleable.ItemViewEvo_rightIconWith, defaultIconSize)
        val iconHigh =
            typedArray.getLayoutDimension(R.styleable.ItemViewEvo_rightIconHigh, defaultIconSize)
        val iconMarginStart =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_rightIconMarginStart, 0)
        val iconMarginEnd =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_rightIconMarginEnd, 0)
        val iconScaleType = typedArray.getInt(R.styleable.ItemViewEvo_rightIconScaleType,
            ImageView.ScaleType.FIT_CENTER.ordinal)
        //设置图片资源
        iconSrc?.let { icon.setImageDrawable(it) }
        icon.visibility = iconVisibility
        adjustSize(icon, width = iconWith, height = iconHigh)
        adjustMargin(icon, typeLinearLayout, start = iconMarginStart, end = iconMarginEnd)
        icon.scaleType = ImageView.ScaleType.values()[iconScaleType]
    }

    private fun initEndIcon(typedArray: TypedArray) {
        val icon = findViewById<AppCompatImageView>(R.id.endIcon)
        val iconSrc = typedArray.getDrawable(R.styleable.ItemViewEvo_endIconSrc)
        val iconVisibility =
            typedArray.getInt(R.styleable.ItemViewEvo_endIconVisibility, View.VISIBLE)
        val iconWith =
            typedArray.getLayoutDimension(R.styleable.ItemViewEvo_endIconWith, defaultIconSize)
        val iconHigh =
            typedArray.getLayoutDimension(R.styleable.ItemViewEvo_endIconHigh, defaultIconSize)
        val iconMarginStart =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_endIconMarginStart, 0)
        val iconMarginEnd =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_endIconMarginEnd,
                defaultMargin.dp2Px())
        val iconScaleType = typedArray.getInt(R.styleable.ItemViewEvo_endIconScaleType,
            ImageView.ScaleType.FIT_CENTER.ordinal)
        //设置图片资源
        iconSrc?.let { icon.setImageDrawable(it) }
        icon.visibility = iconVisibility
        adjustSize(icon, width = iconWith, height = iconHigh)
        adjustMargin(icon, typeLinearLayout, start = iconMarginStart, end = iconMarginEnd)
        icon.scaleType = ImageView.ScaleType.values()[iconScaleType]
    }

    private fun initMainTv(typedArray: TypedArray) {
        val textView = findViewById<AppCompatTextView>(R.id.mainTv)
        val text = typedArray.getString(R.styleable.ItemViewEvo_mainTvText)
        val textColor = typedArray.getColor(R.styleable.ItemViewEvo_mainTvTextColor,
            ContextCompat.getColor(context, R.color.common_text_color))
        val textSize = typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_mainTvTextSize,
            QMUIDisplayHelper.sp2px(context, defaultTextSize))
        val textStyle = typedArray.getInt(R.styleable.ItemViewEvo_mainTvTextStyle, textStyleNormal)
        val marginStart =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_mainTvMarginStart,
                defaultMargin.dp2Px())
        val marginEnd = typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_mainTvMarginEnd, 0)
        val visibility = typedArray.getInt(R.styleable.ItemViewEvo_mainTvVisibility, View.VISIBLE)
        val maxLines = typedArray.getInt(R.styleable.ItemViewEvo_mainTvMaxLines,1)

        textView.text = text
        textView.maxLines = maxLines
        textView.setTextColor(textColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        adjustTextStyle(textView, textStyle)
        adjustMargin(textView, typeRelativeLayout, start = marginStart, end = marginEnd)
        textView.visibility = visibility
    }

    private fun initRightTv(typedArray: TypedArray) {
        val textView = findViewById<AppCompatTextView>(R.id.rightTv)
        val text = typedArray.getString(R.styleable.ItemViewEvo_rightTvText)
        val textColor = typedArray.getColor(R.styleable.ItemViewEvo_rightTvTextColor,
            ContextCompat.getColor(context, R.color.common_text_color))
        val textSize = typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_rightTvTextSize,
            QMUIDisplayHelper.sp2px(context, defaultTextSize))
        val textStyle = typedArray.getInt(R.styleable.ItemViewEvo_rightTvTextStyle, textStyleNormal)
        val marginStart =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_rightTvMarginStart, 0)
        val marginEnd =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_rightTvMarginEnd, 0)
        val visibility = typedArray.getInt(R.styleable.ItemViewEvo_rightTvVisibility, View.GONE)

        textView.text = text
        textView.setTextColor(textColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        adjustTextStyle(textView, textStyle)
        adjustMargin(textView, typeLinearLayout, start = marginStart, end = marginEnd)
        textView.visibility = visibility
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun initSwitch(typedArray: TypedArray) {
        val switch = findViewById<Switch>(R.id.rightSwitch)
        val width = typedArray.getLayoutDimension(R.styleable.ItemViewEvo_rightSwWith,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        val height = typedArray.getLayoutDimension(R.styleable.ItemViewEvo_rightSwHigh,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        val marginStart =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_rightSwMarginStart, 0)
        val marginEnd =
            typedArray.getDimensionPixelSize(R.styleable.ItemViewEvo_rightSwMarginEnd, 0)
        val visibility = typedArray.getInt(R.styleable.ItemViewEvo_rightSwVisibility, View.GONE)
        adjustSize(switch, width = width, height = height)
        adjustMargin(switch, typeLinearLayout, start = marginStart, end = marginEnd)
        switch.visibility = visibility
    }

    private fun initDivider(typedArray: TypedArray) {
        val divider = findViewById<View>(R.id.divider)
        val dividerHigh =
            typedArray.getLayoutDimension(R.styleable.ItemViewEvo_dividerHigh, 1f.dp2Px())
        val dividerColor = typedArray.getColor(R.styleable.ItemViewEvo_dividerColor,
            ContextCompat.getColor(context, R.color.a_black_color))
        val dividerMarginStart =
            typedArray.getDimension(R.styleable.ItemViewEvo_dividerMarginStart, 0f)
        val dividerMarginEnd = typedArray.getDimension(R.styleable.ItemViewEvo_dividerMarginEnd, 0f)
        val dividerVisibility =
            typedArray.getInt(R.styleable.ItemViewEvo_dividerVisibility, View.VISIBLE)
        val dividerPosition =
            typedArray.getInt(R.styleable.ItemViewEvo_dividerPosition, bottomDivider)
        adjustSize(divider, height = dividerHigh)
        divider.setBackgroundColor(dividerColor)
        adjustMargin(divider,
            typeRelativeLayout,
            start = dividerMarginStart.toInt(),
            end = dividerMarginEnd.toInt())
        divider.visibility = dividerVisibility
        val layoutParams = divider.layoutParams as LayoutParams
        if (dividerPosition == topDivider) {
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        } else {
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        }
    }

    private fun adjustSize(view: View, width: Int? = null, height: Int? = null) {
        val layoutParams = view.layoutParams
        width?.let { layoutParams.width = it }
        height?.let { layoutParams.height = it }
        view.layoutParams = layoutParams
    }

    private fun adjustMargin(view: View,
                             layoutType: Int,
                             start: Int? = null,
                             top: Int? = null,
                             end: Int? = null,
                             bottom: Int? = null) {
        val params = view.layoutParams
        val layoutParams = if (layoutType == typeLinearLayout) {
            params as LinearLayout.LayoutParams
        } else {
            params as LayoutParams
        }
        start?.let { layoutParams.marginStart = it }
        end?.let { layoutParams.marginEnd = it }
        top?.let { layoutParams.topMargin = it }
        bottom?.let { layoutParams.topMargin = it }
        view.layoutParams = layoutParams
    }

    private fun adjustTextStyle(textView: TextView, type: Int) {
        when (type) {
            textStyleNormal -> textView.setTypeface(null, Typeface.NORMAL)
            textStyleBold -> textView.setTypeface(null, Typeface.BOLD)
            textStyleItalic -> textView.setTypeface(null, Typeface.ITALIC)
        }
    }

    /**
     * 获取mainTv左侧ImageView
     */
    fun getLeftIcon(): AppCompatImageView {
        return findViewById(R.id.leftIcon)
    }

    /**
     * 获取mainTv
     */
    fun getMainTextView(): AppCompatTextView {
        return findViewById(R.id.mainTv)
    }

    /**
     * 获取mainTv右侧ImageView
     */
    fun getRightIcon(): AppCompatImageView {
        return findViewById(R.id.rightIcon)
    }

    /**
     * 获取rightTv
     */
    fun getRightTextView(): AppCompatTextView {
        return findViewById(R.id.rightTv)
    }

    /**
     * 获取switch
     */
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    fun getRightSwitchButton(): Switch {
        val switch = findViewById<Switch>(R.id.rightSwitch)
        return switch
    }

    /**
     * 获取尾部ImageView
     */
    fun getEndIcon(): AppCompatImageView {
        return findViewById(R.id.endIcon)
    }

    /**
     * 设置switch开关监听
     */
    fun setOnCheckedChangeListener(checkedChangeListener: CompoundButton.OnCheckedChangeListener?) {
        switchListener = checkedChangeListener
        getRightSwitchButton().setOnCheckedChangeListener(switchListener)
    }

    /**
     * 设置按钮状态时不触发监听事件
     */
    fun setRightSwitchCheckedNoEvent(isChecked:Boolean) {
        getRightSwitchButton().run {
            setOnCheckedChangeListener(null)
            setChecked(isChecked)
            setOnCheckedChangeListener(switchListener)
        }
    }

    /**
     * 设置ItemViewEvo能否交互
     * @param isActive ItemViewEvo及其子View能否交互
     * @param alphaDisable 不可交互时的透明度 默认值0.3f
     * @param alphaEnable 可交互时的透明度 默认值1.0f
     */
    fun setActiveState(isActive: Boolean, alphaDisable: Float = 0.3f, alphaEnable: Float = 1.0f) {
        isEnabled = isActive
        getRightSwitchButton().isEnabled = isActive
        children.forEach {
            it.alpha = if (isActive) {
                alphaEnable
            } else {
                alphaDisable
            }
            it.isEnabled = isActive
        }
    }
}