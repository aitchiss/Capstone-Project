package com.suzanneaitchison.workoutpal;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;

import com.google.firebase.database.FirebaseDatabase;
import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.utils.ExerciseSyncUtils;

import java.util.Arrays;

import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        Starts a full sync of exercise data if DB is empty
        ExerciseSyncUtils.initialize(this);
        if(!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        if(mAuth.getCurrentUser() != null){
//            user is already signed in - no need to re-authenticate
//            set up the Helper with their details
            Intent intent = new Intent(this, MainActivity.class);
            FirebaseDatabaseHelper.listenForUser(this, intent);

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
//          Check if the user is new to the app - create their WorkoutPal DB if so
                FirebaseUserMetadata metadata = mAuth.getCurrentUser().getMetadata();
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()){
                    FirebaseDatabaseHelper.createNewUser();
                }
                Intent intent = new Intent(this, MainActivity.class);
                FirebaseDatabaseHelper.listenForUser(this, intent);

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
