package com.suzanneaitchison.workoutpal.utils;

import android.database.Cursor;

import com.suzanneaitchison.workoutpal.data.ExerciseContract;
import com.suzanneaitchison.workoutpal.models.Exercise;

import java.util.ArrayList;

/**
 * Created by suzanne on 07/04/2018.
 */

public class ExerciseCursorUtils {



    public static ArrayList<Exercise> convertCursorToExercises(Cursor cursor){
        ArrayList<Exercise> exercises = new ArrayList<>();
        if(cursor != null){

            int idIndex = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_EXERCISE_ID);
            int nameIndex = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME);
            int descriptionIndex = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_DESCRIPTION);
            int imageIndex = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_IMAGE_URL);
            int categoryIndex = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_CATEGORY);

            cursor.moveToFirst();
            while(cursor.moveToNext()){
                Exercise exercise = new Exercise();
                exercise.setId(cursor.getInt(idIndex));
                exercise.setName(cursor.getString(nameIndex));
                exercise.setDescription(cursor.getString(descriptionIndex));
                exercise.setImageURL(cursor.getString(imageIndex));
                exercise.setExerciseCategory(cursor.getString(categoryIndex));
                exercises.add(exercise);
            }
        }

        return exercises;
    }
}
