package com.suzanneaitchison.workoutpal;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.suzanneaitchison.workoutpal.data.FirebaseDatabaseHelper;
import com.suzanneaitchison.workoutpal.models.User;

public class WidgetService extends IntentService {

    public static final String ACTION_UPDATE_WIDGETS = "com.suzanneaitchison.workoutpal.action.update_widgets";

    public WidgetService(){
        super("WidgetService");
    }

    public static void startActionUpdateWidgets(Context context){
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_UPDATE_WIDGETS.equals(action)){
                handleActionUpdateWidgets();
            }
        }
    }

    private void handleActionUpdateWidgets(){
        User user = FirebaseDatabaseHelper.getUser();
        if(user != null && user.getLastWorkoutCompletedDate() != null){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WorkoutPalWidget.class));
            WorkoutPalWidget.updateWidgets(this, appWidgetManager, appWidgetIds, user.getLastWorkoutCompletedDate());
        }

    }
}
