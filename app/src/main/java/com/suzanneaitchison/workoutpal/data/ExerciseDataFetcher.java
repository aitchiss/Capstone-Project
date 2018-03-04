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
    private final String EXERCISE_IMAGE_REQUEST_URL = "https://wger.de/api/v2/exerciseimage/?exercise=";
    private final String EXERCISE_CATEGORY_REQUEST_URL = "https://wger.de/api/v2/exerciseinfo/";
    private final String AUTH_HEADER_NAME = "Authorization";


    public String fetchLatestApiData(Context context, int pageNumber) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(REQUEST_EXERCISES_URL + "&page=" + pageNumber)
                .header(AUTH_HEADER_NAME, "Token " + context.getResources().getString(R.string.wger_api_key))
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String fetchExerciseImage(Context context, int id) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(EXERCISE_IMAGE_REQUEST_URL + id)
                .header(AUTH_HEADER_NAME, "Token " + context.getResources().getString(R.string.wger_api_key))
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String fetchExerciseCategory(Context context, int id) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(EXERCISE_CATEGORY_REQUEST_URL + id)
                .header(AUTH_HEADER_NAME, "Token " + context.getResources().getString(R.string.wger_api_key))
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
