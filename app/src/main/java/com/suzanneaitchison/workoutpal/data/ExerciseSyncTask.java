package com.suzanneaitchison.workoutpal.data;

import android.content.ContentValues;
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
import java.lang.reflect.Array;
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

        while(continueLoading){
            String result = "";
            try {
                result = dataFetcher.fetchLatestApiData(context, pageNumber);
                try {
                    ArrayList<Exercise> exercises = new ArrayList<>();
                    Collections.addAll(exercises, ExerciseJsonUtils.convertJsonToExercises(result));
                    for (int i = 0; i < exercises.size(); i++) {
                        String exerciseImageDetail = dataFetcher.fetchExerciseImage(context, exercises.get(i).getId());
                        ExerciseJsonUtils.updateExerciseWithImage(exercises.get(i), exerciseImageDetail);
                        String exerciseCategoryDetail = dataFetcher.fetchExerciseCategory(context, exercises.get(i).getId());
                        ExerciseJsonUtils.updateExerciseWithCategory(exercises.get(i), exerciseCategoryDetail);
                    }

                    if(exercises.size() > 0){
                        ContentValues[] contentValues = new ContentValues[exercises.size()];
                        for(Exercise exercise : exercises){
                            ContentValues values = new ContentValues();
                            values.put(ExerciseContract.ExerciseEntry.COLUMN_EXERCISE_ID, exercise.getId());
                            values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME, exercise.getName());
                            values.put(ExerciseContract.ExerciseEntry.COLUMN_DESCRIPTION, exercise.getDescription());
                            values.put(ExerciseContract.ExerciseEntry.COLUMN_IMAGE_URL, exercise.getImageURL());
                            values.put(ExerciseContract.ExerciseEntry.COLUMN_CATEGORY, exercise.getExerciseCategory());
                            contentValues[exercises.indexOf(exercise)] = values;
                        }

                        int enteredToDb = context.getContentResolver().bulkInsert(ExerciseContract.ExerciseEntry.CONTENT_URI, contentValues);
                        Log.d("Inserted: ", enteredToDb + " records");
                    }

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

    }


}
