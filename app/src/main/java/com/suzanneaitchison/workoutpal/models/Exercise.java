package com.suzanneaitchison.workoutpal.models;

/**
 * Created by suzanne on 03/03/2018.
 */

public class Exercise {

    private int mId;
    private String mName;
    private String mDescription;
    private String mImageURL;
    private String[] mMuscleGroups;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public String[] getMuscleGroups() {
        return mMuscleGroups;
    }

    public void setMuscleGroups(String[] mMuscleGroups) {
        this.mMuscleGroups = mMuscleGroups;
    }
}
