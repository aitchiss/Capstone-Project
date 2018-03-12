package com.suzanneaitchison.workoutpal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suzanneaitchison.workoutpal.models.PlannedExercise;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 12/03/2018.
 */

public class WorkoutDetailRecyclerAdapter extends RecyclerView.Adapter<WorkoutDetailRecyclerAdapter.WorkoutDetailRecyclerAdapterViewHolder> {


    private ArrayList<PlannedExercise> mExerciseData;
    private Context mContext;

    public WorkoutDetailRecyclerAdapter(ArrayList<PlannedExercise> exercises, Context context){
        mExerciseData = exercises;
        mContext = context;
    }

    @Override
    public WorkoutDetailRecyclerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.list_item_added_exercise;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new WorkoutDetailRecyclerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkoutDetailRecyclerAdapterViewHolder holder, int position) {
        holder.exerciseName.setText(mExerciseData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if(mExerciseData == null){
            return 0;
        } else {
            return mExerciseData.size();
        }
    }

    public void setExerciseData(ArrayList<PlannedExercise> exercises){
        mExerciseData = exercises;
        notifyDataSetChanged();
    }

    class WorkoutDetailRecyclerAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_exercise_name)
        TextView exerciseName;

        public WorkoutDetailRecyclerAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
