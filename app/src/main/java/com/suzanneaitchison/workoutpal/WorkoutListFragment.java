package com.suzanneaitchison.workoutpal;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
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

    private User mCurrentUser;
    private WorkoutListRecyclerAdapter mAdapter;
    private static final String KEY_LIST_POSITION = "listPosition";

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
                Intent intent = new Intent(rootView.getContext(), WorkoutDetailActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), mFab, "transition_fab");
                startActivity(intent, options.toBundle());
            }
        });

        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_LIST_POSITION, mWorkoutRecyclerView.getVerticalScrollbarPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_LIST_POSITION)){
            mWorkoutRecyclerView.setVerticalScrollbarPosition(savedInstanceState.getInt(KEY_LIST_POSITION));
        }
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


    @Override
    public void onClick(Workout workout) {
        int workoutIndex = mCurrentUser.getWorkoutPlans().indexOf(workout);
        Intent intent = new Intent(getActivity(), WorkoutDetailActivity.class);
        intent.putExtra(WorkoutDetailActivity.WORKOUT_INDEX_EXTRA, workoutIndex);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), mFab, "transition_fab");
        startActivity(intent, options.toBundle());
    }

}
