package com.suzanneaitchison.workoutpal.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.suzanneaitchison.workoutpal.data.ExerciseSyncIntentService;

/**
 * Created by suzanne on 04/03/2018.
 */

public class ExerciseSyncUtils {

    public static void startImmediateSync(@NonNull final Context context){
        Intent intentToSyncImmediately = new Intent(context, ExerciseSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
