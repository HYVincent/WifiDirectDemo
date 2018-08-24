package com.common.util;

import android.content.Context;

/**
 * @author Administrator QQ:1032006226
 * @version v1.0
 * @name EasyApp
 * @page com.common.util
 * @class describe
 * @date 2018/5/15 0:23
 */
public class CommonUtil {

    private static CommonUtil instance;

    public static CommonUtil getInstance() {
        if(instance == null){
            instance = new CommonUtil();
        }
        return instance;
    }

    private Context mContext;

    public void init(Context mContext){
        this.mContext = mContext;
    }

    public Context getmContext() {
        return mContext;
    }
}
