package com.shenmou.wifidirectdemo.utils;

import android.util.Log;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.utils
 * @class describe
 * @date 2018/8/30 9:30
 */
public class LogUtils {

    private static final String TAG = "LogUtils";
    public static final boolean DEBUG = true;

    public static void v(String log){
        Log.v(TAG, log);
    }

    public static void d(String log){
        Log.d(TAG, log);
    }

    public static void e(String log){
        Log.e(TAG, log);
    }

    public static void e(Exception log){
        Log.e(TAG, log.getMessage());
    }


}
