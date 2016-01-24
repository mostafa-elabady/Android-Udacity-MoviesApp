package com.mostafa.moviesapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mostafa.moviesapp.adapters.TrailersReviewsAdapter;
import com.mostafa.moviesapp.models.TrailerReview;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Mostafa El-Abady on 1/23/2016.
 */
public class ParseTrailersReviewsTask extends AsyncTask<Object, Void, TrailerReview[]> {

    private TrailersReviewsAdapter moviesAdapter;
    private Context context;
    private final String LOG_TAG = FetchTask.class.getSimpleName();

    public ParseTrailersReviewsTask(Context context, TrailersReviewsAdapter moviesAdapter) {
        this.context = context;
        this.moviesAdapter = moviesAdapter;
    }

    @Override
    protected TrailerReview[] doInBackground(Object... params) {

        if (params.length == 0) {
            return null;
        }

        final String MOVIES_LIST_KEY = "results";
        final String ID_KEY = "id";

        try {
            String jsonStrring = (String) params[0];

            JSONObject rootJson = new JSONObject(jsonStrring);

            JSONArray moviesArray = rootJson.getJSONArray(MOVIES_LIST_KEY);
            TrailerReview[] trailersReviews = new TrailerReview[moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); i++) {

                JSONObject trailerReviewJSON = moviesArray.getJSONObject(i);
                trailersReviews[i] = new TrailerReview();
                trailersReviews[i].setId(trailerReviewJSON.getString(ID_KEY));
                trailersReviews[i].setKey(trailerReviewJSON.getString("key"));
            }
            return trailersReviews;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {

        }
    }


    @Override
    protected void onPostExecute(TrailerReview[] trailerreviews) {
        super.onPostExecute(trailerreviews);
        if (trailerreviews != null) {
            moviesAdapter.addTrailersReviews(trailerreviews);
            moviesAdapter.notifyDataSetChanged();
        }
    }
}
