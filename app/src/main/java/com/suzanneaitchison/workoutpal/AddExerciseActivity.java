package com.suzanneaitchison.workoutpal;



import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.Exercise;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddExerciseActivity extends AppCompatActivity {

    private static final int ADD_EXERCISE_REQUEST_CODE = 220;
    private static final String EXERCISE_CATEGORY_ALL = "All";

    private ArrayList<Exercise> mExercises;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    @BindView(R.id.spinner_exercises)
    Spinner mExerciseSpinner;

    @BindView(R.id.spinner_exercise_category) Spinner mExerciseCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        ButterKnife.bind(this);
        setUpToolbar();

        mExercises = FirebaseDatabaseHelper.getAllExercises();
        populateCategorySpinner();
        populateExercisesSpinner();
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
//               Add the exercise to the workout
                Log.d("add exercise", "received ok result");
            }
        }
    }
}
