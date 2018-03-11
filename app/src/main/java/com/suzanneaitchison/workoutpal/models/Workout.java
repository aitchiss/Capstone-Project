package com.suzanneaitchison.workoutpal.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by suzanne on 05/03/2018.
 */

public class Workout {

    private String workoutName;

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    private Date completedDate;
    private ArrayList<PlannedExercise> plannedExercises;

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public ArrayList<PlannedExercise> getPlannedExercises() {
        return plannedExercises;
    }

    public void setPlannedExercises(ArrayList<PlannedExercise> plannedExercises) {
        this.plannedExercises = plannedExercises;
    }
}
