package com.suzanneaitchison.workoutpal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.models.Workout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutListActivity extends AppCompatActivity implements WorkoutListRecyclerAdapter.WorkoutClickHandler {


    private User mCurrentUser;

    private WorkoutListRecyclerAdapter mAdapter;

    @BindView(R.id.fab_add_workout) FloatingActionButton mFab;

    @BindView(R.id.nav_drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.nav_view)
    NavigationView mNavView;

    @BindView(R.id.recycler_view_workout_list)
    RecyclerView mWorkoutRecyclerView;

    TextView mNavHeaderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);

        setUpDrawerMenuListeners();

        mCurrentUser = FirebaseDatabaseHelper.getUser();
        View mNavHeaderView = mNavView.getHeaderView(0);
        mNavHeaderText = (TextView) mNavHeaderView.findViewById(R.id.nav_drawer_header_text);
        setNavDrawerTitle(mCurrentUser.getEmail());

//        Populate the view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, 1, false);
        mAdapter = new WorkoutListRecyclerAdapter(mCurrentUser.getWorkoutPlans(), this, this);
        mWorkoutRecyclerView.setLayoutManager(layoutManager);
        mWorkoutRecyclerView.setAdapter(mAdapter);
    }


    private void setNavDrawerTitle(String userEmail){
        mNavHeaderText.setText("You're signed in as " + userEmail);
    }

    private void setUpDrawerMenuListeners(){
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                switch(item.getItemId()){
                    case R.id.sign_out:
                        signUserOut();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
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
//      create the new workout, add it to the users arraylist, then pass the index through to the next activity
        Workout workout = new Workout();
        workout.setWorkoutName("New workout");
        int workoutIndex = mCurrentUser.addNewWorkoutPlan(workout);

        FirebaseDatabaseHelper.saveUsersPlannedWorkouts(mCurrentUser.getWorkoutPlans());
        startDetailActivityWithIndex(workoutIndex);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser = FirebaseDatabaseHelper.getUser();
        mAdapter.setWorkoutData(mCurrentUser.getWorkoutPlans());
    }

    @Override
    public void onClick(Workout workout) {
        int workoutIndex = mCurrentUser.getWorkoutPlans().indexOf(workout);
        startDetailActivityWithIndex(workoutIndex);
    }

    private void startDetailActivityWithIndex(int index){
        Intent intent = new Intent(this, WorkoutDetailActivity.class);
        intent.putExtra(WorkoutDetailActivity.WORKOUT_INDEX_EXTRA, index);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, mFab, "transition_fab");
        startActivity(intent, options.toBundle());
    }

    private void signUserOut(){
        FirebaseDatabaseHelper.signUserOut(this);
    }
}
