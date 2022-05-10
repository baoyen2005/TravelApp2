package com.example.travelapp.broadcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.travelapp.R;
import com.example.travelapp.view.activity.home.MainActivityUser;

public class NotificationService  extends BroadcastReceiver {
    private final String CHANNEL_ID ="Notification_post";
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId",0);
        String message = "You have new notification about travel to: "
                            + intent.getStringExtra("message");
        Intent mainIntent = new Intent(context, MainActivityUser.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,mainIntent,0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            CharSequence channelName = "Notification of travel app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID);
        builder.setContentTitle("Notification of travel app");
        builder.setContentIntent(contentIntent);
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.logo_foreground);
        builder.setAutoCancel( true ) ;

        // Notify
        notificationManager.notify(notificationId, builder.build());
    }
}
