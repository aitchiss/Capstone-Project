package com.suzanneaitchison.workoutpal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by suzanne on 05/03/2018.
 */

public class PlannedExercise extends Exercise implements Parcelable{

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

    private PlannedExercise(Parcel parcel){

        this.setId(parcel.readInt());
        this.setName(parcel.readString());
        this.setReps(parcel.readInt());
        this.setRestTime(parcel.readInt());
        this.setDuration(parcel.readInt());
        this.setWeight(parcel.readInt());
        boolean[] completeArray = parcel.createBooleanArray();
        this.isComplete = completeArray[0];
        this.completedDate = new Date(parcel.readLong());
        this.setDescription(parcel.readString());
        this.setImageURL(parcel.readString());
        this.setExerciseCategory(parcel.readString());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getId());
        dest.writeString(this.getName());
        dest.writeInt(this.getReps());
        dest.writeInt(this.getRestTime());
        dest.writeInt(this.getDuration());
        dest.writeInt(this.getWeight());
        boolean[] complete = {this.isComplete};
        dest.writeBooleanArray(complete);
        dest.writeLong(this.completedDate.getTime());
        dest.writeString(this.getDescription());
        dest.writeString(this.getImageURL());
        dest.writeString(this.getExerciseCategory());
    }


    public static final Creator<PlannedExercise> CREATOR = new Creator<PlannedExercise>() {
        @Override
        public PlannedExercise createFromParcel(Parcel in) {
            return new PlannedExercise(in);
        }

        @Override
        public PlannedExercise[] newArray(int size) {
            return new PlannedExercise[size];
        }

    };
}
