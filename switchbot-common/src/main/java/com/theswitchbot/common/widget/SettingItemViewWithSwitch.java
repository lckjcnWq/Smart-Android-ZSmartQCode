package com.theswitchbot.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.theswitchbot.common.R;


/**
 * 左边有图标，右边有图标，而且左右都有文字，最后甚至还可以有箭头的view
 */
public class SettingItemViewWithSwitch extends RelativeLayout {
    Context mContext;
    AppCompatImageView iconIv, rightIv, arrowIv;
    AppCompatTextView titleTv, rightTv;
    SwitchCompat rightSwitch;
    ConstraintLayout rootCardView;
    OnClickListener mListener;
    SwitchCompat.OnCheckedChangeListener switchListener;

    public SettingItemViewWithSwitch(Context context) {
        super(context);
        mContext = context;
    }

    public SettingItemViewWithSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public SettingItemViewWithSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        View.inflate(getContext(), R.layout.view_title_right_text_and_arrow, this);
        rootCardView = findViewById(R.id.root_card_view);
        iconIv = findViewById(R.id.icon_iv);
        rightIv = findViewById(R.id.right_iv);
        arrowIv = findViewById(R.id.arrow_iv);
        titleTv = findViewById(R.id.title_tv);
        rightTv = findViewById(R.id.tvRight);
        rightSwitch = findViewById(R.id.right_switch);
        TypedArray typeValue = mContext.obtainStyledAttributes(attrs, R.styleable.TextViewWithArrowAndRightText);
        boolean isIconShow = typeValue.getBoolean(R.styleable.TextViewWithArrowAndRightText_icon_show, false);
        boolean isRightTvShow = typeValue.getBoolean(R.styleable.TextViewWithArrowAndRightText_right_tv_show, false);
        boolean isRightIvShow = typeValue.getBoolean(R.styleable.TextViewWithArrowAndRightText_right_iv_show, false);
        boolean isRightSwitchShow = typeValue.getBoolean(R.styleable.TextViewWithArrowAndRightText_right_switch_show, false);
        int arrowVisibility = typeValue.getInt(R.styleable.TextViewWithArrowAndRightText_arrow_visiblity, VISIBLE);
        String title = typeValue.getString(R.styleable.TextViewWithArrowAndRightText_title);
        String rightText = typeValue.getString(R.styleable.TextViewWithArrowAndRightText_right_text);
        Drawable icon = typeValue.getDrawable(R.styleable.TextViewWithArrowAndRightText_icon);
        Drawable rightIcon = typeValue.getDrawable(R.styleable.TextViewWithArrowAndRightText_right_icon);
        titleTv.setText(title);
        rightTv.setText(rightText);
        iconIv.setImageDrawable(icon);
        rightIv.setImageDrawable(rightIcon);
        arrowIv.setVisibility(arrowVisibility == 0 ? VISIBLE : (arrowVisibility == 1 ? INVISIBLE : GONE));
        iconIv.setVisibility(isIconShow ? VISIBLE : GONE);
        rightTv.setVisibility(isRightTvShow ? VISIBLE : GONE);
        rightIv.setVisibility(isRightIvShow ? VISIBLE : GONE);
        rightSwitch.setVisibility(isRightSwitchShow ? VISIBLE : GONE);
        rootCardView.setOnClickListener(mListener);
        rightSwitch.setOnCheckedChangeListener(switchListener);
        titleTv.setActivated(true);

        typeValue.recycle();
    }

    public void setTitle(String title){
        titleTv.setText(title);
    }

    public String getTitle(){
        if(titleTv != null) {
            return titleTv.getText().toString();
        } else {
            return null;
        }
    }

    public void setRightText(String text){
        rightTv.setText(text);
    }

    public String getRightText(){
        if(titleTv != null) {
            return rightTv.getText().toString();
        } else {
            return null;
        }
    }

    public void setIconDrawable(Drawable drawable){
        iconIv.setImageDrawable(drawable);
    }

    public void setRightIvDrawable(Drawable drawable){
        rightIv.setImageDrawable(drawable);
    }

    public void setRightSwitchListener(SwitchCompat.OnCheckedChangeListener listener){
        switchListener = listener;
        rightSwitch.setOnCheckedChangeListener(switchListener);
    }

    public SwitchCompat.OnCheckedChangeListener getRightSwitchListener(){
        return switchListener;
    }

    @Override
    public void setOnClickListener(OnClickListener listener){
        mListener = listener;
        rootCardView.setOnClickListener(listener);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TypedValue outValue = new TypedValue();
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                rootCardView.setForeground(getContext().getDrawable(outValue.resourceId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEnabled(boolean status) {
        rootCardView.setEnabled(status);
        rootCardView.setClickable(status);
        titleTv.setEnabled(status);
        rightTv.setEnabled(status);
        rightSwitch.setEnabled(status);

        rootCardView.setActivated(status);
        titleTv.setActivated(status);
        rightTv.setActivated(status);
        rightSwitch.setActivated(status);
    }

    public void setRightSwitchCheckedNoEvent(boolean checked){
        rightSwitch.setOnCheckedChangeListener(null);
        rightSwitch.setChecked(checked);
        rightSwitch.setOnCheckedChangeListener(switchListener);
    }

    public AppCompatTextView getTitleTv() {
        return titleTv;
    }

    public boolean getSwitchChecked(){
        return rightSwitch.isChecked();
    }

    public void setArrowIvVisibility(int visibility){
        arrowIv.setVisibility(visibility);
    }

    public void setSwitchVisibility(int visibility){
        rightSwitch.setVisibility(visibility);
    }

    public void setBtCheck(boolean status) {
        rightSwitch.setChecked(status);
    }

    public void setRightIvVisibility(int visibility){
        rightIv.setVisibility(visibility);
    }

    public void setArrowIvDrawable(Drawable drawable){
        arrowIv.setImageDrawable(drawable);
    }

    public void performItemClick(){
        rootCardView.performClick();
    }
}