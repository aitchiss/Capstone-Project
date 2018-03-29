package com.suzanneaitchison.workoutpal.models;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by suzanne on 29/03/2018.
 */

public class Achievement {

    private int exerciseId;
    private Date achievementDate;
    private int weight;
    private String exerciseName;

    public Achievement(int exerciseId, int weight, String name){
        this.achievementDate = Calendar.getInstance().getTime();
        this.exerciseId = exerciseId;
        this.weight = weight;
        this.exerciseName = name;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Date getAchievementDate() {
        return achievementDate;
    }

    public void setAchievementDate(Date achievmentDate) {
        this.achievementDate = achievmentDate;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
}
