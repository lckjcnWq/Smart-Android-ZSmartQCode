package com.theswitchbot.common.logger;

import android.util.Log;

public class SimpleFormatStrategy implements FormatStrategy{
    @Override
    public void log(int priority, String tag, String message) {
        Log.println(priority,tag,message);
    }
}
