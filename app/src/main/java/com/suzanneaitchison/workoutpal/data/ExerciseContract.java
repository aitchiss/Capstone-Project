package com.suzanneaitchison.workoutpal.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by suzanne on 31/03/2018.
 */

public class ExerciseContract {

    public static final String AUTHORITY = "com.suzanneaitchison.workoutpal";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_EXERCISES = "exercises";

    public static final class ExerciseEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISES).build();

        public static final String TABLE_NAME ="exercises";
        public static final String COLUMN_EXERCISE_ID = "exerciseId";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
        public static final String COLUMN_CATEGORY = "category";
    }
}
