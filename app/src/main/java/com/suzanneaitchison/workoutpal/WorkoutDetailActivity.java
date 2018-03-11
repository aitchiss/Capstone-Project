package com.suzanneaitchison.workoutpal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @BindView(R.id.et_workout_name)
    EditText mWorkoutName;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);
        ButterKnife.bind(this);
        setUpToolbar();
        mUser = FirebaseDatabaseHelper.getUser();

        Intent intent = getIntent();

        if(intent.hasExtra(WORKOUT_INDEX_EXTRA)){
            int index = intent.getIntExtra(WORKOUT_INDEX_EXTRA, -1);
            mWorkout = mUser.getWorkoutPlans().get(index);
            mWorkoutName.setText(mWorkout.getWorkoutName());
            getSupportActionBar().setTitle(mWorkout.getWorkoutName());

//            todo create the workout in firebase and get the id
        }
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
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up_anim, R.anim.static_anim);

    }
}
