package com.suzanneaitchison.workoutpal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by suzanne on 05/03/2018.
 */

public class User {

    private String email;
    private String id;
    private HashMap<Integer, Achievement> achievements = new HashMap<>();
    private ArrayList<Achievement> achievementList = new ArrayList<>();


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

        if(exercise.getWeight() > 0){
//            If a weighted exercise, record the achievement if needed
            updateAchievement(exercise);
        }
    }

    public int addNewWorkoutPlan(Workout workout){
        workoutPlans.add(workout);
        return workoutPlans.indexOf(workout);
    }

    private void updateAchievement(PlannedExercise completedExercise){
        if(achievements.containsKey(completedExercise.getId())){
//            Update the achievement if necessary
            Achievement previousBest = achievements.get(completedExercise.getId());
            if(completedExercise.getWeight() > previousBest.getWeight()){
                Achievement newBest = new Achievement(completedExercise.getId(), completedExercise.getWeight(), completedExercise.getName());
                achievements.put(completedExercise.getId(), newBest);
            }
        } else {
//            It's the first time it's been achieved, so log this is a best
            Achievement newAchievement = new Achievement(completedExercise.getId(), completedExercise.getWeight(), completedExercise.getName());
            achievements.put(completedExercise.getId(), newAchievement);
        }

        achievementList.clear();
        for(Achievement item : achievements.values()){
            achievementList.add(item);
        }
    }

    public ArrayList<Achievement> getAllAchievements() {
        return achievementList;
    }

    public void setAchievementList(ArrayList<Achievement> list){
        this.achievementList = list;
    }
}
