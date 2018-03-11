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

    public static int getRestTimeInSeconds(String restText){
        if(restText.equals("None")){
            return 0;
        } else if (restText.equals("30 seconds")){
            return 30;
        } else if(restText.equals("1 minute")){
            return 60;
        } else if(restText.equals("2 minutes")){
            return 120;
        } else if (restText.equals("3 minutes")){
            return 180;
        } else if (restText.equals("4 minutes")){
            return 240;
        } else if(restText.equals("5 minutes")){
            return 300;
        } else {
            return 0;
        }
    }

}
