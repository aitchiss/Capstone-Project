package com.suzanneaitchison.workoutpal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomiseExerciseActivity extends AppCompatActivity {


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


    public void onAddTap(View view){
        Intent data = new Intent();
        if(getParent() == null){
            setResult(Activity.RESULT_OK, data);
        } else {
            getParent().setResult(Activity.RESULT_OK, data);
        }
        finish();
    }
}
