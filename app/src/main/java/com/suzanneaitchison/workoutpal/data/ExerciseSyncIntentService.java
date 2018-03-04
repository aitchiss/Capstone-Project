package com.suzanneaitchison.workoutpal.data;

import android.app.IntentService;
import android.content.Intent;
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
        ExerciseSyncTask.syncExercises(this);
    }
}
