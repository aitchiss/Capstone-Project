package com.suzanneaitchison.workoutpal;



import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suzanneaitchison.workoutpal.data.ExerciseContract;
import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.Exercise;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.models.Workout;
import com.suzanneaitchison.workoutpal.models.WorkoutEntry;
import com.suzanneaitchison.workoutpal.utils.ExerciseCursorUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddExerciseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_SETS = "sets";
    public static final String EXTRA_WEIGHT = "weight";
    public static final String EXTRA_REPS = "reps";
    public static final String EXTRA_DURATION = "duration";
    public static final String EXTRA_REST = "rest";

    private static final String KEY_EXERCISES = "exercises";
    private static final String KEY_CATEGORY_SPINNER = "categorySpinner";
    private static final String KEY_EXERCISE_SPINNER = "exerciseSpinner";
    private boolean mUserInitiatedCategorySelect;
    private boolean mUserInitiatedExerciseSelect;

    private static final int ID_EXERCISE_LOADER = 100;

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

    @BindView(R.id.btn_confirm_add_exercise)
    Button mAddExerciseButton;

    @BindView(R.id.add_exercise_progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.no_exercise_data_layout)
    LinearLayout mNoExercisesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        ButterKnife.bind(this);
        mNoExercisesLayout.setVisibility(View.INVISIBLE);

        setUpToolbar();
        mUser = FirebaseDatabaseHelper.getUser();

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(WorkoutDetailActivity.WORKOUT_INDEX_EXTRA)){
            mWorkoutIndex = intent.getIntExtra(WorkoutDetailActivity.WORKOUT_INDEX_EXTRA, -1);
            User user = FirebaseDatabaseHelper.getUser();
            mWorkout = user.getWorkoutPlans().get(mWorkoutIndex);
        }

        if(savedInstanceState == null && (mExercises == null || mExercises.size() == 0)){
            showLoading();
            getSupportLoaderManager().initLoader(ID_EXERCISE_LOADER, null, this);
        }


        mExerciseSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mUserInitiatedExerciseSelect = true;
                return false;
            }
        });
        mExerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mUserInitiatedExerciseSelect){
                    Exercise exercise = getSelectedExercise();
                    updateFromSpinnerSelection(exercise);
                    mUserInitiatedExerciseSelect = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_EXERCISES, mExercises);
        outState.putInt(KEY_CATEGORY_SPINNER, mExerciseCategorySpinner.getSelectedItemPosition());
        outState.putInt(KEY_EXERCISE_SPINNER, mExerciseSpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            mExercises = savedInstanceState.getParcelableArrayList(KEY_EXERCISES);

            populateCategorySpinner();
            mExerciseCategorySpinner.setSelection(savedInstanceState.getInt(KEY_CATEGORY_SPINNER));

            populateExercisesSpinner();
            mExerciseSpinner.setSelection(savedInstanceState.getInt(KEY_EXERCISE_SPINNER));
            Exercise exercise = getSelectedExercise();
            updateFromSpinnerSelection(exercise);
            hideLoading();
        }
    }

    private void updateFromSpinnerSelection(Exercise exercise){
        mExerciseDescription.setText(Html.fromHtml(exercise.getDescription()));
        if(exercise.getImageURL() == null || exercise.getImageURL().isEmpty()){
            mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorPlaceholder));
        } else {
            mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
        Picasso.get().load(exercise.getImageURL())
                .placeholder(R.drawable.no_img_placeholder).into(mExerciseImage);
    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.add_activity);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showLoading(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showNoExerciseError(){
        mAddExerciseButton.setVisibility(View.INVISIBLE);
        mExerciseImage.setVisibility(View.INVISIBLE);
        mExerciseCategorySpinner.setVisibility(View.INVISIBLE);
        mExerciseSpinner.setVisibility(View.INVISIBLE);
        mExerciseDescription.setVisibility(View.INVISIBLE);
        mNoExercisesLayout.setVisibility(View.VISIBLE);
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

        mExerciseCategorySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mUserInitiatedCategorySelect = true;
                mUserInitiatedExerciseSelect = true;
                return false;
            }
        });

        mExerciseCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mUserInitiatedCategorySelect){
//                    Only do this if not restoring from saved instance state
                    populateExercisesSpinner();
                    mUserInitiatedCategorySelect = false;
                    mUserInitiatedExerciseSelect = false;
                }
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

//                Add the entry to the mWorkout

                WorkoutEntry workoutEntry = new WorkoutEntry();
                workoutEntry.setExerciseId(selectedExercise.getId());
                workoutEntry.setExerciseName(selectedExercise.getName());
                workoutEntry.setSets(sets);
                workoutEntry.setWeight(weight);
                workoutEntry.setDuration(duration);
                workoutEntry.setReps(reps);
                workoutEntry.setRestTime(restTime);
                mWorkout.addWorkoutEntry(workoutEntry);

//                Update the users workout plans and save in Firebase

                ArrayList<Workout> updatedWorkouts = mUser.getWorkoutPlans();
                updatedWorkouts.set(mWorkoutIndex, mWorkout);
                FirebaseDatabaseHelper.saveUsersPlannedWorkouts(updatedWorkouts);

                Snackbar snackbar = Snackbar.make(mScrollView, "Exercise added", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case ID_EXERCISE_LOADER:
                Uri queryUri = ExerciseContract.ExerciseEntry.CONTENT_URI;
                return new CursorLoader(this, queryUri, null, null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
 //      Convert to arraylist of exercises, update the spinners
        mExercises = ExerciseCursorUtils.convertCursorToExercises(data);
        if(mExercises.size() == 0){
            showNoExerciseError();
        } else {
            populateCategorySpinner();
            populateExercisesSpinner();
        }
        hideLoading();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
//      We don't want to clear the data until a new set of exercise data is available
    }


}
