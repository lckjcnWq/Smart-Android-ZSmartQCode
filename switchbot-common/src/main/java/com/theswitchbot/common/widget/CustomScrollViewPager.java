package com.theswitchbot.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class CustomScrollViewPager extends ViewPager {
    private boolean isCanScroll = true;

    public CustomScrollViewPager(Context context) {
        super(context);
        setMyScroller();
    }

    public CustomScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(!isCanScroll){//设置为false时，viewpager不能横向滑动
//            requestDisallowInterceptTouchEvent(true);//使viewpager不再通过onInterceptTouchEvent捕获触摸事件
//        }
//        return super.dispatchTouchEvent(ev);

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        Log.d("chengtong", "dispatchTouchEvent: " + isCanScroll);
        return isCanScroll && super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("onTouchEvent", "onTouchEvent: " + isCanScroll);

        return isCanScroll && super.onTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item);
    }
    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new ViewPagerScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class ViewPagerScroller extends Scroller {
        private int mScrollDuration = 700;             // 滑动速度
        public void setScrollDuration(int duration){
            this.mScrollDuration = duration;
        }

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }



        public void initViewPagerScroll(ViewPager viewPager) {
            try {
                Field mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                mScroller.set(viewPager, this);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
