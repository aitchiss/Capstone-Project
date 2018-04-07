package com.suzanneaitchison.workoutpal.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.suzanneaitchison.workoutpal.data.ExerciseContract;
import com.suzanneaitchison.workoutpal.data.ExerciseFirebaseJobService;
import com.suzanneaitchison.workoutpal.data.ExerciseSyncIntentService;

import java.util.concurrent.TimeUnit;

/**
 * Created by suzanne on 04/03/2018.
 */

public class ExerciseSyncUtils {

//      Sync exercises every week
    private static final int SYNC_INTERVAL_DAYS = 7;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.DAYS.toSeconds(SYNC_INTERVAL_DAYS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static final String EXERCISE_SYNC_TAG = "exercise-sync";

    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context){
        if (sInitialized) return;
        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {

                Cursor cursor = context.getContentResolver().query(
                        ExerciseContract.ExerciseEntry.CONTENT_URI,
                        null, null, null, null);

                if(cursor == null || cursor.getCount() == 0){
                    startImmediateSync(context);
                }

                cursor.close();
                return null;
            }
        }.execute();
    }

    public static void startImmediateSync(@NonNull final Context context){
        Intent intentToSyncImmediately = new Intent(context, ExerciseSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context){
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job syncExerciseJob = dispatcher.newJobBuilder()
                .setService(ExerciseFirebaseJobService.class)
                .setTag(EXERCISE_SYNC_TAG)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS, SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncExerciseJob);
    }
}
