package com.suzanneaitchison.workoutpal.data;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.suzanneaitchison.workoutpal.models.Exercise;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.utils.ExerciseJsonUtils;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by suzanne on 04/03/2018.
 */

public class FirebaseDatabaseHelper {

    private static ArrayList<Exercise> mExercises;

    public static void replaceAllExercises(ArrayList<Exercise> exercises){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("exercises");

        Gson gson = new Gson();
        String jsonExercises = gson.toJson(exercises);
        ref.setValue(jsonExercises);
    }

    public static ArrayList<Exercise> getAllExercises(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("exercises");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String exerciseInfo = dataSnapshot.getValue(String.class);
                try {
                    mExercises = ExerciseJsonUtils.convertFirebaseExerciseString(exerciseInfo);
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return mExercises;
    }


}