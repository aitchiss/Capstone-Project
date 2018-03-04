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
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by suzanne on 04/03/2018.
 */

public class ExerciseSyncTask {


    synchronized public static void syncExercises(Context context) {
        int pageNumber = 1;
        boolean continueLoading = true;
        ExerciseDataFetcher dataFetcher = new ExerciseDataFetcher();
        ArrayList<Exercise> exercises = new ArrayList<>();
        int currentIndex = 0;

        while(continueLoading){
            String result = "";
            try {
                result = dataFetcher.fetchLatestApiData(context, pageNumber);
                Log.d("result", result);
                try {
                    Collections.addAll(exercises, ExerciseJsonUtils.convertJsonToExercises(result));
                    for (int i = currentIndex; i < exercises.size(); i++) {
                        String exerciseImageDetail = dataFetcher.fetchExerciseImage(context, exercises.get(i).getId());
                        ExerciseJsonUtils.updateExerciseWithImage(exercises.get(i), exerciseImageDetail);
                        String exerciseCategoryDetail = dataFetcher.fetchExerciseCategory(context, exercises.get(i).getId());
                        ExerciseJsonUtils.updateExerciseWithCategory(exercises.get(i), exerciseCategoryDetail);

                    }
                    currentIndex = exercises.size() - 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try{
                    continueLoading = ExerciseJsonUtils.isAnotherPage(result);

                } catch (JSONException e1){
                    e1.printStackTrace();
                }
                pageNumber++;


            } catch (IOException e) {
                e.printStackTrace();
                continueLoading = false;
            }


        }
        if(exercises.size() > 0){
//            If exercises have synced, replace the exercises DB with the full results
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("exercises");

            Gson gson = new Gson();
            String jsonExercises = gson.toJson(exercises);
            ref.setValue(jsonExercises);


        }
    }


}
