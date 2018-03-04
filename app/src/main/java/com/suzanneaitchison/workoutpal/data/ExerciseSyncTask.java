package com.suzanneaitchison.workoutpal.data;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

/**
 * Created by suzanne on 04/03/2018.
 */

public class ExerciseSyncTask {

    synchronized public static void syncExercises(Context context){
        ExerciseDataFetcher dataFetcher = new ExerciseDataFetcher();
        String result = "";
        try {
            result = dataFetcher.fetchLatestApiData(context);
            Log.d("results", result);
//            todo - do something with the result - e.g. delete old exercise data and add the new
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
