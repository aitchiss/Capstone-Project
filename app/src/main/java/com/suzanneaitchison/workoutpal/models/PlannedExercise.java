package com.suzanneaitchison.workoutpal.models;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by suzanne on 05/03/2018.
 */

public class PlannedExercise extends Exercise {

    private int weight;
    private int duration;
    private int reps;
    private int restTime;
    private boolean isComplete;
    private Date completedDate;

    public PlannedExercise(){
        isComplete = false;
    }

    public PlannedExercise(WorkoutEntry entry){
        isComplete = false;
        this.setId(entry.getExerciseId());
        this.setName(entry.getExerciseName());
        this.setReps(entry.getReps());
        this.setRestTime(entry.getRestTime());
        this.setDuration(entry.getDuration());
        this.setWeight(entry.getWeight());
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int seconds) {
        this.duration = seconds;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
        completedDate = Calendar.getInstance().getTime();
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
