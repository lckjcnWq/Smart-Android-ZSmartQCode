package com.theswitchbot.common.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.theswitchbot.common.R;

/**
 * Created by wuhongyang
 */

public class CheckSettingItemView extends RelativeLayout {

    AppCompatTextView mKeyNameTv;
    SwitchCompat mItemCb;
    CardView mRootCardView;
    Context mContext;
    private SettingItemClickListener mListener;
    private AppCompatImageView mIconIv;
    private AppCompatTextView mRightTextView;
    private AppCompatImageView key_name_help;
    private HelpIconClickListener mhelpListener;

    public CheckSettingItemView(Context context) {
        super(context);
        mContext = context;
    }

    public CheckSettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public CheckSettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        View.inflate(getContext(), R.layout.view_check_setting_item, this);
        mKeyNameTv = findViewById(R.id.key_name_tv);
        mItemCb = findViewById(R.id.item_cb);
        mRootCardView = findViewById(R.id.root_card_view);
        mIconIv = findViewById(R.id.icon_iv);
        mRightTextView = findViewById(R.id.right_textview);
        key_name_help = findViewById(R.id.key_name_help);
        TypedArray typeValue = mContext.obtainStyledAttributes(attrs, R.styleable.SettingView);
        String content = typeValue.getString(R.styleable.SettingView_settingTitle);
        mKeyNameTv.setText(content);

        mRightTextView.setText(typeValue.getString(R.styleable.SettingView_settingRightTextView));

        boolean isIconShow = typeValue.getBoolean(R.styleable.SettingView_isIconShow, false);
        boolean isTitleBold = typeValue.getBoolean(R.styleable.SettingView_isTitleTextBold, true);
        boolean isRightTextViewShow = typeValue.getBoolean(R.styleable.SettingView_isRightTextViewShow, false);
        Drawable drawable1 = typeValue.getDrawable(R.styleable.SettingView_helpIcon);
        if (drawable1 != null) {
            key_name_help.setVisibility(VISIBLE);
            key_name_help.setImageDrawable(drawable1);
        }
        key_name_help.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mhelpListener != null) {
                    mhelpListener.onClick();
                }
            }
        });
        ColorStateList colorStateList = typeValue.getColorStateList(R.styleable.SettingView_keyTextColor);
        if (colorStateList != null) {
            mKeyNameTv.setTextColor(colorStateList);
        }
        mRightTextView.setVisibility(isRightTextViewShow ? VISIBLE : GONE);
        mKeyNameTv.setTypeface(isTitleBold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);

        mIconIv.setVisibility(isIconShow ? VISIBLE : GONE);
        Drawable drawable = typeValue.getDrawable(R.styleable.SettingView_settingIcon);
        if (drawable != null) {
            mIconIv.setImageDrawable(drawable);
        }
        typeValue.recycle();
        switchListener();
        mRootCardView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(v);
            }
        });
        setEnabled(true);
    }

    private void switchListener() {
        mItemCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mListener != null) {
                mListener.onButtonStateCheck(buttonView, isChecked);
            }
        });
    }

    public void setBtCheckNoEvent(boolean status) {
        mItemCb.setOnCheckedChangeListener(null);
        mItemCb.setChecked(status);
        switchListener();
    }

    public void setBtCheck(boolean status) {
        mItemCb.setChecked(status);
    }

    public boolean isChecked() {
        return mItemCb.isChecked();
    }

    public void setSelect(boolean status) {
        Log.i("test", "setSelect:" + status);
        setSelected(status);
        mKeyNameTv.setActivated(!status);
        mKeyNameTv.setEnabled(!status);
        mItemCb.setEnabled(!status);
    }

    @Override
    public void setEnabled(boolean status) {
        mRootCardView.setEnabled(status);
        mRootCardView.setClickable(status);
        mItemCb.setEnabled(status);
        mKeyNameTv.setEnabled(status);
        mRightTextView.setEnabled(status);

        mRootCardView.setActivated(status);
        mRootCardView.setActivated(status);
        mItemCb.setActivated(status);
        mKeyNameTv.setActivated(status);
        mRightTextView.setActivated(status);
    }

    //点击事件接口
    public interface SettingItemClickListener {
        default void onClick(View v){

        }

        default void onButtonStateCheck(CompoundButton button, boolean status){

        }
    }


    public void setOnSettingItemListener(SettingItemClickListener listener) {
        mListener = listener;
    }

    public SettingItemClickListener getOnSettingItemListener(){
        return mListener;
    }

    public interface HelpIconClickListener {
        void onClick();
    }
    public void setHelpIconClickListener(HelpIconClickListener listener) {
        mhelpListener = listener;
    }
    public HelpIconClickListener getHelpIconClickListener() {
        return mhelpListener;
    }
}
