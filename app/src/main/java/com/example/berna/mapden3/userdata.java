package com.example.berna.mapden3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by berna on 16.07.2017.
 */

public class userdata {

    public String firebaseuser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            String name = user.getDisplayName();

            return name;
        } else {
            return "boş";
        }
    }

    public String firebaseuid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        return uid;
    }

    public String firebasefoto() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String foto = user.getPhotoUrl().toString();
        return foto;
    }
    

     }

