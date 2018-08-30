package com.shenmou.wifidirectdemo.utils;

/*
 * 该类是用来对文件进行基本的操作，包括复制，移动，创建，删除等操作。
 */

import android.util.Log;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    private static final String TAG = "文件操作";

    /**
     * 创建文件
     * @param filePath 包含文件名
     * @return 文件新建成功则返回true
     */
    public static boolean createFile(String filePath) {
        Log.e(TAG, "createFile: filePath -- >"+filePath );
        File file = new File(filePath);
        int index = filePath.lastIndexOf(File.separator);
        String dir = filePath.substring(0,index);//目录
        File files = new File(dir);
        if(!files.exists()){
            if(!files.mkdirs()){
                Log.e(TAG, "createFile: 创建目录失败");
                return false;
            }
        }
        if(file.exists()){
            file.delete();
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
