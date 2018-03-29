package com.suzanneaitchison.workoutpal;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.Achievement;
import com.suzanneaitchison.workoutpal.models.PlannedExercise;
import com.suzanneaitchison.workoutpal.models.User;
import com.suzanneaitchison.workoutpal.models.Workout;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryFragment extends Fragment {

    private User mUser;

    private PersonalBestsRecyclerAdapter mAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_personal_bests)
    RecyclerView mRecylerPersonalBests;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, rootView);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        actionbar.setTitle(rootView.getContext().getResources().getString(R.string.history_title));

        mUser = FirebaseDatabaseHelper.getUser();
        showPersonalBests();

        return rootView;
    }


    private void showPersonalBests(){

        ArrayList<Achievement> personalBests = mUser.getAllAchievements();
        mAdapter = new PersonalBestsRecyclerAdapter(personalBests);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), 1, false);
        mRecylerPersonalBests.setLayoutManager(layoutManager);
        mRecylerPersonalBests.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
