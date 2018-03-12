package com.suzanneaitchison.workoutpal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toolbar;

import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.models.Workout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutDetailActivity extends AppCompatActivity {

    public static final String WORKOUT_INDEX_EXTRA = "workoutId";

    private User mUser;
    private Workout mWorkout;
    private int mWorkoutIndex;

    private WorkoutDetailRecyclerAdapter mDetailAdapter;

    @BindView(R.id.et_workout_name)
    EditText mWorkoutName;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    @BindView(R.id.exercises_recycler_view)
    RecyclerView mExercisesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);
        ButterKnife.bind(this);
        setUpToolbar();
        mUser = FirebaseDatabaseHelper.getUser();

        Intent intent = getIntent();

        if(intent.hasExtra(WORKOUT_INDEX_EXTRA)){
            mWorkoutIndex  = intent.getIntExtra(WORKOUT_INDEX_EXTRA, -1);
            mWorkout = mUser.getWorkoutPlans().get(mWorkoutIndex);
            mWorkoutName.setText(mWorkout.getWorkoutName());
            getSupportActionBar().setTitle(mWorkout.getWorkoutName());

//            todo create the workout in firebase and get the id - do it on back button click?
        }

        mDetailAdapter = new WorkoutDetailRecyclerAdapter(mWorkout.getPlannedExercises(), this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, 1, false);
        mExercisesRecyclerView.setLayoutManager(layoutManager);
        mExercisesRecyclerView.setAdapter(mDetailAdapter);

        mWorkoutName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//  todo              update the workout name in the db and the view - save in FB
                mWorkout.setWorkoutName(s.toString());
                getSupportActionBar().setTitle(s.toString());
            }
        });
    }

    @Override
    protected void onResume() {
//        Refresh the activity with the latest version of the user's workout plan
        mUser = FirebaseDatabaseHelper.getUser();
        if(mWorkoutIndex >= 0){
            mWorkout = mUser.getWorkoutPlans().get(mWorkoutIndex);
            mDetailAdapter.setExerciseData(mWorkout.getPlannedExercises());
        }
        super.onResume();
    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onAddExerciseClick(View view){
        Intent intent = new Intent(this, AddExerciseActivity.class);
        intent.putExtra(WORKOUT_INDEX_EXTRA, mWorkoutIndex);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up_anim, R.anim.static_anim);
    }
}
