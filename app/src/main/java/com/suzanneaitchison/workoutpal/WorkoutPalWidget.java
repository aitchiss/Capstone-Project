package com.suzanneaitchison.workoutpal;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class WorkoutPalWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Date lastWorkoutDate) {

        Intent intent = new Intent(context, AuthActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.workout_pal_widget);
        views.setOnClickPendingIntent(R.id.ll_widget_layout, pendingIntent);

        Calendar todayCalendar = Calendar.getInstance();
        Calendar lastWorkoutCalendar = Calendar.getInstance();

        todayCalendar.setTime(Calendar.getInstance().getTime());
        lastWorkoutCalendar.setTime(lastWorkoutDate);

        long daysApart = (todayCalendar.getTime().getTime() - lastWorkoutCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);

        if(daysApart < 4)
        {
            views.setViewVisibility(R.id.tv_widget_message, View.INVISIBLE);
            views.setViewVisibility(R.id.iv_thumbs_up, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.tv_widget_message, View.VISIBLE);
            views.setViewVisibility(R.id.iv_thumbs_up, View.INVISIBLE);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd MMM");
        String date = simpleDateFormat.format(lastWorkoutDate);
        views.setTextViewText(R.id.tv_widget_last_workout_date, date);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        WidgetService.startActionUpdateWidgets(context);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Date mostRecentWorkout){
        for(int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId, mostRecentWorkout);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

