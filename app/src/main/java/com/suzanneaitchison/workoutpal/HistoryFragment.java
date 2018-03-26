package com.suzanneaitchison.workoutpal;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ap_history_graph)
    XYPlot mHistoryGraph;


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

        setUpGraph();

        return rootView;
    }

    private void setUpGraph(){
//        ArrayList<Workout> workouts = mUser.getWorkoutPlans();
//        ArrayList<Date> completedDates = new ArrayList<>();
//        for(Workout workout : workouts){
//            if(workout.getCompletedDate() != null){
//                completedDates.add(workout.getCompletedDate());
//            }
//        }
////      Put into date order
//        Collections.sort(completedDates);
//        HashMap<Date, Integer> mapOfDates = new HashMap<>();
//
//        for(Date workoutDate : completedDates){
//            if (mapOfDates.size() == 0){
//                mapOfDates.put(workoutDate, 1);
//            } else {
////                Check if the workout occurred on same date as any existing key
//                for(Date dateKey : mapOfDates.keySet()){
//                    Calendar keyCalendar = Calendar.getInstance();
//                    Calendar workoutCalendar = Calendar.getInstance();
//
//                    keyCalendar.setTime(dateKey);
//                    workoutCalendar.setTime(workoutDate);
//
//                    boolean sameDay = keyCalendar.get(Calendar.DATE) == workoutCalendar.get(Calendar.DATE);
//                    if(sameDay){
//                        mapOfDates.put(workoutDate, mapOfDates.get(workoutDate) + 1);
//                    } else {
//                        mapOfDates.put(workoutDate, 1);
//                    }
//                }
//            }
//        }

//        Integer[] ySeries = new Integer[mapOfDates.values().size()];
//        Object[] seriesData = mapOfDates.values().toArray();
//        for(int j= 0; j < seriesData.length; j++){
//            ySeries[j] = (Integer) seriesData[j];
//        };
//
//        final String[] xSeries = new String[mapOfDates.size()];
//
//        Object[] dates = mapOfDates.keySet().toArray();
//        for(int i=0; i < dates.length; i++){
//            xSeries[i] = dates[i].toString();
//        }
//
//
//        final String[] xSeries = new String[]{ "Monday", "Tuesday", "Friday" };
//        Integer[] ySeries = new Integer[]{2, 3, 1};
//
//        XYSeries xySeriesY = new SimpleXYSeries(Arrays.asList(ySeries), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1");
//        LineAndPointFormatter series1Format =
//                new LineAndPointFormatter(Color.RED, Color.RED, null, null);
//
//
//
//        mHistoryGraph.addSeries(xySeriesY, series1Format);
//        mHistoryGraph.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
//            @Override
//            public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
//                int i = Math.round(((Number) obj).floatValue());
//                return toAppendTo.append("hello");
//            }
//
//            @Override
//            public Object parseObject(String source, @NonNull ParsePosition pos) {
//                return null;
//            }
//        });



    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
