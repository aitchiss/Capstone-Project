package com.suzanneaitchison.workoutpal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suzanneaitchison.workoutpal.models.Workout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 12/03/2018.
 */

public class WorkoutListRecyclerAdapter extends RecyclerView.Adapter<WorkoutListRecyclerAdapter.WorkoutListViewHolder> {

    ArrayList<Workout> mWorkoutData;

    public WorkoutListRecyclerAdapter(ArrayList<Workout> workouts){
        mWorkoutData = workouts;
    }

    @Override
    public WorkoutListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.list_item_workout_name;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new WorkoutListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkoutListViewHolder holder, int position) {
        holder.workoutName.setText(mWorkoutData.get(position).getWorkoutName());
    }

    public void setWorkoutData(ArrayList<Workout> workouts){
        mWorkoutData = workouts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mWorkoutData == null){
            return 0;
        } else {
            return mWorkoutData.size();
        }
    }

    class WorkoutListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_workout_name)
        TextView workoutName;

        public WorkoutListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
