package com.suzanneaitchison.workoutpal.data;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.suzanneaitchison.workoutpal.models.Exercise;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.models.Workout;
import com.suzanneaitchison.workoutpal.utils.ExerciseJsonUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suzanne on 04/03/2018.
 */

public class FirebaseDatabaseHelper {

    private static User mCurrentUser;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference mUsersRef = mRootRef.child("users");
    private static DatabaseReference mCurrentUserRef;

    private static ArrayList<Exercise> mExercises;



    public static void replaceAllExercises(ArrayList<Exercise> exercises){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("exercises");

        Gson gson = new Gson();
        String jsonExercises = gson.toJson(exercises);
        ref.setValue(jsonExercises);
    }

    public static void listenForExercises(){
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
    }

    public static ArrayList<Exercise> getAllExercises(){
        return mExercises;
    }

    public static boolean checkForAuthenticatedUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            return true;
        }
        return false;
    }

    public static void listenForUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        Query userQueryRef = mUsersRef.orderByChild("email").equalTo(user.getEmail());

        userQueryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
//                        There should only be one user in the list returned
                    mCurrentUser = userSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//todo - show some sort of error
            }
        });
    }

    public static User getUser(){
        return mCurrentUser;
    }

    public static void createNewUser() {
        String userEmail = mAuth.getCurrentUser().getEmail();
        mCurrentUser = new User();
        mCurrentUser.setEmail(userEmail);
        mCurrentUserRef = mUsersRef.push();
        mCurrentUserRef.setValue(mCurrentUser);
        String userId = mCurrentUserRef.getKey();
        mCurrentUser.setId(userId);
        mCurrentUserRef.setValue(mCurrentUser);
    }

    public static void saveUsersPlannedWorkouts(ArrayList<Workout> updatedPlannedWorkouts){
        mCurrentUser.setWorkoutPlans(updatedPlannedWorkouts);
        DatabaseReference userToUpdateRef = mUsersRef.child(mCurrentUser.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("workoutPlans", updatedPlannedWorkouts);
        userToUpdateRef.updateChildren(updates);
    }

}
