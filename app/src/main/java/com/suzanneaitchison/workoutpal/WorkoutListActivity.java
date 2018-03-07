package com.suzanneaitchison.workoutpal;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.suzanneaitchison.workoutpal.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mUsersRef = mRootRef.child("users");
    private User mCurrentUser;


    @BindView(R.id.nav_drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.nav_view)
    NavigationView mNavView;

    TextView mNavHeaderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mAuth = FirebaseAuth.getInstance();

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);

        retrieveUserFromFirebase();
        View mNavHeaderView = mNavView.getHeaderView(0);
        mNavHeaderText = (TextView) mNavHeaderView.findViewById(R.id.nav_drawer_header_text);


    }

    private void retrieveUserFromFirebase(){
        FirebaseUser user = mAuth.getCurrentUser();
        Query userQueryRef = mUsersRef.orderByChild("email").equalTo(user.getEmail());
        userQueryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    mCurrentUser = userSnapshot.getValue(User.class);
                    setNavDrawerTitle(mCurrentUser.getEmail());
//                    TODO - call the function that updates the list view with the users workouts
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //todo - show some sort of error
            }
        });

    }

    private void setNavDrawerTitle(String userEmail){
        mNavHeaderText.setText("You're signed in as " + userEmail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNewWorkoutButtonClick(View view){
        Intent intent = new Intent(this, WorkoutDetailActivity.class);
        intent.putExtra(WorkoutDetailActivity.WORKOUT_ID_EXTRA, -1);
        startActivity(intent);
    }
}
