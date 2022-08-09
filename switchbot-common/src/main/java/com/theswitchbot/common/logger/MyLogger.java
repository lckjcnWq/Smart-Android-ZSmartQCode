package com.theswitchbot.common.logger;

import android.util.Log;

/**
 * Created by cbd on 2021/6/10
 * 日志加TAG 和 开关
 */
public class MyLogger {

    private static  boolean SHOW_LOG = true;
    private static  String TAG = "TAG";

    public static void init(String tag ,boolean isShow){
        TAG=tag;
        SHOW_LOG=isShow;
    }


    //默认TAG
    public static  void d(String str){
        if(SHOW_LOG){
            Log.d(TAG,str);
        }
    }

    //自定义TAG
    public static  void d(String tag, String str){
        if(SHOW_LOG){
            Log.d(tag,str);
        }
    }

    //默认TAG
    public static  void i(String str){
        if(SHOW_LOG){
            Log.i(TAG,str);
        }
    }

    //自定义TAG
    public static  void i(String tag, String str){
        if(SHOW_LOG){
            Log.i(tag,str);
        }
    }

    //默认TAG
    public static  void w(String str){
        if(SHOW_LOG){
            Log.w(TAG,str);
        }
    }

    //自定义TAG
    public static  void w(String tag, String str){
        if(SHOW_LOG){
            Log.w(tag,str);
        }
    }

    //默认TAG
    public static  void e(String str){
        if(SHOW_LOG){
            Log.e(TAG,str);
        }
    }

    //自定义TAG
    public static  void e(String tag, String str){
        if(SHOW_LOG){
            Log.e(tag,str);
        }
    }
}
