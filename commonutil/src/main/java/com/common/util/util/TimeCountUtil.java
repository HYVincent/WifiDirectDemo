package com.common.util.util;

import android.util.Log;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Administrator QQ:1032006226
 * @version v1.0
 * @name EasyApp
 * @page com.common.util
 * @class describe
 * @date 2018/5/14 23:56
 */
public class TimeCountUtil {

    private static ScheduledExecutorService scheduledExecutorService;
    private static final String TAG = "计时器";
    /**
     * 计时器，定时任务 这个比较完美，并且在任务时间大于定时时间的时候也能正常执行
     * @param initValues 初始值，
     * @param delayTime 延迟时间  延迟多少毫秒开始执行
     * @param interval 间隔时间，间隔多少时间执行一次
     * @param threadNum 线程池的数量
     * @param timeListener 监听器
     */
    public static void startTime(int initValues,long delayTime,long interval,  int threadNum, final TimeListener timeListener) {
        Log.d(TAG, "startTime: 开始计时..");
        final AtomicInteger atomicInteger = new AtomicInteger(initValues);
        if (threadNum == 0) {
            threadNum = 1;
        }
        scheduledExecutorService = new ScheduledThreadPoolExecutor(threadNum);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                timeListener.doAction(atomicInteger.incrementAndGet());
//                timeListener.doAction(1);
            }
        }, delayTime, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止计时
     */
    public static void cancelTimeTask(){
        if(scheduledExecutorService!= null){
            scheduledExecutorService.shutdownNow();
        }
    }

    public interface TimeListener{
        void doAction(int index);
    }

}
