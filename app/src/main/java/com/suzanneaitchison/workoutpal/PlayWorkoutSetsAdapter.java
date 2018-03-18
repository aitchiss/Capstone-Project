package com.suzanneaitchison.workoutpal;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suzanneaitchison.workoutpal.models.PlannedExercise;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 18/03/2018.
 */

public class PlayWorkoutSetsAdapter extends RecyclerView.Adapter<PlayWorkoutSetsAdapter.PlayWorkoutSetsViewHolder> {

    private ArrayList<PlannedExercise> mExercises;
    private Context mContext;

    public PlayWorkoutSetsAdapter(ArrayList<PlannedExercise> exercises, Context context){
        mContext = context;
        mExercises = exercises;
    }

    @Override
    public PlayWorkoutSetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.list_item_play_set;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new PlayWorkoutSetsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayWorkoutSetsViewHolder holder, int position) {
        PlannedExercise exercise = mExercises.get(position);


        boolean isTimedActivity = exercise.getDuration() > 0;

        if (isTimedActivity){
            holder.repsLayout.setVisibility(View.GONE);
            holder.timedLayout.setVisibility(View.VISIBLE);

            holder.timedSetNo.setText(String.valueOf(position + 1) +".");
            holder.timedDuration.setText(String.valueOf(exercise.getDuration()) + " secs");

            if(exercise.getWeight() > 0){
                holder.timedWeight.setText(String.valueOf(exercise.getWeight()));
            } else {
                holder.timedWeight.setVisibility(View.INVISIBLE);
            }

            if(exercise.isComplete()){
                holder.timedCheckButton.setBackground(mContext.getDrawable(R.drawable.checkbox_complete));
                holder.timedCheckButton.setText(null);
            } else {
                holder.timedCheckButton.setBackground(null);
                holder.timedCheckButton.setText(mContext.getString(R.string.start));
            }


        } else {
            holder.timedLayout.setVisibility(View.GONE);
            holder.repsLayout.setVisibility(View.VISIBLE);

            holder.repsSetNo.setText(String.valueOf(position + 1) + ".");
            holder.noOfReps.setText(String.valueOf(exercise.getReps()));
            holder.repsWeight.setText(String.valueOf(exercise.getWeight()));

            if(exercise.isComplete()){
                holder.repsCheckButton.setBackground(mContext.getDrawable(R.drawable.checkbox_complete));
            } else {
                holder.repsCheckButton.setBackground(mContext.getDrawable(R.drawable.checkbox_incomplete));
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mExercises == null){
            return 0;
        } else {
            return mExercises.size();
        }
    }

    public void updateExerciseData(ArrayList<PlannedExercise> exercises){
        mExercises = exercises;
        notifyDataSetChanged();
    }

    class PlayWorkoutSetsViewHolder extends RecyclerView.ViewHolder {

//        Layout for timed activities
        @BindView(R.id.layout_timed)
        LinearLayout timedLayout;

        @BindView(R.id.tv_timed_set_number)
        TextView timedSetNo;

        @BindView(R.id.tv_timed_duration)
        TextView timedDuration;

        @BindView(R.id.et_timed_weight)
        EditText timedWeight;

        @BindView(R.id.btn_timed_check)
        Button timedCheckButton;

//        Layout for reps activities
        @BindView(R.id.layout_reps)
        ConstraintLayout repsLayout;

        @BindView(R.id.tv_reps_set_number)
        TextView repsSetNo;

        @BindView(R.id.et_reps_number_of_reps)
        EditText noOfReps;

        @BindView(R.id.et_reps_weight)
        EditText repsWeight;

        @BindView(R.id.btn_reps_check)
        Button repsCheckButton;

        public PlayWorkoutSetsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
