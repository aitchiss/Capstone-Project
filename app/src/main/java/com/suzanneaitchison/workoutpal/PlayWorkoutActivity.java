package com.suzanneaitchison.workoutpal;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.Exercise;
import com.suzanneaitchison.workoutpal.models.PlannedExercise;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.models.Workout;
import com.suzanneaitchison.workoutpal.models.WorkoutEntry;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.suzanneaitchison.workoutpal.WorkoutDetailActivity.WORKOUT_INDEX_EXTRA;

public class PlayWorkoutActivity extends AppCompatActivity {

    private Workout mWorkout;
    private User mUser;
    private int mWorkoutIndex;

    private ArrayList<ArrayList<PlannedExercise>> mTabData = new ArrayList<>();
    private PlayWorkoutSetsAdapter mAdapter;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    @BindView(R.id.tab_layout_exercise_names)
    TabLayout mExerciseTabLayout;

    @BindView(R.id.recycler_view_sets_list)
    RecyclerView mSetsRecyclerView;

    @BindView(R.id.iv_exercise)
    ImageView mExerciseImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_workout);
        ButterKnife.bind(this);
        mUser = FirebaseDatabaseHelper.getUser();

        Intent intent = getIntent();
        if(intent.hasExtra(WORKOUT_INDEX_EXTRA)){
            mWorkoutIndex  = intent.getIntExtra(WORKOUT_INDEX_EXTRA, -1);
            mWorkout = mUser.getWorkoutPlans().get(mWorkoutIndex);
            setUpToolbar();
            setUpTabView();
            setUpTabData();


        }
    }

    private void setUpTabView(){
        for(WorkoutEntry entry : mWorkout.getWorkoutEntries()){
            mExerciseTabLayout.addTab(mExerciseTabLayout.newTab().setText(entry.getExerciseName()));
        }

        mExerciseTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateWithTabSelection();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mWorkout.getWorkoutName());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateWithTabSelection(){
//  todo access the correct tab's data from mTabData and update the recycler view accordingly
//        todo update the image

        int selectedTab = mExerciseTabLayout.getSelectedTabPosition();
        mAdapter.updateExerciseData(mTabData.get(selectedTab));

        if(mTabData.get(selectedTab).get(0).getImageURL() == null || mTabData.get(selectedTab).get(0).getImageURL().isEmpty()){
            mExerciseImage.setImageDrawable(getResources().getDrawable(R.drawable.no_img_placeholder));
            mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorPlaceholder));
        } else {
            Picasso.get().load(mTabData.get(selectedTab).get(0).getImageURL())
                    .placeholder(getResources().getDrawable(R.drawable.no_img_placeholder))
                    .into(mExerciseImage);
            mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
    }

    private void setUpTabData(){
//        todo create an arrayList of all the exercises for that tab (i.e. each set)
//        todo add this to the correct tab position in mTabData
//        todo set a recycler view adapter onCreate with initial set of exercises - do this in onCreate?

        int numberOfTabs = mWorkout.getWorkoutEntries().size();

        for(int i=0; i < numberOfTabs; i++){
            ArrayList<PlannedExercise> listOfSets = new ArrayList<>();
            WorkoutEntry entry = mWorkout.getWorkoutEntries().get(i);
            Exercise exerciseInfo = FirebaseDatabaseHelper.getExerciseWithId(entry.getExerciseId());
            for(int j = 0; j < entry.getSets(); j++){
                PlannedExercise plannedExercise = new PlannedExercise(entry);
                if(exerciseInfo != null){
                    plannedExercise.setDescription(exerciseInfo.getDescription());
                    plannedExercise.setImageURL(exerciseInfo.getImageURL());
                    plannedExercise.setExerciseCategory(exerciseInfo.getExerciseCategory());
                }
                listOfSets.add(plannedExercise);
            }
            mTabData.add(i, listOfSets);
        }

        mAdapter = new PlayWorkoutSetsAdapter(mTabData.get(0), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, 1, false);
        mSetsRecyclerView.setLayoutManager(layoutManager);
        mSetsRecyclerView.setAdapter(mAdapter);

        if(mTabData.get(0).get(0).getImageURL() == null || mTabData.get(0).get(0).getImageURL().isEmpty()){
            mExerciseImage.setImageDrawable(getResources().getDrawable(R.drawable.no_img_placeholder));
            mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorPlaceholder));
        } else {
            Picasso.get().load(mTabData.get(0).get(0).getImageURL())
                    .placeholder(getResources().getDrawable(R.drawable.no_img_placeholder))
                    .into(mExerciseImage);
            mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
    }
}
