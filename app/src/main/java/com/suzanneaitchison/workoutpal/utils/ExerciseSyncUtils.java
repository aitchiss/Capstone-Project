package com.suzanneaitchison.workoutpal.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.suzanneaitchison.workoutpal.data.ExerciseContract;
import com.suzanneaitchison.workoutpal.data.ExerciseSyncIntentService;

/**
 * Created by suzanne on 04/03/2018.
 */

public class ExerciseSyncUtils {

    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context){
        if (sInitialized) return;
        sInitialized = true;

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
}
