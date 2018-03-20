package com.suzanneaitchison.workoutpal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by suzanne on 20/03/2018.
 */

public class TabDataLog implements Parcelable {

    ArrayList<ArrayList<PlannedExercise>> tabData;

    public TabDataLog(ArrayList<ArrayList<PlannedExercise>> data){
        tabData = data;
    }

    private TabDataLog(Parcel parcel){
        tabData = parcel.readArrayList(PlannedExercise.class.getClassLoader());
    }

    public ArrayList<ArrayList<PlannedExercise>> getTabData(){
        return tabData;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(tabData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TabDataLog> CREATOR = new Creator<TabDataLog>() {
        @Override
        public TabDataLog createFromParcel(Parcel in) {
            return new TabDataLog(in);
        }

        @Override
        public TabDataLog[] newArray(int size) {
            return new TabDataLog[size];
        }
    };
}
