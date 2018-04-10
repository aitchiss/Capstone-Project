package com.suzanneaitchison.workoutpal.data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

/**
 * Created by suzanne on 04/03/2018.
 */

public class ExerciseSyncIntentService extends IntentService {

    public ExerciseSyncIntentService(){
        super("ExerciseSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo.isConnectedOrConnecting()){
            ExerciseSyncTask.syncExercises(this);
        }
    }
}
