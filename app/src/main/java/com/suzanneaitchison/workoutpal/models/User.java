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
    private ArrayList<Workout> completedWorkouts = new ArrayList<>();

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

    public ArrayList<Workout> getCompletedWorkouts() {
        return completedWorkouts;
    }

    public void setCompletedWorkouts(ArrayList<Workout> completedWorkouts) {
        this.completedWorkouts = completedWorkouts;
    }

    public int addNewWorkoutPlan(Workout workout){
        workoutPlans.add(workout);
        return workoutPlans.indexOf(workout);
    }

}
