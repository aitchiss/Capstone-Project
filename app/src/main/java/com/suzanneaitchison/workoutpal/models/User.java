package com.suzanneaitchison.workoutpal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by suzanne on 05/03/2018.
 */

public class User {

    private String email;
    private String id;
    private HashMap<Integer, Achievement> achievements = new HashMap<>();
    private ArrayList<Achievement> achievementList = new ArrayList<>();
    private Date lastWorkoutCompletedDate;

    private ArrayList<Workout> workoutPlans = new ArrayList<>();
    private ArrayList<PlannedExercise> completedExercises = new ArrayList<>();

    public User(){

    }

    public Date getLastWorkoutCompletedDate() {
        return lastWorkoutCompletedDate;
    }

    public void setLastWorkoutCompletedDate(Date lastWorkoutCompletedDate) {
        this.lastWorkoutCompletedDate = lastWorkoutCompletedDate;
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
        this.lastWorkoutCompletedDate = Calendar.getInstance().getTime();

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

    public ArrayList<Achievement> getAchievementList() {
        return achievementList;
    }

    public void setAchievementList(ArrayList<Achievement> achievementList) {
        this.achievementList = achievementList;
    }

    public void updateAchievementMap(){
        if(achievementList != null && achievementList.size()> 0){
            for(Achievement achievement : achievementList){
                achievements.put(achievement.getExerciseId(), achievement);
            }
        }
    }
}
