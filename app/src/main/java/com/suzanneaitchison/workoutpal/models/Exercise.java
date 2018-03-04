package com.suzanneaitchison.workoutpal.models;

/**
 * Created by suzanne on 03/03/2018.
 */

public class Exercise {

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
}
