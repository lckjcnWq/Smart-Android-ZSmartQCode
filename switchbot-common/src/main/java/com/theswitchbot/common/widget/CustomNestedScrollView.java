package com.theswitchbot.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class CustomNestedScrollView extends NestedScrollView {
    public CustomNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private float xDistance, yDistance, lastX, lastY;
    private boolean isIntercept;
    private boolean isSolve;//是否完成了拦截判断，如果决定拦截，那么同系列事件就不能设置为不拦截

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);// / 3; // favor X events
                lastX = curX;
                lastY = curY;
                if (yDistance > xDistance) {
//                    Log.i("Tag", "拦截");
                    return true;
                }
                break;
//                if (!isSolve) {//是否已经决定拦截/不拦截？
//                    isIntercept = (Math.abs(ev.getX() - mPointGapF.x) > Math.abs(ev.getY() - mPointGapF.y)*2);//如果是左右滑动，且水平角度小于30°，就拦截
//                    isSolve = true;
//                }
//                return isIntercept;//如果是左右滑动，就拦截


        }
        return super.onInterceptTouchEvent(ev);
    }
}
