package com.example.berna.mapden3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class message extends FirebaseMessagingService {
    public message() {
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        Log.d("From: ", "mes"+remoteMessage.getNotification().getBody());
              addNotification(remoteMessage.getNotification().getBody());
    }
    private void addNotification(String messagebody) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)

                        .setContentTitle("Notifications Example")
                        .setContentText(messagebody)
                         .setOnlyAlertOnce(true);

        Intent notificationIntent = new Intent(this, MapsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
