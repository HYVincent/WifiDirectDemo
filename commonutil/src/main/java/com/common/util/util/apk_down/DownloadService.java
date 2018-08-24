package com.common.util.util.apk_down;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import com.common.util.R;

import java.io.File;

/**
 * @name MyUtils
 * @class name：com.vincent.example
 * @class describe
 * @author  Vincent QQ:1032006226
 * @time 2017年9月2日11:33:22
 * @change
 * @chang time
 * @class describe
 */

public class DownloadService extends Service {

    private static final String TAG = "下载服务";
    private DownloadTask downloadTask;
    private int notificationImgId;

    private String downloadUrl;

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            Log.d(TAG, "onProgress: 收到进度提示-->"+progress);
            getNotificationManager().notify(1, getNotification("正在下载星康医疗升级包..", notificationImgId,progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            // 下载成功时将前台服务通知关闭，并创建一个下载成功的通知
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Success",notificationImgId, -1));
            stopService(new Intent(DownloadService.this,DownloadService.class));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            // 下载失败时将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Failed", notificationImgId,-1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
        }

    };

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 使用流程
     *  在Fragment或者Activity的onCreate种初始化
     *
     *
     *
     */
    public  class DownloadBinder extends Binder {

        /**
         * 下载调用
         * @param url
         * @param notificationImgIds
         * @param notificationTitle
         */
        public void startDownload(String url,int notificationImgIds,String notificationTitle) {
            if (downloadTask == null) {
                downloadUrl = url;
                notificationImgId = notificationImgIds;
                downloadTask = new DownloadTask(DownloadService.this,listener);
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification(notificationTitle,notificationImgId, 0));
                Toast.makeText(DownloadService.this, "正在后台下载，下拉可查看进度", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            } else {
                if (downloadUrl != null) {
                    // 取消下载时需将文件删除，并将通知关闭
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title,int logoImgId, int progress) {
        Log.d(TAG, "getNotification: .........." + progress + "........");
        Intent intent = new Intent("toncentsoft.hengfuwei.ui.avtivity.LoginActivity");
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(notificationImgId);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), logoImgId));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress >= 0) {
            // 当progress大于或等于0时才需显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }
}
