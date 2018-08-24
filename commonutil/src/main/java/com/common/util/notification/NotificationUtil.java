package com.common.util.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import okhttp3.HttpUrl;

/**
 * @author Administrator Vincent
 * @version v1.0
 * @name EasyApp
 * @page com.common.util.notification
 * @class describe
 * @date 2018/6/23 0:06
 */

public class NotificationUtil {

    /**
     * 普通通知栏
     * @param mContext
     * @param title
     * @param centent
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void normarNotification(Context mContext, String title, String centent, String url,int smallIconId, int largeIconId){
        Notification.Builder builder = new Notification.Builder(mContext);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(smallIconId);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),largeIconId));
        builder.setAutoCancel(true);
        builder.setContentTitle(title);
        builder.setContentText(centent);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int)(System.currentTimeMillis()/2),builder.build());
    }



}
