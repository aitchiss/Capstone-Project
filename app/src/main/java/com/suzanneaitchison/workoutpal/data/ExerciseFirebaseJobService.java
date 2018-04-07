package com.suzanneaitchison.workoutpal.data;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by suzanne on 07/04/2018.
 */

public class ExerciseFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchExercisesTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mFetchExercisesTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                ExerciseSyncTask.syncExercises(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        };

        mFetchExercisesTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mFetchExercisesTask != null){
            mFetchExercisesTask.cancel(true);
        }
        return  true;
    }
}
