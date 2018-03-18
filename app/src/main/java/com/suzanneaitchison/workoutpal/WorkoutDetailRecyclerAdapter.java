package com.suzanneaitchison.workoutpal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suzanneaitchison.workoutpal.models.PlannedExercise;
import com.suzanneaitchison.workoutpal.models.WorkoutEntry;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 12/03/2018.
 */

public class WorkoutDetailRecyclerAdapter extends RecyclerView.Adapter<WorkoutDetailRecyclerAdapter.WorkoutDetailRecyclerAdapterViewHolder> {


    private ArrayList<WorkoutEntry> mWorkoutEntryData;
    private Context mContext;

    public WorkoutDetailRecyclerAdapter(ArrayList<WorkoutEntry> entries, Context context){
        mWorkoutEntryData = entries;
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
        WorkoutEntry entry = mWorkoutEntryData.get(position);
        boolean isTimed = entry.getDuration() != 0;

        String detailText = String.valueOf(entry.getSets()) + " x ";

        if(isTimed){
            holder.exerciseImage.setBackgroundColor(mContext.getResources().getColor(R.color.timedExercise));
            holder.exerciseImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_timer));
            String seconds = String.valueOf(entry.getDuration());
            detailText += seconds + "s";
        } else {
            holder.exerciseImage.setBackgroundColor(mContext.getResources().getColor(R.color.repsExercise));
            holder.exerciseImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_barbell));
            String reps = String.valueOf(entry.getReps());
            detailText += reps + " reps";
        }

        if(entry.getWeight() != 0){
            detailText += " @ " + String.valueOf(entry.getWeight()) + "kg";
        }
        holder.exerciseDetail.setText(detailText);
        holder.exerciseName.setText(mWorkoutEntryData.get(position).getExerciseName());
    }

    @Override
    public int getItemCount() {
        if(mWorkoutEntryData == null){
            return 0;
        } else {
            return mWorkoutEntryData.size();
        }
    }

    public void setWorkoutEntryData(ArrayList<WorkoutEntry> entries){
        mWorkoutEntryData = entries;
        notifyDataSetChanged();
    }

    class WorkoutDetailRecyclerAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_exercise_name)
        TextView exerciseName;

        @BindView(R.id.tv_exercise_detail)
        TextView exerciseDetail;

        @BindView(R.id.iv_entry_image)
        ImageView exerciseImage;

        public WorkoutDetailRecyclerAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
