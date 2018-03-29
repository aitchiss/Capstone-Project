package com.suzanneaitchison.workoutpal.models;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by suzanne on 29/03/2018.
 */

public class Achievement {

    private int exerciseId;
    private Date achievmentDate;
    private int weight;

    public Achievement(int exerciseId, int weight){
        this.achievmentDate = Calendar.getInstance().getTime();
        this.exerciseId = exerciseId;
        this.weight = weight;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Date getAchievmentDate() {
        return achievmentDate;
    }

    public void setAchievmentDate(Date achievmentDate) {
        this.achievmentDate = achievmentDate;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
