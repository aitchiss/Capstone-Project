package com.suzanneaitchison.workoutpal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.suzanneaitchison.workoutpal.utils.CustomiseExerciseSpinnerUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomiseExerciseActivity extends AppCompatActivity {

//      Exercise selection options - Reps Layout
    @BindView(R.id.spinner_rest_reps) Spinner mRestReps;
    @BindView(R.id.et_reps_reps_layout) EditText mRepsRepsLayout;
    @BindView(R.id.et_sets_reps_layout) EditText mSetsRepsLayout;
    @BindView(R.id.et_weight_reps_layout) EditText mWeightRepsLayout;

//    Exercise selection options - Timed Layout
    @BindView(R.id.spinner_rest_timed) Spinner mRestTimed;
    @BindView(R.id.et_duration_timed_layout) EditText mDurationTimedLayout;
    @BindView(R.id.et_sets_timed_layout) EditText mSetsTimedLayout;
    @BindView(R.id.et_weight_timed_layout) EditText mWeightTimedLayout;

//    Tabbed layout views
    @BindView(R.id.reps_layout)
    LinearLayout mRepsLayout;

    @BindView(R.id.timed_layout)
    LinearLayout mTimedLayout;

    @BindView(R.id.tab_layout) TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customise_exercise);
        ButterKnife.bind(this);
        populateRestTimeSpinners();



        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
//                        Reps
                        mRepsLayout.setVisibility(View.VISIBLE);
                        mTimedLayout.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
//                        Timed
                        mTimedLayout.setVisibility(View.VISIBLE);
                        mRepsLayout.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void populateRestTimeSpinners(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CustomiseExerciseSpinnerUtils.getRestTimeOptions());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mRestReps.setAdapter(adapter);
        mRestTimed.setAdapter(adapter);
    }


    public void onAddTap(View view){
        Intent data = new Intent();

        if(mRepsLayout.getVisibility() == View.VISIBLE){
            data.putExtra(AddExerciseActivity.EXTRA_SETS, Integer.valueOf(mSetsRepsLayout.getText().toString()));
            data.putExtra(AddExerciseActivity.EXTRA_REPS, Integer.valueOf(mRepsRepsLayout.getText().toString()));
            data.putExtra(AddExerciseActivity.EXTRA_WEIGHT, Integer.valueOf(mWeightRepsLayout.getText().toString()));
            data.putExtra(AddExerciseActivity.EXTRA_REST, CustomiseExerciseSpinnerUtils.getRestTimeInSeconds(mRestReps.getSelectedItem().toString()));
        } else {
            data.putExtra(AddExerciseActivity.EXTRA_SETS, Integer.valueOf(mSetsTimedLayout.getText().toString()));
            data.putExtra(AddExerciseActivity.EXTRA_DURATION, Integer.valueOf(mDurationTimedLayout.getText().toString()));
            data.putExtra(AddExerciseActivity.EXTRA_WEIGHT, Integer.valueOf(mWeightTimedLayout.getText().toString()));
            data.putExtra(AddExerciseActivity.EXTRA_REST, CustomiseExerciseSpinnerUtils.getRestTimeInSeconds(mRestTimed.getSelectedItem().toString()));
        }

        if(getParent() == null){
            setResult(Activity.RESULT_OK, data);
        } else {
            getParent().setResult(Activity.RESULT_OK, data);
        }
        finish();
    }
}
