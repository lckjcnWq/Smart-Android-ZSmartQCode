package com.theswitchbot.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.theswitchbot.common.R;


public class ColorPickItemView extends FrameLayout {
    ImageView mItemContent;
    GradientDrawable drawable;
    Context mContext;
    LinearLayout mColorPickItemContainer;
    int currentColor;
    boolean isColor = false;
    public static final int COLORFUL_INT_NUM = -99999936;

    public ColorPickItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.color_pick_item_view, this);
        mItemContent = findViewById(R.id.item_content);
        mColorPickItemContainer = findViewById(R.id.color_pick_item_container);
        TypedArray typeValue = context.obtainStyledAttributes(attrs, R.styleable.ColorPickItemView);
        if (typeValue.getDrawable(R.styleable.ColorPickItemView_src) == null) {
            setBackgroundColor(typeValue.getColor(R.styleable.ColorPickItemView_background_color, Color.BLACK));
        } else {
            setBackgroundSrc(typeValue.getDrawable(R.styleable.ColorPickItemView_src));
        }
        typeValue.recycle();
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public boolean getIsColor(){
        return isColor;
    }

    @Override
    public void setBackgroundColor(int color) {
        currentColor = color;
        mColorPickItemContainer = findViewById(R.id.color_pick_item_container);
        mItemContent = findViewById(R.id.item_content);
        mColorPickItemContainer.setBackgroundColor(Color.TRANSPARENT);
        mItemContent.setBackground(mContext.getResources().getDrawable(R.drawable.selector_color_pick_item_view));
        drawable = (GradientDrawable) mItemContent.getBackground();
        drawable.setColor(color);
        isColor = true;
    }

    public void setBackgroundSrc(Drawable drawable) {
        mColorPickItemContainer = findViewById(R.id.color_pick_item_container);
        mItemContent = findViewById(R.id.item_content);
        mColorPickItemContainer.setBackground(mContext.getResources().getDrawable(R.drawable.selector_color_pick_item_view));
        GradientDrawable drawable2 = (GradientDrawable)mColorPickItemContainer.getBackground();
        drawable2.setColor(Color.WHITE);
        drawable2.setStroke(4, Color.WHITE);
        mItemContent.setBackground(drawable);
        isColor = false;
        currentColor = COLORFUL_INT_NUM;
    }

    public void setColorful(){
        setBackgroundSrc(getResources().getDrawable(R.mipmap.palette));
    }
}
