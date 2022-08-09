package com.theswitchbot.common.widget;

import android.R.attr;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.theswitchbot.common.R.color;
import com.theswitchbot.common.R.id;
import com.theswitchbot.common.R.layout;
import com.theswitchbot.common.R.styleable;

/**
 * Created by wohand on 2017/10/28.
 */

public class SettingItemView extends RelativeLayout {

    AppCompatTextView mKeyNameTv;
    AppCompatTextView mValueTv;
    ConstraintLayout mRootCardView;
    Context mContext;
    private SettingItemClickListener mListener;
    private AppCompatImageView mIconIv;
    private AppCompatImageView mArrowIv;
    private AppCompatImageView mValueAlertIv;
    private AppCompatTextView mRightTextView;
    private ArrowIconClickListener mArrowIconListener;

    public SettingItemView(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        View.inflate(getContext(), layout.view_setting_item, this);
        mKeyNameTv = findViewById(id.key_name_tv);
        mIconIv = findViewById(id.icon_iv);
        mArrowIv = findViewById(id.arrow_iv);
        mValueTv = findViewById(id.value_tv);
        mValueAlertIv = findViewById(id.value_alert_iv);
        mRootCardView = findViewById(id.root_card_view);
        mRightTextView = findViewById(id.right_textview);
        TypedArray typeValue = mContext.obtainStyledAttributes(attrs, styleable.SettingView);
        boolean isIconShow = typeValue.getBoolean(styleable.SettingView_isIconShow, false);
        boolean isArrowShow = typeValue.getBoolean(styleable.SettingView_isArrowShow, false);
        boolean isLeftTextViewShow = typeValue.getBoolean(styleable.SettingView_isLeftTextViewShow, false);
        boolean isRightTextViewShow = typeValue.getBoolean(styleable.SettingView_isRightTextViewShow, false);
        boolean enable = typeValue.getBoolean(styleable.SettingView_enable, true);
        String isRightTextViewHint = typeValue.getString(styleable.SettingView_setRightTextViewhint);
        boolean arrowClick = typeValue.getBoolean(styleable.SettingView_arrowClick, true);
        Log.d("TextViewSettingItemView", "init: " + isRightTextViewHint);
        String content = typeValue.getString(styleable.SettingView_settingTitle);
        setEnabled(enable);
        if (null != content) {
            mKeyNameTv.setText(content);
        }
        if (null != isRightTextViewHint) {
            mRightTextView.setHint(isRightTextViewHint);
        }
        mRightTextView.setText(typeValue.getString(styleable.SettingView_settingRightTextView));
        mIconIv.setVisibility(isIconShow ? VISIBLE : GONE);
        if (!isIconShow){
            ConstraintLayout.LayoutParams params= (ConstraintLayout.LayoutParams) mKeyNameTv.getLayoutParams();
            params.leftToLeft=ConstraintLayout.LayoutParams.PARENT_ID;
            mKeyNameTv.setLayoutParams(params);
        }else {
            ConstraintLayout.LayoutParams params= (ConstraintLayout.LayoutParams) mKeyNameTv.getLayoutParams();
            params.leftToLeft=mIconIv.getId();
            mKeyNameTv.setLayoutParams(params);
        }
        mArrowIv.setVisibility(isArrowShow ? VISIBLE : GONE);
        mRightTextView.setVisibility(isRightTextViewShow ? VISIBLE : GONE);
        if (arrowClick){
            mArrowIv.setOnClickListener(v -> {
                if (null != mArrowIconListener) {
                    mArrowIconListener.onArrowIconClick(v);
                }
            });
        }
        Drawable background = typeValue.getDrawable(styleable.SettingView_background);
        if (background != null) {
            mRootCardView.setBackground(background);
        }
        Drawable background2 = typeValue.getDrawable(styleable.SettingView_arrowicon);
        if (background2 != null) {
            mArrowIv.setVisibility(VISIBLE);
            mArrowIv.setImageDrawable(background2);
        }
        Drawable drawable = typeValue.getDrawable(styleable.SettingView_settingIcon);
        if (drawable != null) {
            mIconIv.setImageDrawable(drawable);
        }

        boolean isTitleBold = typeValue.getBoolean(styleable.SettingView_isTitleTextBold, true);
        ColorStateList colorStateList = typeValue.getColorStateList(styleable.SettingView_keyTextColor);
        if (colorStateList != null) {
            mKeyNameTv.setTextColor(colorStateList);
        }
        ColorStateList contentColorStateList = typeValue.getColorStateList(styleable.SettingView_contentColor);
        if (contentColorStateList != null) {
            mValueTv.setTextColor(contentColorStateList);
        }

        mKeyNameTv.setTypeface(isTitleBold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mKeyNameTv.setActivated(true);
        mKeyNameTv.setEnabled(true);
        typeValue.recycle();
        mRootCardView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(v);
            }
        });
    }

    public void setTitle(String title) {
        mKeyNameTv.setText(title);
    }

    public void setTitle(@StringRes int resid) {
        mKeyNameTv.setText(mContext.getResources().getText(resid));
    }

    public TextView getRightText() {
        return mRightTextView;
    }

    public String getRightTextView() {
        return mRightTextView.getText().toString();
    }

    public void setRightTextView(String str) {
        mRightTextView.setText(str);
    }

    public void setmRightTextViewVisiblity(int visibility) {
        mRightTextView.setVisibility(visibility);
    }

    public void setmRightTextViewTextColor(int color) {
        mRightTextView.setTextColor(color);
    }

    public void setHint(String valueHint) {
        mValueTv.setHint(valueHint);
    }

    public void setValueIconVisible(boolean show) {
        mValueAlertIv.setVisibility(show ? VISIBLE : GONE);
    }

    public void setValueIcon(@DrawableRes int resDrawble) {
        mValueAlertIv.setVisibility(VISIBLE);
        mValueAlertIv.setImageResource(resDrawble);
    }

    @Override
    public void setEnabled(boolean status) {
        mRootCardView.setEnabled(status);
        mRootCardView.setClickable(status);
        mKeyNameTv.setEnabled(status);

        mRootCardView.setActivated(status);
        mRootCardView.setActivated(status);
        mKeyNameTv.setActivated(status);
    }

    public void setIconIvDrawable(@DrawableRes int resDrawble) {
        mIconIv.setImageResource(resDrawble);
    }

    public void setEnabled(boolean status, boolean isClickAble) {
        if (isClickAble && !status) {
            mKeyNameTv.setTextColor(getResources().getColor(color.text_disable_color));
        } else {
            mRootCardView.setEnabled(status);
            mRootCardView.setClickable(status);
            mKeyNameTv.setEnabled(status);
            mKeyNameTv.setTextColor(getResources().getColor(color.cc4));
        }
    }

    public void setSelect(boolean status) {
        setSelected(status);
        mKeyNameTv.setSelected(status);
    }
    public TextView getLeftTextView(){
        return mKeyNameTv;
    }

    public TextView getValueTextView(){
        return mValueTv;
    }

    public String getValue() {
        return mValueTv.getText().toString();
    }

    public void setValue(@StringRes int resid) {
        mValueTv.setText(mContext.getResources().getText(resid));
    }

    public void setValue(String valueContent) {
        mValueTv.setText(valueContent);
    }

    public void setIconIvDrawableVisiable(Boolean isshow) {
        if (isshow) {
            mIconIv.setVisibility(VISIBLE);
        } else {
            mIconIv.setVisibility(GONE);
        }
    }

    public void setArrowVisiable(boolean isshow) {
        if (isshow) {
            mArrowIv.setVisibility(VISIBLE);
        } else {
            mArrowIv.setVisibility(GONE);
        }
    }

    public void setArrowIcon(@DrawableRes int resDrawble) {
        mArrowIv.setVisibility(VISIBLE);
        mArrowIv.setImageResource(resDrawble);
    }

    public void setOnArrowIconClickListener(ArrowIconClickListener listener) {
        mArrowIconListener = listener;

    }

    public void setOnSettingItemListener(SettingItemClickListener listener) {
        mListener = listener;

        try {
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                TypedValue outValue = new TypedValue();
                getContext().getTheme().resolveAttribute(attr.selectableItemBackground, outValue, true);
                mRootCardView.setForeground(getContext().getDrawable(outValue.resourceId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performItemClick() {
        mRootCardView.performClick();
    }

    //点击事件接口
    public interface SettingItemClickListener {
        void onClick(View v);
    }

    public interface ArrowIconClickListener {
        void onArrowIconClick(View v);
    }
}
