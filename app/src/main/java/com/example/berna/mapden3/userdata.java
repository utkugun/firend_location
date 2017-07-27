package com.example.berna.mapden3;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    
    
     public Bitmap getfoto() {
          String urldisplay = firebasefoto();
          Bitmap mIcon11 = null;
          try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
          } catch (Exception e) {
              Log.e("Error", e.getMessage());
              e.printStackTrace();
          }
          return mIcon11;
      }

    
    
}
