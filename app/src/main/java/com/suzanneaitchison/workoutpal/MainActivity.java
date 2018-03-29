package com.suzanneaitchison.workoutpal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
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

public class MainActivity extends AppCompatActivity {

    private Fragment mCurrentFragment;
    private boolean mIsListView;
    private static final String IS_LIST_VIEW_KEY = "isListView";

    private User mCurrentUser;

    @BindView(R.id.nav_drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavView;


    TextView mNavHeaderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);
        ButterKnife.bind(this);

        setUpDrawerMenuListeners();

        if(savedInstanceState == null || mCurrentFragment == null){
//            Default to the listView
            mIsListView = true;
            mCurrentFragment = new WorkoutListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mCurrentFragment)
                    .commit();
        } else {
            mIsListView = savedInstanceState.getBoolean(IS_LIST_VIEW_KEY, true);
            if(mIsListView){
                showListFragment();
            } else {
                showHistoryFragment();
            }
        }

        mCurrentUser = FirebaseDatabaseHelper.getUser();
        View mNavHeaderView = mNavView.getHeaderView(0);
        mNavHeaderText = (TextView) mNavHeaderView.findViewById(R.id.nav_drawer_header_text);
        setNavDrawerTitle(mCurrentUser.getEmail());


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_LIST_VIEW_KEY, mIsListView);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mIsListView = savedInstanceState.getBoolean(IS_LIST_VIEW_KEY, true);
        if(mIsListView){
            showListFragment();
        } else {
            showHistoryFragment();
        }

    }

    private void setNavDrawerTitle(String userEmail){
        mNavHeaderText.setText("You're signed in as " + userEmail);
    }

    private void setUpDrawerMenuListeners(){
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.sign_out:
                        signUserOut();
                        break;
                    case R.id.history:
                        showHistoryFragment();
                        break;
                    case R.id.home:
                        showListFragment();
                        break;
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void showListFragment(){
        mIsListView = true;
        if(!(mCurrentFragment instanceof WorkoutListFragment)){
            mCurrentFragment = new WorkoutListFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mCurrentFragment)
                .commit();
    }

    private void showHistoryFragment(){
        mIsListView = false;
        if(!(mCurrentFragment instanceof WorkoutListFragment)){
            mCurrentFragment = new WorkoutListFragment();
        }
        mCurrentFragment = new HistoryFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mCurrentFragment)
                .commit();
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



    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser = FirebaseDatabaseHelper.getUser();
    }

    private void signUserOut(){
        FirebaseDatabaseHelper.signUserOut(this);
    }

}
