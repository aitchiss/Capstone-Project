package com.suzanneaitchison.workoutpal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutDetailActivity extends AppCompatActivity {

    public static final String WORKOUT_ID_EXTRA = "workoutId";

    @BindView(R.id.et_workout_name)
    EditText mWorkoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        if(intent.hasExtra(WORKOUT_ID_EXTRA) && intent.getIntExtra(WORKOUT_ID_EXTRA, -1) == -1){
            mWorkoutName.setText("New workout");
        }
    }
}
