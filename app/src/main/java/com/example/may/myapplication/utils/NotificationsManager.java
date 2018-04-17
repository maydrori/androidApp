package com.example.may.myapplication.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.may.myapplication.R;
import com.example.may.myapplication.ViewWorkshop;

/**
 * Created by May on 4/14/2018.
 */

public class NotificationsManager {
    
    public void send(Context context) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), "notify_001");

        // TODO ??
        Intent ii = new Intent(context.getApplicationContext(), ViewWorkshop.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);

        // Notification style
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
//        bigText.bigText(verseurl);
        bigText.setBigContentTitle("Today's Bible Verse");
        bigText.setSummaryText("Text in detail");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }
}
