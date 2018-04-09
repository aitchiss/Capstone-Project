package com.suzanneaitchison.workoutpal;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class WorkoutPalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
