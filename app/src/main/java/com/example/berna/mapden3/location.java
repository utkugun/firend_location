package com.example.berna.mapden3;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;


import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;


public class location implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private List<Geofence> geofences = new LinkedList<>();
    PendingIntent pending;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private final Context mContext;
    userdata user=new userdata();

    public location(Context mContext) {
        this.mContext = mContext;
    }


    public void onCreate() {
        this.onCreate();

    }


    public void onStatusChanged(String s, int i, Bundle bundle) {

    }


    public void onProviderEnabled(String s) {

    }


    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        startLocationUpdates();

        setupGeofence();


        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        double lat = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude();
        double lon = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLongitude();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + user.firebaseuid() + "/location");
        myRef.child("lat").setValue(lat);
        myRef.child("long").setValue(lon);


        myRef.child("updatetime").setValue(java.util.Calendar.getInstance().getTime().toString());


    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }





    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, location.this);

    }



    public void onLocationChanged(Location location) {



       FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference myRef = database.getReference("users/" + user.firebaseuid() + "/location");
        myRef.child("lat").setValue(location.getLatitude());

        myRef.child("updatetime").setValue(java.util.Calendar.getInstance().getTime().toString());

        Log.d("Sample", "location updated");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void  apibuild(){

                 mGoogleApiClient = new GoogleApiClient.Builder(this.mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    public void setupGeofence() {

        if (!this.geofences.isEmpty()) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d("Sample", "setupGeofence");
        this.geofences.add(
                new Geofence.Builder()
                        .setRequestId("home")
                        .setCircularRegion(
                                39.992489,
                                32.7056663,
                                500
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build()
        );

        GeofencingRequest req = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT | GeofencingRequest.INITIAL_TRIGGER_DWELL)
                .addGeofences(this.geofences)
                .build();

        Intent intent = new Intent(mContext,  MyIntentService.class);
        pending = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        LocationServices.GeofencingApi.addGeofences(
                this.mGoogleApiClient,
                req,
                pending
        );


    }
}