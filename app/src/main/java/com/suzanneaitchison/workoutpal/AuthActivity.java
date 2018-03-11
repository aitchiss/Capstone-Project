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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.suzanneaitchison.workoutpal.data.ExerciseDataFetcher;
import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.utils.ExerciseSyncUtils;

import java.io.IOException;
import java.util.Arrays;

import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    private static final int RC_SIGN_IN = 123;


    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        if(FirebaseDatabaseHelper.getAllExercises() != null && FirebaseDatabaseHelper.getAllExercises().size() == 0){
////            If the database of exercises is empty, complete the sync
//            ExerciseSyncUtils.startImmediateSync(this);
//        }
//        TODO - only want to do a full immediate sync if it's their first use of the app - move to new user block

        boolean authenticatedUserExists = FirebaseDatabaseHelper.checkForAuthenticatedUser();
        if(authenticatedUserExists){
            FirebaseDatabaseHelper.listenForUser();
            FirebaseDatabaseHelper.listenForExercises();
        }

        if(FirebaseDatabaseHelper.getUser() != null){
            Intent intent = new Intent(this, WorkoutListActivity.class);
            startActivity(intent);
        } else {
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
//          Check if the user already exists in the database
                FirebaseDatabaseHelper.listenForUser();
                FirebaseDatabaseHelper.listenForExercises();

                if(FirebaseDatabaseHelper.getUser() == null){
//                    Create the user in the database
                    FirebaseDatabaseHelper.createNewUser();
                }

                Intent intent = new Intent(this, WorkoutListActivity.class);
                startActivity(intent);
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


}
