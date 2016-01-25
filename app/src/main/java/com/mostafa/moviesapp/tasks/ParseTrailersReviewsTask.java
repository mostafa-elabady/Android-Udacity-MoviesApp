package com.mostafa.moviesapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mostafa.moviesapp.adapters.TrailersReviewsAdapter;
import com.mostafa.moviesapp.helpers.Utility;
import com.mostafa.moviesapp.models.TrailerReview;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Mostafa El-Abady on 1/23/2016.
 */
public class ParseTrailersReviewsTask extends AsyncTask<Object, Void, TrailerReview[]> {

    private TrailersReviewsAdapter moviesAdapter;
    private Context context;
    private Utility.TrailersReviewsType type;
    private final String LOG_TAG = FetchTask.class.getSimpleName();

    public ParseTrailersReviewsTask(Context context, TrailersReviewsAdapter moviesAdapter, Utility.TrailersReviewsType type) {
        this.context = context;
        this.moviesAdapter = moviesAdapter;
        this.type = type;
    }

    @Override
    protected TrailerReview[] doInBackground(Object... params) {

        if (params.length == 0) {
            return null;
        }

        final String MOVIES_LIST_KEY = "results";
        final String ID_KEY = "id";
        final  String TRAILER_NAME_KEY = "name";
        final  String TRAILER_SITE_KEY ="site";
        final  String TRAILER_KEY_KEY = "key";


        final  String REVIEW_AUTHOR_KEY = "author";
        final  String REVIEW_CONTENT_KEY = "content";
        final  String REVIEW_URL_KEY = "url";



        try {
            String jsonStrring = (String) params[0];

            JSONObject rootJson = new JSONObject(jsonStrring);

            JSONArray moviesArray = rootJson.getJSONArray(MOVIES_LIST_KEY);
            TrailerReview[] trailersReviews = new TrailerReview[moviesArray.length()];

            if (type == Utility.TrailersReviewsType.Trailer) {
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject trailerReviewJSON = moviesArray.getJSONObject(i);
                    trailersReviews[i] = new TrailerReview();
                    trailersReviews[i].setId(trailerReviewJSON.getString(ID_KEY));
                    trailersReviews[i].setKey(trailerReviewJSON.getString(TRAILER_KEY_KEY));
                    trailersReviews[i].setType(type.toString());
                    trailersReviews[i].setName(trailerReviewJSON.getString(TRAILER_NAME_KEY));
                    trailersReviews[i].setSite(trailerReviewJSON.getString(TRAILER_SITE_KEY));
                }
            } else {
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject trailerReviewJSON = moviesArray.getJSONObject(i);
                    trailersReviews[i] = new TrailerReview();
                    trailersReviews[i].setId(trailerReviewJSON.getString(ID_KEY));
                    trailersReviews[i].setType(type.toString());
                    trailersReviews[i].setAuthor(trailerReviewJSON.getString(REVIEW_AUTHOR_KEY));
                    trailersReviews[i].setUrl(trailerReviewJSON.getString(REVIEW_URL_KEY));
                    trailersReviews[i].setContent(trailerReviewJSON.getString(REVIEW_CONTENT_KEY));
                }
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
