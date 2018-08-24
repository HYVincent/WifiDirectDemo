package com.shenmou.wifidirectdemo.base;

import android.app.Application;

import com.common.util.CommonUtil;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo
 * @class describe
 * @date 2018/8/24 10:24
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CommonUtil.getInstance().init(getApplicationContext());
    }
}
