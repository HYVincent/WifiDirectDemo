package com.shenmou.wifidirectdemo.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * @author Vincent QQ:1032006226
 * @version v1.0
 * @name EasyApp
 * @page com.common.util.file
 * @class describe
 * @date 2018/5/26 14:21
 */
public class FileUtils {

    /**
     * 获取外部存储根目录
     * @param mContext
     * @return
     */
    public static String getExternalStorageRoot(Context mContext){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param dirPath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }



    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 判断文件路径是否存在，不存在则创建
     * @param filePath
     * @return
     */
    public static File getFileByPath(String filePath) {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }
}
