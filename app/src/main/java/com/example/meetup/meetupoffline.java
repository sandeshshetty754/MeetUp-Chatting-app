package com.example.meetup;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class meetupoffline extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
