package com.suzanneaitchison.workoutpal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by suzanne on 05/03/2018.
 */

public class User {

    private String email;
    private String id;


    private ArrayList<Workout> workoutPlans = new ArrayList<>();
    private ArrayList<PlannedExercise> completedExercises = new ArrayList<>();

    public User(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Workout> getWorkoutPlans() {
        return workoutPlans;
    }

    public void setWorkoutPlans(ArrayList<Workout> workoutPlans) {
        this.workoutPlans = workoutPlans;
    }

    public ArrayList<PlannedExercise> getCompletedExercises() {
        return completedExercises;
    }

    public void setCompletedWorkouts(ArrayList<PlannedExercise> completedExercises) {
        this.completedExercises = completedExercises;
    }

    public void addCompletedExercise(PlannedExercise exercise){
        this.completedExercises.add(exercise);
    }

    public int addNewWorkoutPlan(Workout workout){
        workoutPlans.add(workout);
        return workoutPlans.indexOf(workout);
    }

}
