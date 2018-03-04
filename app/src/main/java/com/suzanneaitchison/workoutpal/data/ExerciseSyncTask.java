package com.suzanneaitchison.workoutpal.data;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.suzanneaitchison.workoutpal.models.Exercise;
import com.suzanneaitchison.workoutpal.utils.ExerciseJsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by suzanne on 04/03/2018.
 */

public class ExerciseSyncTask {


    synchronized public static void syncExercises(Context context) {
        ExerciseDataFetcher dataFetcher = new ExerciseDataFetcher();
        String result = "";
        try {
            result = dataFetcher.fetchLatestApiData(context);
            try {
                Exercise[] exercises = ExerciseJsonUtils.convertJsonToExercises(result);
                for (Exercise exercise : exercises) {
                    String exerciseImageDetail = dataFetcher.fetchExerciseImage(context, exercise.getId());
                    ExerciseJsonUtils.updateExerciseWithImage(exercise, exerciseImageDetail);
                    String exerciseCategoryDetail = dataFetcher.fetchExerciseCategory(context, exercise.getId());
                    ExerciseJsonUtils.updateExerciseWithCategory(exercise, exerciseCategoryDetail);

                }

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("exercises");

                Gson gson = new Gson();
                String jsonExercises = gson.toJson(exercises);
                ref.setValue(jsonExercises);
            } catch (JSONException e) {
                e.printStackTrace();
            }


//todo - check if we're on the final page of results, if not, keep going

//            todo - do something with the result - e.g. delete old exercise data and add the new
        } catch (IOException e) {
            e.printStackTrace();
        }

//        todo Save everything to the database
    }


}
