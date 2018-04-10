package com.suzanneaitchison.workoutpal.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suzanne on 03/03/2018.
 */

public class Exercise implements Parcelable {

    private int id;
    private String name;
    private String description;
    private String imageURL;
    private String exerciseCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getExerciseCategory() {
        return exerciseCategory;
    }

    public void setExerciseCategory(String exerciseCategory) {
        this.exerciseCategory = exerciseCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.imageURL);
        dest.writeString(this.exerciseCategory);
    }

    public Exercise(Parcel parcel){
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.description = parcel.readString();
        this.imageURL = parcel.readString();
        this.exerciseCategory = parcel.readString();
    }

    public Exercise(){

    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel source) {
            return new Exercise(source);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}
