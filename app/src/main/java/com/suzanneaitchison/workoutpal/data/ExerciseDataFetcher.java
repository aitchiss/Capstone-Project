package com.suzanneaitchison.workoutpal.data;

import android.app.LoaderManager;
import android.content.Context;
import android.util.Log;

import com.suzanneaitchison.workoutpal.MainActivity;
import com.suzanneaitchison.workoutpal.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by suzanne on 03/03/2018.
 */

public class ExerciseDataFetcher {

    private final String REQUEST_EXERCISES_URL = "https://wger.de/api/v2/exercise/?language=2&status=2";
    private final String EXERCISE_DETAIL_REQUEST_URL = "api/v2/exerciseiinfo/";
    private final String AUTH_HEADER_NAME = "Authorization";
    private final int EXERCISE_LOADER_ID = 100;


    public String fetchLatestApiData(Context context) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(REQUEST_EXERCISES_URL)
                .header(AUTH_HEADER_NAME, "Token " + context.getResources().getString(R.string.wger_api_key))
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String fetchExerciseDetail(Context context, int id) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(EXERCISE_DETAIL_REQUEST_URL + id + "/")
                .header(AUTH_HEADER_NAME, "Token " + context.getResources().getString(R.string.wger_api_key))
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
