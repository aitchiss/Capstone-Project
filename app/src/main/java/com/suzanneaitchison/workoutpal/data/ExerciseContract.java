package com.suzanneaitchison.workoutpal.data;

import android.provider.BaseColumns;

/**
 * Created by suzanne on 31/03/2018.
 */

public class ExerciseContract {

    public static final class ExerciseEntry implements BaseColumns {

        public static final String TABLE_NAME ="exercises";
        public static final String COLUMN_EXERCISE_ID = "exerciseId";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_URL = "imageUrl";
        public static final String COLUMN_CATEGORY = "category";
    }
}
