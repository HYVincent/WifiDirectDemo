package com.common.util.more_language;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

/**
 * @author Vincent QQ:1032006226
 * @version v1.0
 * @name StartKangMedical_Android
 * @page com.vincent.mylibrary.more_language
 * @class describe
 * @date 2018/3/5 10:55
 */

public class ViewUtil {

    /**
     * 调用更新语言
     * @param view
     */
    public static void updateViewLanguage(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int count = vg.getChildCount();
            for (int i = 0; i < count; i++) {
                updateViewLanguage(vg.getChildAt(i));
            }
        } else if (view instanceof LanguageChangableView) {
            LanguageChangableView tv = (LanguageChangableView) view;
            tv.reLoadLanguage();
        }
    }

    /**
     * 刷新系统语言环境
     * @param mContext
     * @param locale
     */
   public static void refreshLanguageConfig(Context mContext,Locale locale){
       Configuration config = mContext.getResources().getConfiguration();// 获得设置对象
       Resources resources = mContext.getResources();// 获得res资源对象
       DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
       config.locale = locale; // 简体中文
       resources.updateConfiguration(config, dm);
   }

}
