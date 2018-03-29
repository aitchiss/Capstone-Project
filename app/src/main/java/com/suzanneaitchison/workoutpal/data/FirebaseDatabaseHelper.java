package com.suzanneaitchison.workoutpal.data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.suzanneaitchison.workoutpal.AuthActivity;
import com.suzanneaitchison.workoutpal.models.Exercise;
import com.suzanneaitchison.workoutpal.models.PlannedExercise;
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

    private static final int RC_SIGN_IN = 123;

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

    public static Exercise getExerciseWithId(int id){
        for(Exercise exercise : mExercises){
            if(exercise.getId() == id){
                return exercise;
            }
        }
        return null;
    }

    public static ArrayList<Exercise> getAllExercises(){
        return mExercises;
    }

    public static void listenForUser(final Context context, final Intent intent){
        FirebaseUser user = mAuth.getCurrentUser();
        Query userQueryRef = mUsersRef.orderByChild("email").equalTo(user.getEmail());

        userQueryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
//                        There should only be one user in the list returned
                    mCurrentUser = userSnapshot.getValue(User.class);
                }

                if(mCurrentUser != null){
                    context.startActivity(intent);
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

    public static void saveUsersCompletedExercises(ArrayList<PlannedExercise> updatedExercises){
        mCurrentUser.setCompletedWorkouts(updatedExercises);
        DatabaseReference userToUpdateRef = mUsersRef.child(mCurrentUser.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("completedExercises", updatedExercises);
        updates.put("achievements", mCurrentUser.getAllAchievements());
        userToUpdateRef.updateChildren(updates);
    }

    public static void signUserOut(final Context context){
        mCurrentUser = null;
        mCurrentUserRef = null;
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(context, AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                });
    }



}
