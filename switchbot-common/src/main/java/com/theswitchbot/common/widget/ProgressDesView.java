package com.theswitchbot.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.theswitchbot.common.R;


/**
 * Created by wohand on 2017/10/28.
 */

public class ProgressDesView extends ConstraintLayout {

    private static final String TAG = "ProgressDesView";
    Context mContext;
    ImageView mDeviceIconIv;
    CircularProgressBar mPressProgressBar;
    ConstraintLayout mRootCardView;
    private TextView press_text;

    public ProgressDesView(Context context) {
        super(context);
        mContext = context;
    }

    public ProgressDesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public ProgressDesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {

        TypedArray typeValue = mContext.obtainStyledAttributes(attrs, R.styleable.ProgressDesView);
        //给bot添加列表选择新加的参数,可以更换圈的大小
        //后续要扩展的话(新的圈的大小),可以增加int判断,或者声明int常量

        View.inflate(mContext, R.layout.view_progress_des, this);
        press_text=findViewById(R.id.press_text);
        mDeviceIconIv = findViewById(R.id.customer_icon_iv);
        mPressProgressBar = findViewById(R.id.press_progressBar);
        mRootCardView = findViewById(R.id.root_card_view);
        Drawable drawable = typeValue.getDrawable(R.styleable.ProgressDesView_imageIcon);
        //设置padding属性
        int iconPadding = typeValue.getDimensionPixelSize(R.styleable.ProgressDesView_imageIconPadding, 0);
        if (drawable != null) {
            mDeviceIconIv.setImageDrawable(drawable);
        }
        mDeviceIconIv.setPadding(iconPadding,iconPadding,iconPadding,iconPadding);
        typeValue.recycle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int[] sysAttrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray ta = getContext().obtainStyledAttributes(sysAttrs);
            Drawable mDefaultFocusHighlightCache = ta.getDrawable(0);
            ta.recycle();
            setForeground(mDefaultFocusHighlightCache);
        }
    }

    public void setDeviceIconDrawable(int drawableId){
        mDeviceIconIv.setImageDrawable(mContext.getResources().getDrawable(drawableId));
    }

    public void setPressProgressBar() {
        mPressProgressBar.setVisibility(View.VISIBLE);
        mPressProgressBar.setProgressWithAnimation(100);
    }

    public void setViewSelect(boolean status) {
        mDeviceIconIv.setSelected(status);
    }

    public void setViewActivated(boolean status) {
        mDeviceIconIv.setActivated(status);
    }
    public void setTextview(String status) {
        press_text.setVisibility(VISIBLE);
        press_text.setText(status);
    }
    public void setTextviewColor(int textStyle) {
        press_text.setTextColor(textStyle);
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mDeviceIconIv.setEnabled(enabled);
    }

    public void pauseAnimator(){
        mPressProgressBar.pauseAnimator();
    }

    public void resumeAnimator(){
        mPressProgressBar.resumeAnimator();
    }

    public void cancelProgressBar() {
        mPressProgressBar.setProgress(0);
        mPressProgressBar.cancelAnimator();
    }
}
