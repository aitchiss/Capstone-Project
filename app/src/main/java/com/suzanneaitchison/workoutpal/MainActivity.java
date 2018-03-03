package com.suzanneaitchison.workoutpal;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.suzanneaitchison.workoutpal.data.ExerciseDataFetcher;

import java.io.IOException;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 123;

//    Todo - make the UI for this page a splashscreen replaced by the UI activity for result?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);

        fetchData();


        if(mAuth.getCurrentUser() != null){

        } else {
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    RC_SIGN_IN);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
//                TODO start the signed in activity
                finish();
            } else {
                if(response == null){
//                    TODO handle user cancelled/tapped back
                    return;
                }
                if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
//                    TODO show a snackbar saying no internet connection
                    return;
                }
//                TODO show a snackbar saying there's been an unknown error
                Log.e(TAG, "Sign in error: ", response.getError());
            }
        }
    }


    private void fetchData(){
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader loader = loaderManager.getLoader(100);
        if(loader == null){
            loaderManager.initLoader(100, null, this);
        } else {
            loaderManager.restartLoader(100, null, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args){
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                ExerciseDataFetcher dataFetcher = new ExerciseDataFetcher();
                String result = "";
                try {
                    result = dataFetcher.fetchLatestApiData(MainActivity.this);
                } catch (IOException e){
                    e.printStackTrace();
                }
                return result;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
