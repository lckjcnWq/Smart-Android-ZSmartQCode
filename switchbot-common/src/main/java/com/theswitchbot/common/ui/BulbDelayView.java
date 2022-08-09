package com.theswitchbot.common.ui;

import static com.theswitchbot.common.util.UiUtilsKt.sp2px;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.theswitchbot.common.R;

public class BulbDelayView extends View {
    //圆轮颜色
    private int mRingColor;
    //圆轮宽度
    private float mRingWidth;
    //圆轮进度值文本大小
    private int mRingProgessTextSize;
    //宽度
    private int mWidth;
    //高度
    private int mHeight;
    private Paint mPaint;
    private Paint mPaintBg;
    //圆环的矩形区域
    private RectF mRectF;
    //
    private int mProgessTextColor;
    private int mCountdownTime;
    private float mCurrentProgress;
    private OnCountDownFinishListener mListener;

    public BulbDelayView(Context context) {
        this(context, null);
    }

    public BulbDelayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BulbDelayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BulbDelayView);
        mRingColor = a.getColor(R.styleable.BulbDelayView_ringColor, context.getResources().getColor(R.color.colorAccent));
        mRingWidth = a.getFloat(R.styleable.BulbDelayView_ringWidth, 40);
        mRingProgessTextSize = a.getDimensionPixelSize(R.styleable.BulbDelayView_progressTextSize, sp2px(context, 20));
        mProgessTextColor = a.getColor(R.styleable.BulbDelayView_delayTextColor, context.getResources().getColor(R.color.colorAccent));
        mCountdownTime = a.getInteger(R.styleable.BulbDelayView_countdownTime, 60);
        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBg.setAntiAlias(true);
        mPaintBg.setColor(getResources().getColor(R.color.red));
        mPaintBg.setStyle(Paint.Style.STROKE);
        this.setWillNotDraw(false);
    }

    public void setCountdownTime(int mCountdownTime) {
        this.mCountdownTime = mCountdownTime;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRectF = new RectF(0 + mRingWidth / 2, 0 + mRingWidth / 2,
                mWidth - mRingWidth / 2, mHeight - mRingWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //空心
        mPaint.setStyle(Paint.Style.STROKE);
        //宽度
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setColor(getResources().getColor(R.color.white_80));
        canvas.drawArc(mRectF, 0, 360, false, mPaint);
        //颜色
        mPaint.setColor(mRingColor);

        canvas.drawArc(mRectF, -90, mCurrentProgress - 360, false, mPaint);
        mPaintBg.setStrokeWidth(mRingWidth);
        //绘制文本
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int second =  mCountdownTime - (int) (mCurrentProgress / 360f * mCountdownTime);
        String text = getShowText(second);
        textPaint.setTextSize(mRingProgessTextSize);
        textPaint.setColor(mProgessTextColor);

        //文字居中显示
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (int) ((mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.drawText(text, mRectF.centerX(), baseline, textPaint);
    }

    public String getShowText(int countSecond){
        int hour = countSecond / 3600;
        int min = countSecond % 3600 / 60;
        int second = countSecond % 3600 % 60;
        String secondStr = second >= 10 ? second + "" : "0" +  second;
        String hourStr = hour >= 10 ? hour + "" : "0" +  hour;
        String minStr =  min >= 10 ? min + "" : "0" +  min;
        if (hour > 0 || min > 0) {
            return hourStr + ":" + minStr;
        } else {
            return secondStr;
        }
    }

    private ValueAnimator getValA(long countdownTime) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setDuration(countdownTime);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        return valueAnimator;
    }
    /**
     * 开始倒计时
     */
    public void startCountDown() {
        ValueAnimator valueAnimator = getValA(mCountdownTime * 1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
                mCurrentProgress = (int) (360 * (i / 100f));
                invalidate();
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //倒计时结束回调
                if (mListener != null) {
                    mListener.countDownFinished();
                }
            }

        });
    }
    public void setAddCountDownListener(OnCountDownFinishListener mListener) {
        this.mListener = mListener;
    }
    public interface OnCountDownFinishListener {
        void countDownFinished();
    }


}