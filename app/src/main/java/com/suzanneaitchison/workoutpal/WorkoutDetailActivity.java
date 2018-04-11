package com.suzanneaitchison.workoutpal;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.models.Workout;
import com.suzanneaitchison.workoutpal.models.WorkoutEntry;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutDetailActivity extends AppCompatActivity {

    public static final String WORKOUT_INDEX_EXTRA = "workoutId";

    private User mUser;
    private Workout mWorkout;
    private int mWorkoutIndex;
    private static final String KEY_LIST_POSITION = "listPosition";

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
        } else {
//            this is a new workout
            mWorkout = new Workout();
            mWorkout.setWorkoutName(getResources().getString(R.string.new_workout_name));
            mWorkoutIndex = mUser.addNewWorkoutPlan(mWorkout);
            mWorkoutName.setText(mWorkout.getWorkoutName());
            getSupportActionBar().setTitle(mWorkout.getWorkoutName());
            FirebaseDatabaseHelper.saveUsersPlannedWorkouts(mUser.getWorkoutPlans());
        }

        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteWorkoutEntry(viewHolder.getAdapterPosition());
            }
        };

        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(mExercisesRecyclerView);

        mDetailAdapter = new WorkoutDetailRecyclerAdapter(mWorkout.getWorkoutEntries(), this);

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
                mWorkout.setWorkoutName(s.toString());
                mUser.getWorkoutPlans().set(mWorkoutIndex, mWorkout);
                FirebaseDatabaseHelper.saveUsersPlannedWorkouts(mUser.getWorkoutPlans());
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_LIST_POSITION, mExercisesRecyclerView.getVerticalScrollbarPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_LIST_POSITION)){
            mExercisesRecyclerView.setVerticalScrollbarPosition(savedInstanceState.getInt(KEY_LIST_POSITION));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_workout_detail, menu);
        return true;
    }

    @Override
    protected void onResume() {
//        Refresh the activity with the latest version of the user's workout plan
        mUser = FirebaseDatabaseHelper.getUser();
        if(mWorkoutIndex >= 0){
            mWorkout = mUser.getWorkoutPlans().get(mWorkoutIndex);
            mDetailAdapter.setWorkoutEntryData(mWorkout.getWorkoutEntries());
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
            case R.id.action_delete:
                ArrayList<Workout> updatedWorkouts = mUser.getWorkoutPlans();
                updatedWorkouts.remove(mWorkoutIndex);
                FirebaseDatabaseHelper.saveUsersPlannedWorkouts(updatedWorkouts);
                supportFinishAfterTransition();
                Toast toast = Toast.makeText(this, getResources().getString(R.string.deleted_workout), Toast.LENGTH_LONG);
                toast.show();
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

    private void deleteWorkoutEntry(int position){
        ArrayList<WorkoutEntry> entries = mWorkout.getWorkoutEntries();
        entries.remove(position);
        ArrayList<Workout> updatedWorkouts = mUser.getWorkoutPlans();
        updatedWorkouts.set(mWorkoutIndex, mWorkout);
        FirebaseDatabaseHelper.saveUsersPlannedWorkouts(updatedWorkouts);

        mDetailAdapter.setWorkoutEntryData(entries);
    }

    public void onPlayButtonTap(View view){
        if (mWorkout.getWorkoutEntries() == null || mWorkout.getWorkoutEntries().size() == 0)
        {
            return;
        }
        Intent intent = new Intent(this, PlayWorkoutActivity.class);
        intent.putExtra(WORKOUT_INDEX_EXTRA, mWorkoutIndex);
        startActivity(intent);
    }



}
