package com.mostafa.moviesapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mostafa.moviesapp.adapters.ImagesGridAdapter;
import com.mostafa.moviesapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Mostafa El-Abady on 1/2/2016.
 */
public class ParseMoviesTask extends AsyncTask<Object, Void, Movie[]> {

    private ImagesGridAdapter moviesAdapter;
    private  Context context;
    private final String LOG_TAG = FetchTask.class.getSimpleName();

    public ParseMoviesTask(Context context, ImagesGridAdapter moviesAdapter) {
        this.context = context;
        this.moviesAdapter = moviesAdapter;
    }

    @Override
    protected Movie[] doInBackground(Object... params) {

        if (params.length == 0) {
            return null;
        }

        final String MOVIES_LIST_KEY = "results";
        final String ID_KEY = "id";
        final String TITLE_KEY = "title";
        final String OVERVIEW_KEY = "overview";
        final String POSTERPATH_KEY = "poster_path";

        final String RELEASE_DATE_KEY = "release_date";
        final String VOTE_Count_KEY = "vote_count";
        final String VOTE_AVERAGE_KEY = "vote_average";

        try {
            String jsonStrring = (String) params[0];

            JSONObject rootJson = new JSONObject(jsonStrring);

            JSONArray moviesArray = rootJson.getJSONArray(MOVIES_LIST_KEY);
            Movie[] movies = new Movie[moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); i++) {

                JSONObject movieJSON = moviesArray.getJSONObject(i);

                movies[i] = new Movie();
                movies[i].setId(movieJSON.getInt(ID_KEY));
                movies[i].setOverview(movieJSON.getString(OVERVIEW_KEY));
                movies[i].setPosterRelativePath(movieJSON.getString(POSTERPATH_KEY));
                movies[i].setTitle(movieJSON.getString(TITLE_KEY));

                movies[i].setReleaseDate(movieJSON.getString(RELEASE_DATE_KEY));
                movies[i].setVoteCount(movieJSON.getInt(VOTE_Count_KEY));
                movies[i].setVoteAverage(movieJSON.getDouble(VOTE_AVERAGE_KEY));
            }
            return movies;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {

        }
    }


    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        if (movies != null) {
            moviesAdapter.setMovies(movies);
            moviesAdapter.notifyDataSetChanged();

        }
    }
}
