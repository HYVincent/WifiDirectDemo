package com.common.view.log;

import android.util.Log;

import com.common.view.BuildConfig;


/**
 * @author Vincent QQ:1032006226
 * @version v1.0
 * @name StarKangMedical_Android
 * @page com.vincent.mylibrary.util.log
 * @class describe
 * @date 2018/5/3 9:53
 */
public class LogUtil {
    /**
     * log控制开关
     */
    private static  boolean logSwitch = BuildConfig.DEBUG;
    private static String TAG = "运行日志";
    /**
     * 打开log
     */
    public static void openLog(){
        logSwitch = true;
    }

    /**
     * 关闭log
     */
    public static void closeLog(){
        logSwitch = false;
    }

    public static void d(String tag,String log){
        if(logSwitch){
            Log.d(tag, log);
        }
    }

    /**
     * log
     * @param log
     */
    public static void d(String log){
        if(logSwitch){
           d(TAG,log);
        }
    }

    /**
     * log
     * @param log
     */
    public static void e(String log){
        if(logSwitch){
           e(TAG,log);
        }
    }


    public static void e(String tag,String log){
        if(logSwitch){
            Log.d(tag, log);
        }
    }

}
