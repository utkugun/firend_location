package com.example.berna.mapden3;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener

{
    location loc = new location(this);
    private TextView text;
    private GoogleMap mMap;
    private Marker marker;
    public Marker[] friendmarker = new Marker[100];
    int i;
     userdata user =new userdata();
    HashMap<String, Marker> Hashmap = new HashMap();

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        text = findViewById(R.id.textView);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();






    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


    @Override
    public void onStart() {

        super.onStart();

try {

        mGoogleApiClient.connect();
} catch (Exception e) {

              e.printStackTrace();
          }
          
stopService(new Intent(MapsActivity.this, MyService1.class));



    }
    @Override
    public void onPause() {
    super.onPause();

    mGoogleApiClient.disconnect();

}

    public void onResume() {
        super.onResume();

        mGoogleApiClient.connect();


    }
    @Override
    public void onStop() {

        super.onStop();
        mGoogleApiClient.disconnect();
        startService(new Intent(MapsActivity.this, MyService1.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            util.scheduleJob(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(1.0, 1.0)));


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users/" + user.firebaseuid() + "/location");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                double mylat = (double) dataSnapshot.child("lat").getValue();
                double mylong = (double) dataSnapshot.child("long").getValue();
                LatLng latlng = new LatLng(mylat, mylong);


                marker.setPosition(latlng);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference myRef1 = database.getReference("users/" + user.firebaseuid() + "/friends");

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot child : dataSnapshot.getChildren())
                    if (child.getValue() != null) {


                        final DatabaseReference myRef2 = database.getReference("users");

                        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Double friendlat = (Double) dataSnapshot.child(child.getValue().toString() + "/location/lat").getValue();
                                Double friendlong = (Double) dataSnapshot.child(child.getValue().toString() + "/location/long").getValue();
                               String friendname= (String) dataSnapshot.child(child.getValue().toString()+"/name").getValue();
                               LatLng friendcurrentposition = new LatLng(friendlat, friendlong);


                                if (friendmarker[i] == null) {

                                    MarkerOptions a = new MarkerOptions().position(friendcurrentposition)
                                            .draggable(true).title(myRef2.getParent().toString())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                                            .title(friendname);

                                    friendmarker[i] = mMap.addMarker(a);
                                    Hashmap.put(child.getValue().toString(), friendmarker[i]);


                                    i++;

                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }


                        });


                        myRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Double friendlat = (Double) dataSnapshot.child(child.getValue().toString() + "/location/lat").getValue();
                                Double friendlong = (Double) dataSnapshot.child(child.getValue().toString() + "/location/long").getValue();
                                LatLng friendcurrentposition = new LatLng(friendlat, friendlong);

                                Hashmap.get(child.getValue().toString()).setPosition(friendcurrentposition);

                                mMap.addCircle(new CircleOptions().center(friendcurrentposition).radius(10).fillColor(Color.BLUE));

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        createLocationRequest();
        LocationRequest.create();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


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

            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }

            @Override
            public void onLocationChanged(Location mlocation) {

                text.setText(user.firebasefoto());
               // Toast.makeText(this,"kkk",Toast.LENGTH_LONG).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users/" + user.firebaseuid() + "/location");
                myRef.child("lat").setValue(mlocation.getLatitude());
                myRef.child("long").setValue(mlocation.getLongitude());

            }

            protected void createLocationRequest() {

               mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(5000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setSmallestDisplacement(10);

            }

        }
