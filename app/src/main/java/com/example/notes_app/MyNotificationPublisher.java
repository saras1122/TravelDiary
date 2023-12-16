package com.example.notes_app;

import static com.example.notes_app.Message.NOTIFICATION_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyNotificationPublisher extends BroadcastReceiver {
    public static final int notificationID = 1;
    public static final String channelID = "channel1";
    public static final String titleExtra = "titleExtra";
    public static final String messageExtra = "messageExtra";
    public void onReceive (Context context , Intent intent) {
        Log.d("hi","");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel1")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Look where you are one year ago")
                .setVibrate(null);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(notificationID, builder.build());
        }

//
//        Log.d("time","");
//        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
//        assert notificationManager != null;
//        notificationManager.notify(id, notification);
    }
}
