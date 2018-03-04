package com.suzanneaitchison.workoutpal.data;

import android.content.Context;
import android.util.Log;

import com.suzanneaitchison.workoutpal.models.Exercise;
import com.suzanneaitchison.workoutpal.utils.ExerciseJsonUtils;

import org.json.JSONException;

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
            try {
                Exercise[] exercises = ExerciseJsonUtils.convertJsonToExercises(result);
                for (Exercise exercise : exercises){
                    String exerciseImageDetail = dataFetcher.fetchExerciseImage(context, exercise.getId());
                    ExerciseJsonUtils.updateExerciseWithImage(exercise, exerciseImageDetail);
                    if (exercise.getImageURL() != null){
                        Log.d("exercise image", exercise.getImageURL());
                    }

                }
            } catch (JSONException e){
                e.printStackTrace();
            }

//            todo - do something with the result - e.g. delete old exercise data and add the new
        } catch (IOException e){
            e.printStackTrace();
        }

//        todo image url and muscle groups need to be fetched separately
//        Create the Exercise Objects
//        For each object, fetch the additional data
//        Save everything to the database
    }
}
