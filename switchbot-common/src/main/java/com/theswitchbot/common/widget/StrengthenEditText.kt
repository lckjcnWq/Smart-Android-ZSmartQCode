package com.theswitchbot.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.qmuiteam.qmui.layout.QMUIRelativeLayout
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.theswitchbot.common.R


/**
 * 加强版文字输入框 支持左侧图标 右侧图标 右侧图表加文字
 */
class StrengthenEditText : QMUIRelativeLayout {
    private val defImageSize=18
    private val defTextSize=10
    private val defRightTextSize=10
    private val defRightImageSize=18

    private lateinit var viewInputEdittext:EditText

    constructor(context: Context) : super(context) {
        initView(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView(attrs, defStyle)
    }


    private fun initView(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StrengthenEditText, defStyle, 0)
        val leftImageWidth=a.getDimension(R.styleable.StrengthenEditText_leftImageWidth,QMUIDisplayHelper.dpToPx(defImageSize).toFloat())
        val leftImageHeight=a.getDimension(R.styleable.StrengthenEditText_leftImageHeight,QMUIDisplayHelper.dpToPx(defImageSize).toFloat())
        val rightImageWidth=a.getDimension(R.styleable.StrengthenEditText_rightImageWidth,QMUIDisplayHelper.dpToPx(defRightImageSize).toFloat())
        val rightImageHeight=a.getDimension(R.styleable.StrengthenEditText_rightImageHeight,QMUIDisplayHelper.dpToPx(defRightImageSize).toFloat())
        val edittextSize=a.getDimension(R.styleable.StrengthenEditText_edittextSize,QMUIDisplayHelper.sp2px(context,defTextSize).toFloat())
        val rightTextSize=a.getDimension(R.styleable.StrengthenEditText_rightTextSize,QMUIDisplayHelper.sp2px(context,defRightTextSize).toFloat())
        val textHint=a.getString(R.styleable.StrengthenEditText_textHint)
        val textHintColor=a.getColor(R.styleable.StrengthenEditText_textHintColor,resources.getColor(R.color.white))
        val textColor=a.getColor(R.styleable.StrengthenEditText_textColor,resources.getColor(R.color.white))
        val rightTextColor=a.getColor(R.styleable.StrengthenEditText_rightTextColor,resources.getColor(R.color.white))
        val rightText=a.getString(R.styleable.StrengthenEditText_rightText)
        val leftImage=a.getDrawable(R.styleable.StrengthenEditText_leftImage)
        val rightImage=a.getDrawable(R.styleable.StrengthenEditText_rightImage)
        val text=a.getString(R.styleable.StrengthenEditText_text)
        val inputType=a.getInt(R.styleable.StrengthenEditText_etInputType,EditorInfo.TYPE_CLASS_TEXT)
        a.recycle()
        val rootView= inflate(context,R.layout.view_strengthen_edit_text,this)
        val viewInputLeftImage=rootView.findViewById<ImageView>(R.id.viewInputLeftImage)
        viewInputEdittext=rootView.findViewById<EditText>(R.id.viewInputEdittext)
        val viewInputRightText=rootView.findViewById<TextView>(R.id.viewInputRightText)
        val viewInputRightImage=rootView.findViewById<ImageView>(R.id.viewInputRightImage)
        viewInputLeftImage.visibility=if(leftImage!=null){ VISIBLE}else{ GONE}
        viewInputRightImage.visibility=if(rightImage!=null){ VISIBLE}else{ INVISIBLE}
        viewInputRightText.visibility=if(rightText!=null){ VISIBLE}else{ GONE}
        adjustSize(viewInputLeftImage,leftImageWidth.toInt(),leftImageHeight.toInt())
        adjustSize(viewInputEdittext,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        adjustSize(viewInputRightText,LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT)
        adjustSize(viewInputRightImage,rightImageWidth.toInt(),rightImageHeight.toInt())

        viewInputLeftImage.setImageDrawable(leftImage)

        viewInputEdittext.inputType=inputType
        viewInputEdittext.textSize=edittextSize
        viewInputEdittext.hint=textHint
        viewInputEdittext.setTextColor(textColor)
        viewInputEdittext.setHintTextColor(textHintColor)
        viewInputEdittext.setText(text)

        viewInputRightText.textSize=rightTextSize
        viewInputRightText.setTextColor(rightTextColor)
        viewInputRightText.text=rightText
        viewInputRightImage.setImageDrawable(rightImage)

    }

    fun getText():String=viewInputEdittext.text.toString()

    private fun adjustSize(view:View,width:Int,height:Int){
        val layoutParams=view.layoutParams
        layoutParams.width= width
        layoutParams.height= height
        view.layoutParams=layoutParams
    }

    fun setRightImageClickListener(listener:OnClickListener){
        findViewById<ImageView>(R.id.viewInputRightImage).setOnClickListener(listener)
    }

    fun getRealEditText()=viewInputEdittext

    fun setRightImage(resId:Int){
        findViewById<ImageView>(R.id.viewInputRightImage).setImageResource(resId)

    }

    fun setText(charSequence: CharSequence){
        viewInputEdittext.setText(charSequence)
    }

}