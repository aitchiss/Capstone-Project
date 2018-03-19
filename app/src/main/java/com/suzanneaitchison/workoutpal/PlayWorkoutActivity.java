package com.suzanneaitchison.workoutpal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.Exercise;
import com.suzanneaitchison.workoutpal.models.PlannedExercise;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.models.Workout;
import com.suzanneaitchison.workoutpal.models.WorkoutEntry;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.suzanneaitchison.workoutpal.WorkoutDetailActivity.WORKOUT_INDEX_EXTRA;

public class PlayWorkoutActivity extends AppCompatActivity implements PlayWorkoutSetsAdapter.SetButtonClickHandler {

    private Workout mWorkout;
    private User mUser;
    private int mWorkoutIndex;

    private ArrayList<ArrayList<PlannedExercise>> mTabData = new ArrayList<>();
    private PlayWorkoutSetsAdapter mAdapter;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    @BindView(R.id.tab_layout_exercise_names)
    TabLayout mExerciseTabLayout;

    @BindView(R.id.recycler_view_sets_list)
    RecyclerView mSetsRecyclerView;

    @BindView(R.id.iv_exercise)
    ImageView mExerciseImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_workout);
        ButterKnife.bind(this);
        mUser = FirebaseDatabaseHelper.getUser();

        Intent intent = getIntent();
        if(intent.hasExtra(WORKOUT_INDEX_EXTRA)){
            mWorkoutIndex  = intent.getIntExtra(WORKOUT_INDEX_EXTRA, -1);
            mWorkout = mUser.getWorkoutPlans().get(mWorkoutIndex);
            setUpToolbar();
            setUpTabView();
            setUpTabData();
        }
    }

    private void setUpTabView(){
        for(WorkoutEntry entry : mWorkout.getWorkoutEntries()){
            mExerciseTabLayout.addTab(mExerciseTabLayout.newTab().setText(entry.getExerciseName()));
        }

        mExerciseTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateWithTabSelection();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mWorkout.getWorkoutName());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateWithTabSelection(){
//  todo access the correct tab's data from mTabData and update the recycler view accordingly
//        todo update the image

        int selectedTab = mExerciseTabLayout.getSelectedTabPosition();
        mAdapter.updateExerciseData(mTabData.get(selectedTab));

        if(mTabData.get(selectedTab).get(0).getImageURL() == null || mTabData.get(selectedTab).get(0).getImageURL().isEmpty()){
            mExerciseImage.setImageDrawable(getResources().getDrawable(R.drawable.no_img_placeholder));
            mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorPlaceholder));
        } else {
            Picasso.get().load(mTabData.get(selectedTab).get(0).getImageURL())
                    .placeholder(getResources().getDrawable(R.drawable.no_img_placeholder))
                    .into(mExerciseImage);
            mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
    }

    private void setUpTabData(){
//        todo create an arrayList of all the exercises for that tab (i.e. each set)
//        todo add this to the correct tab position in mTabData
//        todo set a recycler view adapter onCreate with initial set of exercises - do this in onCreate?

        int numberOfTabs = mWorkout.getWorkoutEntries().size();

        for(int i=0; i < numberOfTabs; i++){
            ArrayList<PlannedExercise> listOfSets = new ArrayList<>();
            WorkoutEntry entry = mWorkout.getWorkoutEntries().get(i);
            Exercise exerciseInfo = FirebaseDatabaseHelper.getExerciseWithId(entry.getExerciseId());
            for(int j = 0; j < entry.getSets(); j++){
                PlannedExercise plannedExercise = new PlannedExercise(entry);
                if(exerciseInfo != null){
                    plannedExercise.setDescription(exerciseInfo.getDescription());
                    plannedExercise.setImageURL(exerciseInfo.getImageURL());
                    plannedExercise.setExerciseCategory(exerciseInfo.getExerciseCategory());
                }
                listOfSets.add(plannedExercise);
            }
            mTabData.add(i, listOfSets);
        }

        mAdapter = new PlayWorkoutSetsAdapter(mTabData.get(0), this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, 1, false);
        mSetsRecyclerView.setLayoutManager(layoutManager);
        mSetsRecyclerView.setAdapter(mAdapter);

        if(mTabData.get(0).get(0).getImageURL() == null || mTabData.get(0).get(0).getImageURL().isEmpty()){
            mExerciseImage.setImageDrawable(getResources().getDrawable(R.drawable.no_img_placeholder));
            mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorPlaceholder));
        } else {
            Picasso.get().load(mTabData.get(0).get(0).getImageURL())
                    .placeholder(getResources().getDrawable(R.drawable.no_img_placeholder))
                    .into(mExerciseImage);
            mExerciseImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
    }

    private boolean isWorkoutComplete(){
        for(ArrayList<PlannedExercise> activity : mTabData){
            for(PlannedExercise exercise : activity){
                if(!exercise.isComplete()){
                    return false;
                }
            }
        }
        return true;
    }

    private void showRestTimer(int restTime){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final TextView countdownTextView = new TextView(this);
        countdownTextView.setText(String.valueOf(restTime));
        countdownTextView.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            countdownTextView.setTextAppearance(R.style.set_detail_text_view_style);
        }
        alertDialog.setView(countdownTextView);
        alertDialog.setTitle(getResources().getString(R.string.rest));
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.skip), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

        new CountDownTimer(restTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
            }
        }.start();
    }

    private void completeTimedExercise(final PlannedExercise exercise){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final TextView countdownTextView = new TextView(this);
        countdownTextView.setText(String.valueOf(exercise.getDuration()));
        countdownTextView.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            countdownTextView.setTextAppearance(R.style.set_detail_text_view_style);
        }
        alertDialog.setView(countdownTextView);
        alertDialog.setTitle(getResources().getString(R.string.title_timed_exercise));
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

        new CountDownTimer(exercise.getDuration() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                exercise.setComplete(true);
                mUser.addCompletedExercise(exercise);
                FirebaseDatabaseHelper.saveUsersCompletedExercises(mUser.getCompletedExercises());

                if(isWorkoutComplete()){
//                   todo handle completed workout
                } else {
                    if(exercise.getRestTime() > 0){
                        showRestTimer(exercise.getRestTime());
                    }
                    mAdapter.updateExerciseData(mTabData.get(mExerciseTabLayout.getSelectedTabPosition()));
                }
                alertDialog.dismiss();
            }
        }.start();
    }

    private void showWorkoutCompleteDialogAndFinish(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        LinearLayout completeLayout = new LinearLayout(this);
        completeLayout.setOrientation(LinearLayout.VERTICAL);
        completeLayout.setPadding(20,200,20,200);
        ImageView image = new ImageView(this);
        image.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_complete));
        image.setMinimumHeight(75);
        image.setMinimumWidth(75);
        TextView completeText = new TextView(this);
        completeText.setPadding(0,50,0,0);
        completeText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        completeText.setText(getResources().getString(R.string.workout_complete));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            completeText.setTextAppearance(R.style.set_detail_text_view_style);
        }

        completeLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        completeLayout.addView(image);
        completeLayout.addView(completeText);

        alertDialog.setView(completeLayout);
        alertDialog.show();

        new CountDownTimer(3000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
                finish();
            }
        }.start();
    }

    @Override
    public void onClick(PlannedExercise editedExercise, int position) {
        PlannedExercise exercise = mTabData.get(mExerciseTabLayout.getSelectedTabPosition()).get(position);
        if(!exercise.isComplete()){
//            If the exercise is already complete, nothing needs to be done
            boolean isTimedExercise = exercise.getDuration() > 0;
            if(isTimedExercise){
//                todo - start a dialog timer, then mark as complete
                exercise.setWeight(editedExercise.getWeight());
                completeTimedExercise(exercise);
            } else {
//                update with the edited fields
                exercise.setReps(editedExercise.getReps());
                exercise.setWeight(editedExercise.getWeight());
                exercise.setComplete(true);
//                save the completed exercise to the users completed exercises
                mUser.addCompletedExercise(exercise);
                FirebaseDatabaseHelper.saveUsersCompletedExercises(mUser.getCompletedExercises());

                if(isWorkoutComplete()){
//                    todo - finish the workout
                    showWorkoutCompleteDialogAndFinish();
                } else {
//                 kick off the rest timer if needed
                    if(exercise.getRestTime() > 0){
                        showRestTimer(exercise.getRestTime());
                    }
                    mAdapter.updateExerciseData(mTabData.get(mExerciseTabLayout.getSelectedTabPosition()));
                }


            }
        }
    }
}
