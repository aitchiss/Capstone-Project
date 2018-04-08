package com.suzanneaitchison.workoutpal.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.suzanneaitchison.workoutpal.data.ExerciseContract.ExerciseEntry.TABLE_NAME;

/**
 * Created by suzanne on 31/03/2018.
 */

public class ExerciseContentProvider extends ContentProvider {

    private ExerciseDbHelper mExerciseDbHelper;

    public static final int EXERCISES = 100;
    public static final int EXERCISE_WITH_ID = 101;
    public static final int EXERCISES_WITH_IDS = 102;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ExerciseContract.AUTHORITY, ExerciseContract.PATH_EXERCISES, EXERCISES);
        uriMatcher.addURI(ExerciseContract.AUTHORITY, ExerciseContract.PATH_EXERCISES + "/ids", EXERCISES_WITH_IDS);
        uriMatcher.addURI(ExerciseContract.AUTHORITY, ExerciseContract.PATH_EXERCISES + "/#", EXERCISE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mExerciseDbHelper = new ExerciseDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mExerciseDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        switch(match){
            case EXERCISES:
                returnCursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EXERCISE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String idSelection = ExerciseContract.ExerciseEntry.COLUMN_EXERCISE_ID + "=?";
                String[] mSelectionArgs = new String[]{id};
                returnCursor = db.query(TABLE_NAME, projection, idSelection, mSelectionArgs, null, null, sortOrder);
                break;
            case EXERCISES_WITH_IDS:
                String querySelection = ExerciseContract.ExerciseEntry.COLUMN_EXERCISE_ID + "=?";
                for(int i=1; i < selectionArgs.length; i++){
                    querySelection += " OR " + ExerciseContract.ExerciseEntry.COLUMN_EXERCISE_ID + "=?";
                }
                returnCursor = db.query(TABLE_NAME, projection, querySelection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mExerciseDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch(match){
            case EXERCISES:
                long id = db.insert(TABLE_NAME, null, values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(ExerciseContract.ExerciseEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mExerciseDbHelper.getWritableDatabase();
        switch(sUriMatcher.match(uri)){
            case EXERCISES:
                db.beginTransaction();
                int rowsInserted = 0;

                try {
                    for(ContentValues value : values){
                        long _id = db.insert(ExerciseContract.ExerciseEntry.TABLE_NAME, null, value);
                        if(_id != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
