package com.theswitchbot.common.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

/**
 * Created  by Administrator on 2021/4/18 10:25
 * https://blog.csdn.net/zhaohuiyang_949/article/details/83684288
 */
public class StatusBarUtil {

    /**
     * 为我们的 activity 的状态栏设置颜色
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(Activity activity,int color){
        // 5.0 以上
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            // 直接调用系统提供的方法 setStatusBarColor
            activity.getWindow().setStatusBarColor(color);
        }else if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
            // 4.4 - 5.0 之间  采用一个技巧，首先把他弄成全屏，在状态栏的部分加一个布局
            // 首先把他弄成全屏（），在状态栏的部分加一个布局
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            // 电量 时间 网络状态 都还在
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 在状态栏的部分加一个布局 setContentView 源码分析，自己加一个布局 (高度是状态栏的高度)
            View view=new View(activity);
            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));
            view.setLayoutParams(params);
            view.setBackgroundColor(color);
            ViewGroup decorview= (ViewGroup) activity.getWindow().getDecorView();
            decorview.addView(view);
            //但是android:fitsSystemWindows="true" 每个布局都要写？
            //  DecorView是一个 FrameLayout 布局 , 会加载一个系统的布局（LinearLayout） ,
            // 在系统布局中会有一个 id 为 android.R.id.content 这布局是（RelativeLayout）

            // 获取activity中setContentView布局的根布局,根布局里面才是放的我们自己的布局
            ViewGroup contentView=activity.findViewById(android.R.id.content);
            //方式1 直接给根布局设置paddingTop
            contentView.setPadding(0,getStatusBarHeight(activity),0,0);
           // View activityView= contentView.getChildAt(0);
            //方式2
           // activityView.setFitsSystemWindows(true);
            //方式3 给我们自己的布局设置paddingTop
           // activityView.setPadding(0,getStatusBarHeight(activity),0,0);
        }

    }

    /**
     * 获取状态栏的高度
     * @param activity
     * @return
     */
    private static int getStatusBarHeight(Activity activity) {
        // 插件式换肤：怎么获取资源的，先获取资源id，根据id获取资源
        Resources resources = activity.getResources();
        int statusBarHeightId = resources.getIdentifier("status_bar_height","dimen","android");
        Log.e("TAG",statusBarHeightId+" -> "+resources.getDimensionPixelOffset(statusBarHeightId));
        return resources.getDimensionPixelOffset(statusBarHeightId);
    }

    /**
     * 设置activity全屏
     * @param activity
     */
    public static void setStatusBarTranslucent(Activity activity) {
        //      // 5.0 以上
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            //设置状态栏透明
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            //但是全屏以后布局看上去整体向上移动了 我们需要在跟布局setFitsSystemWindows 或者设置padingTop 这样我们就不需要每次在跟布局里面设置android:fitsSystemWindows="true"了
             //以下三种方式任选其一
            ViewGroup contentView=activity.findViewById(android.R.id.content);
            //方式1 直接给根布局设置paddingTop
           // contentView.setPadding(0,getStatusBarHeight(activity),0,0);

        }else if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){//4.4 - 5.0 之间
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //但是全屏以后布局看上去整体向上移动了 我们需要在跟布局setFitsSystemWindows 或者设置padingTop 这样我们就不需要每次在跟布局里面设置android:fitsSystemWindows="true"了
            ViewGroup contentView=activity.findViewById(android.R.id.content);
            //方式1 直接给根布局设置paddingTop
           //  contentView.setPadding(0,getStatusBarHeight(activity),0,0);
        }
    }


    /**
     * 界面设置状态栏字体颜色
     */
    public static void changeStatusBarTextImgColor(Activity activity,boolean isBlack) {
        if (isBlack) {
            //设置状态栏黑色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //恢复状态栏白色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }


    /**
     * 判断颜色是不是亮色
     * 这里可以获取titlebar颜色亮暗自动设置状态栏的字体颜色的黑和白色？？可以尝试一下
     *
     * @param color
     * @return
     * @from https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    private boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }







}
