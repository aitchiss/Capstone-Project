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

    private ArrayList<WorkoutEntry> workoutEntries;

    public ArrayList<WorkoutEntry> getWorkoutEntries() {
        return workoutEntries;
    }

    public void setWorkoutEntries(ArrayList<WorkoutEntry> workoutEntries) {
        this.workoutEntries = workoutEntries;
    }

    public void addWorkoutEntry(WorkoutEntry entry){
        if(this.workoutEntries == null){
            this.workoutEntries = new ArrayList<>();
        }
        this.workoutEntries.add(entry);
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

}
