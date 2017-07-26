package com.example.berna.mapden3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.location.LocationRequest;

public class MyService1 extends Service{

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Override
    public int onStartCommand (Intent intent, int flags, int startId){





        location loc=new location(this);
        loc.apibuild();
        loc.createLocationRequest();
        loc.mLocationRequest= LocationRequest.create();
        loc.mGoogleApiClient.connect();


return START_STICKY;



    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



}
