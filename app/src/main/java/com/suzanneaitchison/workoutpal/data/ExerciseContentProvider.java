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

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ExerciseContract.AUTHORITY, ExerciseContract.PATH_EXERCISES, EXERCISES);
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
        return null;
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
