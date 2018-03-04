package com.suzanneaitchison.workoutpal.utils;

import com.suzanneaitchison.workoutpal.models.Exercise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by suzanne on 04/03/2018.
 */

public class ExerciseJsonUtils {

    private static final String EXERCISE_ID = "id";
    private static final String EXERCISE_NAME = "name";
    private static final String EXERCISE_DESCRIPTION = "description";

    public static Exercise[] convertJsonToExercises(String jsonString) throws JSONException{
        JSONObject results = new JSONObject(jsonString);
//        JSONObject jsonExercisesObject = results.getJSONObject("results");
        JSONArray jsonExercises = results.getJSONArray("results");

        Exercise[] exercises = new Exercise[jsonExercises.length()];

        for (int i = 0; i < jsonExercises.length(); i++){
            JSONObject jsonObject = jsonExercises.getJSONObject(i);

            int id = jsonObject.getInt(EXERCISE_ID);
            String name = jsonObject.getString(EXERCISE_NAME);
            String description = jsonObject.getString(EXERCISE_DESCRIPTION);

            Exercise exercise = new Exercise();
            exercise.setId(id);
            exercise.setName(name);
            exercise.setDescription(description);
            exercises[i] = exercise;
        }
        return exercises;
    }

    public static Exercise updateExerciseWithImage(Exercise exercise, String jsonString) throws JSONException{
        JSONObject jsonDetail = new JSONObject(jsonString);
        if(jsonDetail.getInt("count") > 0){
            JSONArray images = jsonDetail.getJSONArray("results");
            exercise.setImageURL(images.getJSONObject(0).getString("image"));
        }
        return exercise;
    }

    public static Exercise updateExerciseWithCategory(Exercise exercise, String jsonString) throws JSONException{
        JSONObject jsonDetail = new JSONObject(jsonString);
        JSONObject categoryObject = jsonDetail.getJSONObject("category");
        exercise.setExerciseCategory(categoryObject.getString("name"));
        return exercise;
    }
}
