package com.example.berna.mapden3;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    public static final String TRANSITION_INTENT_SERVICE = "ReceiveTransitionsIntentService";
    public MyIntentService() {
        super(TRANSITION_INTENT_SERVICE);
    }

userdata user=new userdata();

    @Override
    protected void onHandleIntent(Intent intent) {



        GeofencingEvent e = GeofencingEvent.fromIntent(intent);
        if (e == null || e.getTriggeringLocation() == null) {
            Log.d("Sample", "GeofencingEvent#getTriggeringLocation is null");

        }

        String reason = "";
        if (e.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_ENTER) {
            reason = "EVE Girdi";
        } else if (e.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_EXIT) {
            reason = "Evden Çıktı";
        } else if (e.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_DWELL) {
            reason = "DWELL";
        }


        Log.d("Sample", reason + ": " + e.getTriggeringLocation().getLatitude() + ", " + e.getTriggeringLocation().getLongitude());
        // addNotification(reason);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users/" + user.firebaseuid() + "/home");
        myRef.setValue(reason);
    }
    private void addNotification(String messagebody) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)

                        .setContentTitle("Notifications Example")
                        .setContentText(messagebody)
                        .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.cast_ic_notification_small_icon);

        Intent notificationIntent = new Intent(this, MapsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

           }


}




