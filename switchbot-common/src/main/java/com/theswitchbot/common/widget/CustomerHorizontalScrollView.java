package com.theswitchbot.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import androidx.annotation.RequiresApi;


public class CustomerHorizontalScrollView extends HorizontalScrollView {
    private final static String TAG = "CustomerHorizontalScrollView";

    public CustomerHorizontalScrollView(Context context) {
        super(context);
    }

    public CustomerHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomerHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * 可以在此处理冲突
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 还没滑到右边，请求父控件不要拦截我的事件，事件自己处理 true ；已经滑到右边，则事件交由父控件处理 false。
       getParent().requestDisallowInterceptTouchEvent(!isScrollToRight());
        return super.dispatchTouchEvent(ev);
    }

   /**
     * 也可以在此处理冲突
     * @param ev
     * @return
     */
    @SuppressLint("LongLogTag")
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                if (isScrollToLeft() || isScrollToRight())
                {
                    Log.e(TAG, "滑到" + ( isScrollToLeft() ? "左边" : "右边" ) );
                    // 把事件交给父控件处理，例如：viewpager滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 请求父控件可以拦截事件
                getParent().requestDisallowInterceptTouchEvent(false);
                break;

            default:
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 是否已经滑到了最右边
     *
     * @return
     */
    private boolean isScrollToRight() {
        return getChildAt(getChildCount() - 1).getRight() == getScrollX() + getWidth();
    }

    /**
     * 是否已经滑到了最左边
     * @return
     */
    private boolean isScrollToLeft() {
        return getScrollX() == 0;
    }
}
