package com.example.if_dose;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import timber.log.Timber;

/**
 * Created by aaaa on 10/24/2017.
 */

public class OnetimeAlarmReceiver extends BroadcastReceiver {

    final int REQUEST_CODE = 123;

    @Override
    @RequiresApi(api = 16)
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = context.getString(R.string.notification_title);
        String notificationText = context.getString(R.string.notification_text);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Timber.i("Start notification after 4 Hours");


        NotificationCompat.Builder builder=new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_stat_drop)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher_5))

            .setAutoCancel(true)
            .setSound(alarmSound)
            .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(notificationText));
        Intent inten=new Intent(context,GlucoseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,inten,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());

    }
}
