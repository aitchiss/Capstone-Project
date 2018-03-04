package com.suzanneaitchison.workoutpal.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.suzanneaitchison.workoutpal.models.Exercise;

import java.util.ArrayList;

/**
 * Created by suzanne on 04/03/2018.
 */

public class FirebaseDatabaseHelper {

    public static void replaceAllExercises(ArrayList<Exercise> exercises){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("exercises");

        Gson gson = new Gson();
        String jsonExercises = gson.toJson(exercises);
        ref.setValue(jsonExercises);
    }
}
