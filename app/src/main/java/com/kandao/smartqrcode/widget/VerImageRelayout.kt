package com.kandao.smartqrcode.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.kandao.smartqrcode.R

class VerImageRelayout : RelativeLayout {
  private lateinit var ivImg: ImageView

  constructor(context: Context) : super(context) {
    initView(context, null)
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    initView(context, attrs)
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  ) {
    initView(context, attrs)
  }

  private fun initView(context: Context, attrs: AttributeSet?) {
    val view = LayoutInflater.from(context).inflate(R.layout.custom_pictxt, this)
    ivImg = view.findViewById(R.id.ivContent)
    val tvContent = view.findViewById<TextView>(R.id.tvContent)

    val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomPicText)
    if (typeArray != null) {
      val picBackground = typeArray.getResourceId(R.styleable.CustomPicText_picSrc, 0)
      val picWidth = typeArray.getDimension(R.styleable.CustomPicText_picWidth, 40F)
      val picHeight = typeArray.getDimension(R.styleable.CustomPicText_picHeight, 40F)
      ivImg.setImageResource(picBackground)
      ivImg.minimumWidth = picWidth.toInt()
      ivImg.minimumWidth = picHeight.toInt()

      val text = typeArray.getString(R.styleable.CustomPicText_picText)
      val textColor = typeArray.getColor(R.styleable.CustomPicText_picTextColor, Color.BLACK)
      val textSize = typeArray.getResourceId(R.styleable.CustomPicText_picTextSize, 16)
      tvContent.text = text
      tvContent.textSize = textSize.toFloat()
      tvContent.setTextColor(textColor)
      typeArray.recycle()
    }
  }

  fun getImage(): ImageView {
    return ivImg
  }
}