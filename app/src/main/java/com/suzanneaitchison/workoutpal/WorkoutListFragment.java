package com.suzanneaitchison.workoutpal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.models.Workout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WorkoutListFragment extends Fragment implements WorkoutListRecyclerAdapter.WorkoutClickHandler {

    public interface StartActivityFromList {
        public void startDetailActivity(int workoutIndex, FloatingActionButton fab);
    }

    private User mCurrentUser;

    private WorkoutListRecyclerAdapter mAdapter;

    @BindView(R.id.fab_add_workout)
    FloatingActionButton mFab;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view_workout_list)
    RecyclerView mWorkoutRecyclerView;


    public WorkoutListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_workout_list, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);

        mCurrentUser = FirebaseDatabaseHelper.getUser();


//        Populate the view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), 1, false);
        mAdapter = new WorkoutListRecyclerAdapter(mCurrentUser.getWorkoutPlans(), getContext(), this);
        mWorkoutRecyclerView.setLayoutManager(layoutManager);
        mWorkoutRecyclerView.setAdapter(mAdapter);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Workout workout = new Workout();
//                workout.setWorkoutName("New workout");
//                int workoutIndex = mCurrentUser.addNewWorkoutPlan(workout);
//
//
//                Intent intent = new Intent(rootView.getContext(), WorkoutDetailActivity.class);
//                intent.putExtra(WorkoutDetailActivity.WORKOUT_INDEX_EXTRA, workoutIndex);
//                getActivity().startActivity(intent);
//                FirebaseDatabaseHelper.saveUsersPlannedWorkouts(mCurrentUser.getWorkoutPlans());
//                ((StartActivityFromList)getActivity()).startDetailActivity(workoutIndex, mFab);

                Intent intent = new Intent(rootView.getContext(), WorkoutDetailActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), mFab, "transition_fab");
                startActivity(intent, options.toBundle());
            }
        });

        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentUser = FirebaseDatabaseHelper.getUser();
        mAdapter.setWorkoutData(mCurrentUser.getWorkoutPlans());
    }

    public void onNewWorkoutButtonClick(){
//      create the new workout, add it to the users arraylist, then pass the index through to the next activity
        Workout workout = new Workout();
        workout.setWorkoutName("New workout");
        int workoutIndex = mCurrentUser.addNewWorkoutPlan(workout);

        FirebaseDatabaseHelper.saveUsersPlannedWorkouts(mCurrentUser.getWorkoutPlans());
        startDetailActivityWithIndex(workoutIndex);
    }


    @Override
    public void onClick(Workout workout) {
        int workoutIndex = mCurrentUser.getWorkoutPlans().indexOf(workout);
        startDetailActivityWithIndex(workoutIndex);
    }

    private void startDetailActivityWithIndex(int index){
        Intent intent = new Intent(getActivity(), WorkoutDetailActivity.class);
        intent.putExtra(WorkoutDetailActivity.WORKOUT_INDEX_EXTRA, index);


    }
}
