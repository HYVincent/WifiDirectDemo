package com.shenmou.wifidirectdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name WifiDirectDemo
 * @page com.shenmou.wifidirectdemo.utils
 * @class describe
 * @date 2018/8/27 15:12
 */
public class ImageUtil {
    /**
     * 把base 64转为Bitmap
     * @param base64
     * @return
     */
    public static Bitmap base64ToBitmap(String base64){
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray=Base64.decode(base64, Base64.DEFAULT);
            bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 图片转为Base 64
     * @param mContext
     * @param imgPath
     * @return
     */
    public static String imgToBase64(Context mContext,String imgPath){
        InputStream inputStream = imgToInputStream(mContext,imgPath);
        if(inputStream != null){
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
            byte[] bytes=bos.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        return "";
    }

    /**
     * 图片转为InputStream
     * @param imgPath 图片本地路径
     * @return InputStream
     */
    public static InputStream imgToInputStream(Context mContext,String imgPath){
        if(TextUtils.isEmpty(imgPath)){
            return null;
        }
        try {
            return mContext.getContentResolver().openInputStream(Uri.fromFile(new File(imgPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 图片转为byte数组
     * @param imgPath 图片本地路径
     * @return 数组
     */
    public static byte[] imgTobyteArray(Context mContext,String imgPath){
        try {
            if(TextUtils.isEmpty(imgPath)){
                return null;
            }
            InputStream inputStream = imgToInputStream(mContext,imgPath);
            if(inputStream != null){
                return readStream(inputStream);
            }else {
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 图片转为InputStream
     * @param bitmap 图片Bitmap对象
     * @return InputStream
     */
    public static InputStream bitmapToInputStream(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * @param
     * @param inStream 将图片内容解析成字节数组
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }



}
