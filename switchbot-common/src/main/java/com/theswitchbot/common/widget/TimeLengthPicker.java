package com.theswitchbot.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;

import com.theswitchbot.common.R;


public class TimeLengthPicker extends LinearLayout {
    private MaterialNumberPicker npMinute;
    private MaterialNumberPicker npSecond;
    private int timeUnit=60;
    private int maxLength=3600;
    private int minLength=3;
    private ValueChangedListener valueChangedListener;

    public TimeLengthPicker(Context context) {
        this(context, null);
    }

    public TimeLengthPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLengthPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimeLengthPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 设置最大最小范围
     * @param max
     * @param min
     */
    public void setTimeRange(int max,int min){
        maxLength=max;
        minLength=min;
    }

    /**
     * 设置当前时长
     * @param length
     */
    public void setLength(long length){
        int min= (int) (length/timeUnit);
        int sec= (int) (length-min*timeUnit);
        npMinute.setValue(min);
        npSecond.setValue(sec);
    }


    public void setValueChangedListener(ValueChangedListener valueChangedListener) {
        this.valueChangedListener = valueChangedListener;
    }

    private void init(){
        setOrientation(HORIZONTAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_alert_time_settings,this,true);
        npMinute=findViewById(R.id.npMinute);
        npSecond=findViewById(R.id.npSeconds);

        npMinute.setOnScrollListener((view, scrollState) -> {
            if (valueChangedListener!=null&&scrollState== NumberPicker.OnScrollListener.SCROLL_STATE_IDLE){
                rangeCheck();
                valueChangedListener.onChanged(npMinute.getValue(),npSecond.getValue());
            }
        });
        npSecond.setOnScrollListener((view, scrollState) -> {
            if (valueChangedListener!=null&&scrollState== NumberPicker.OnScrollListener.SCROLL_STATE_IDLE){
                rangeCheck();
                valueChangedListener.onChanged(npMinute.getValue(),npSecond.getValue());
            }
        });
    }

    private void rangeCheck(){
        if (getLength()>maxLength){
            setLength(maxLength);
        }
        if (getLength()<minLength){
            setLength(minLength);
        }
    }


    public void setNpBackgroundColor(int resId){
        npMinute.setBackgroundColor(getResources().getColor(resId));
        npSecond.setBackgroundColor(getResources().getColor(resId));
    }

    public int getLength(){
        return npMinute.getValue()*timeUnit+npSecond.getValue();
    }

    public interface ValueChangedListener{
        void onChanged(int minute,int seconds);
    }
}


