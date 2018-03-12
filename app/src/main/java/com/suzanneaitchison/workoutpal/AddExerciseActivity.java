package com.suzanneaitchison.workoutpal;



import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.Exercise;
import com.suzanneaitchison.workoutpal.models.PlannedExercise;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.models.Workout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddExerciseActivity extends AppCompatActivity {

    public static final String EXTRA_SETS = "sets";
    public static final String EXTRA_WEIGHT = "weight";
    public static final String EXTRA_REPS = "reps";
    public static final String EXTRA_DURATION = "duration";
    public static final String EXTRA_REST = "rest";

    private static final int ADD_EXERCISE_REQUEST_CODE = 220;
    private static final String EXERCISE_CATEGORY_ALL = "All";

    private User mUser;
    private int mWorkoutIndex;
    private Workout mWorkout;

    private ArrayList<Exercise> mExercises;

    @BindView(R.id.scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    @BindView(R.id.spinner_exercises)
    Spinner mExerciseSpinner;

    @BindView(R.id.spinner_exercise_category) Spinner mExerciseCategorySpinner;

    @BindView(R.id.tv_exercise_desc)
    TextView mExerciseDescription;

    @BindView(R.id.iv_exercise_image)
    ImageView mExerciseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        ButterKnife.bind(this);
        setUpToolbar();
        mUser = FirebaseDatabaseHelper.getUser();

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(WorkoutDetailActivity.WORKOUT_INDEX_EXTRA)){
            mWorkoutIndex = intent.getIntExtra(WorkoutDetailActivity.WORKOUT_INDEX_EXTRA, -1);

            User user = FirebaseDatabaseHelper.getUser();
            mWorkout = user.getWorkoutPlans().get(mWorkoutIndex);
        }

        mExercises = FirebaseDatabaseHelper.getAllExercises();
        populateCategorySpinner();
        populateExercisesSpinner();

        mExerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Exercise exercise = getSelectedExercise();
                mExerciseDescription.setText(Html.fromHtml(exercise.getDescription()));

                if(exercise.getImageURL() == null || exercise.getImageURL().isEmpty()){
                    mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorPlaceholder));
                } else {
                    mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                Picasso.get().load(exercise.getImageURL())
                        .placeholder(R.drawable.no_img_placeholder).into(mExerciseImage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.add_activity);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateCategorySpinner(){
//        Get all the unique categories
        ArrayList<String> categories = new ArrayList<>();
        categories.add(EXERCISE_CATEGORY_ALL);
        for (Exercise exercise : mExercises){
            if(exercise.getExerciseCategory() == null || exercise.getExerciseCategory().isEmpty()){
                continue;
            }
            if(!categories.contains(exercise.getExerciseCategory())){
                categories.add(exercise.getExerciseCategory());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExerciseCategorySpinner.setAdapter(adapter);

        mExerciseCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateExercisesSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void populateExercisesSpinner(){
        String category = mExerciseCategorySpinner.getSelectedItem().toString();

        List<String> spinnerList = new ArrayList<String>();
        for(Exercise exercise : mExercises){
            if( exercise.getName() != null && !exercise.getName().isEmpty()){
                if(category.equals(EXERCISE_CATEGORY_ALL ) || category.equals(exercise.getExerciseCategory())){
                    spinnerList.add(exercise.getName());
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExerciseSpinner.setAdapter(adapter);
    }

    private Exercise getSelectedExercise(){
        String exerciseName = mExerciseSpinner.getSelectedItem().toString();
        for(Exercise exercise : mExercises){
            if(exercise.getName().equals(exerciseName)){
                return exercise;
            }
        }
        return new Exercise();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case android.R.id.home:
                supportFinishAfterTransition();
                overridePendingTransition(R.anim.static_anim, R.anim.slide_down_anim);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onAddTap(View view){
//        Create the dialog to make further selections
        Intent intent = new Intent(this, CustomiseExerciseActivity.class);
        startActivityForResult(intent, ADD_EXERCISE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_EXERCISE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
//                Get the current spinner exercise item
                Exercise selectedExercise = new Exercise();
                for (Exercise exercise : mExercises){
                    if(exercise.getName().equals(mExerciseSpinner.getSelectedItem().toString())){
                        selectedExercise = exercise;
                    }
                }

//                Get the further details from the result data
                int sets = 0;
                int weight = 0;
                int duration = 0;
                int reps = 0;
                int restTime = 0;

                if(data.hasExtra(EXTRA_SETS)){
                    sets = data.getIntExtra(EXTRA_SETS, 0);
                }
                if(data.hasExtra(EXTRA_WEIGHT)){
                    weight = data.getIntExtra(EXTRA_WEIGHT, 0);
                }
                if(data.hasExtra(EXTRA_DURATION)){
                    duration = data.getIntExtra(EXTRA_DURATION, 0);
                }
                if(data.hasExtra(EXTRA_REPS)){
                    reps = data.getIntExtra(EXTRA_REPS, 0);
                }
                if(data.hasExtra(EXTRA_REST)){
                    restTime = data.getIntExtra(EXTRA_REST, 0);
                }

//                if there are no sets to add, return without proceeding
                if(sets == 0){ return; }

//                Add the exercise to the mWorkout, as many as sets dictates
                for(int i=0; i < sets; i++){
                    PlannedExercise plannedExercise = new PlannedExercise();
                    plannedExercise.setName(selectedExercise.getName());
                    plannedExercise.setId(selectedExercise.getId());
                    plannedExercise.setDescription(selectedExercise.getDescription());
                    plannedExercise.setExerciseCategory(selectedExercise.getExerciseCategory());
                    plannedExercise.setImageURL(selectedExercise.getImageURL());
                    plannedExercise.setWeight(weight);
                    plannedExercise.setDuration(duration);
                    plannedExercise.setReps(reps);
                    plannedExercise.setRestTime(restTime);
                    mWorkout.addExercise(plannedExercise);
                }
//                Update the users workout plans and save in Firebase

                ArrayList<Workout> updatedWorkouts = mUser.getWorkoutPlans();
                updatedWorkouts.set(mWorkoutIndex, mWorkout);
                FirebaseDatabaseHelper.saveUsersPlannedWorkouts(updatedWorkouts);

                Snackbar snackbar = Snackbar.make(mScrollView, "Exercise added", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
}