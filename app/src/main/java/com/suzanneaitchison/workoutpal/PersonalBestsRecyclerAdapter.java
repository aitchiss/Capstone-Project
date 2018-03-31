package com.suzanneaitchison.workoutpal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suzanneaitchison.workoutpal.models.Achievement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suzanne on 29/03/2018.
 */

public class PersonalBestsRecyclerAdapter extends RecyclerView.Adapter<PersonalBestsRecyclerAdapter.PersonalBestViewHolder> {

    ArrayList<Achievement> mAchievements;

    public PersonalBestsRecyclerAdapter(ArrayList<Achievement> achievements){
        mAchievements = achievements;
    }

    @Override
    public PersonalBestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.list_item_personal_best;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new PersonalBestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonalBestViewHolder holder, int position) {
        Achievement achievement = mAchievements.get(position);
        holder.exerciseName.setText(achievement.getExerciseName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        String date = dateFormat.format(achievement.getAchievementDate());
        holder.date.setText(date);
        holder.achievementDetail.setText(String.valueOf(achievement.getWeight()) + "kg");
    }

    @Override
    public int getItemCount() {
        if(mAchievements == null){
            return 0;
        } else {
            return mAchievements.size();
        }
    }

    public void setData(ArrayList<Achievement> achievements){
        mAchievements = achievements;
        notifyDataSetChanged();
    }

    class PersonalBestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_achievement_image)
        ImageView image;

        @BindView(R.id.tv_achievement_exercise_name)
        TextView exerciseName;

        @BindView(R.id.tv_achievement_details)
        TextView achievementDetail;

        @BindView(R.id.tv_achievement_date)
        TextView date;

        public PersonalBestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
