package com.suzanneaitchison.workoutpal.models;

/**
 * Created by suzanne on 05/03/2018.
 */

public class PlannedExercise extends Exercise {

    private int weight;
    private int duration;
    private int reps;
    private int restTime;

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
}
