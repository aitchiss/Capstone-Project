package com.suzanneaitchison.workoutpal.utils;

import java.util.ArrayList;

/**
 * Created by suzanne on 11/03/2018.
 */

public class CustomiseExerciseSpinnerUtils {

    public static ArrayList<String> getRestTimeOptions(){
        ArrayList<String> options = new ArrayList<>();
        options.add("None");
        options.add("30 seconds");
        options.add("1 minute");
        options.add("2 minutes");
        options.add("3 minutes");
        options.add("4 minutes");
        options.add("5 minutes");
        return options;
    }

}
