package com.shenmou.wifidirectdemo.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.service
 * @class describe
 * @date 2018/8/28 10:53
 */
public class MyService extends Service {

    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    private String ipAddress;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
