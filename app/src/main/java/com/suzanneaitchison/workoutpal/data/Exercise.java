package com.suzanneaitchison.workoutpal.data;

/**
 * Created by suzanne on 03/03/2018.
 */

public class Exercise {

    private int mId;
    private String mName;
    private String mDescription;
    private String mImageURL;
    private String[] mMuscleGroups;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmImageURL() {
        return mImageURL;
    }

    public void setmImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public String[] getmMuscleGroups() {
        return mMuscleGroups;
    }

    public void setmMuscleGroups(String[] mMuscleGroups) {
        this.mMuscleGroups = mMuscleGroups;
    }
}
