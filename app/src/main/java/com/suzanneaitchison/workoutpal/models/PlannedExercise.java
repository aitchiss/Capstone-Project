package com.suzanneaitchison.workoutpal.models;

/**
 * Created by suzanne on 05/03/2018.
 */

public class PlannedExercise extends Exercise {

    private int weight;
    private int seconds;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
