package com.shenmou.wifidirectdemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.utils
 * @class describe
 * @date 2018/8/27 15:12
 */
public class ImageUtil {

    public static Bitmap pathToBitmap(String imgPath) {
        if(TextUtils.isEmpty(imgPath)){
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(imgPath);
            return  BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

}
