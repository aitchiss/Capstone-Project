package com.suzanneaitchison.workoutpal.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by suzanne on 31/03/2018.
 */

public class ExerciseDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "exercises.db";
    private static final int DATABASE_VERSION = 1;

    public ExerciseDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EXERCISES_TABLE = "CREATE TABLE " + ExerciseContract.ExerciseEntry.TABLE_NAME + " (" +
                ExerciseContract.ExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ExerciseContract.ExerciseEntry.COLUMN_EXERCISE_ID + " INTEGER NOT NULL," +
                ExerciseContract.ExerciseEntry.COLUMN_NAME + " TEXT NOT NULL," +
                ExerciseContract.ExerciseEntry.COLUMN_DESCRIPTION + " TEXT," +
                ExerciseContract.ExerciseEntry.COLUMN_IMAGE_URL + " TEXT," +
                ExerciseContract.ExerciseEntry.COLUMN_CATEGORY + " TEXT" +
                ");";

        db.execSQL(SQL_CREATE_EXERCISES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseContract.ExerciseEntry.TABLE_NAME);
        onCreate(db);
    }
}
