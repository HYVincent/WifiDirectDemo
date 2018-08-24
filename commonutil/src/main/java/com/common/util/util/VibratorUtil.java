package com.common.util.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;

import com.common.util.CommonUtil;


/**
 * @author Administrator QQ:1032006226
 * @version v1.0
 * @name StarKangMedical_Android
 * @page com.vincent.mylibrary.util.voice
 * @class describe 震动工具类
 * @date 2018/5/15 14:28
 */
public class VibratorUtil {

    private static Vibrator vibrator;

    /**
     * 开始振动
     * @param vibratorTime 持续时间
     * @param amplitude 震动强度  范围值为0-255之间
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void startVibrator(long vibratorTime, int amplitude){
        if(vibrator == null){
            vibrator = (Vibrator) CommonUtil.getInstance().getmContext().getSystemService(Context.VIBRATOR_SERVICE);
        }
        if(Build.VERSION.SDK_INT >25){
            VibrationEffect vibrationEffect = VibrationEffect.createOneShot(vibratorTime,amplitude);
            vibrator.vibrate(vibrationEffect);
        }else {
            vibrator.vibrate(vibratorTime);
        }
    }

}
