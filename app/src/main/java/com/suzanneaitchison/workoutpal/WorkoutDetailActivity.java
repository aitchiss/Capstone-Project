package com.suzanneaitchison.workoutpal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutDetailActivity extends AppCompatActivity {

    public static final String WORKOUT_ID_EXTRA = "workoutId";

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


        Intent intent = getIntent();

        if(intent.hasExtra(WORKOUT_ID_EXTRA) && intent.getIntExtra(WORKOUT_ID_EXTRA, -1) == -1){
            mWorkoutName.setText("New workout");
            getSupportActionBar().setTitle("New workout");
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
}
