package com.suzanneaitchison.workoutpal;



import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddExerciseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        ButterKnife.bind(this);
        setUpToolbar();

    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.add_activity);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        startActivity(intent);
    }



}
