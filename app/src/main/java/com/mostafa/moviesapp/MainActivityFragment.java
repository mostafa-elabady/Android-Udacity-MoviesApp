package com.mostafa.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.mostafa.moviesapp.adapters.ImagesGridAdapter;
import com.mostafa.moviesapp.helpers.Utility;
import com.mostafa.moviesapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private GridView moviesGridView;
    private ImagesGridAdapter moviesAdapter;
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private FetchMoviesTask fetchFromServerTask;
    private ParseMoviesTask parseMoviesTask;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            if (isConnected(getActivity())) {
                parseMoviesTask = new ParseMoviesTask();
                fetchFromServerTask = new FetchMoviesTask(parseMoviesTask);
                String FORECAST_URL = String.format(Utility.API_URL, BuildConfig.OPEN_WEATHER_MAP_API_KEY, Utility.PAGE_DEFAULT_VALUE);
                fetchFromServerTask.execute(FORECAST_URL);
            } else {
                Toast.makeText(getActivity(), R.string.NOInternetConnection, Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        moviesGridView = (GridView) rootView.findViewById(R.id.maingridView);
        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie item = (Movie) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("movie", item);
                //Start details activity
                startActivity(intent);
            }
        });
        if (isConnected(getActivity())) {
            parseMoviesTask = new ParseMoviesTask();
            fetchFromServerTask = new FetchMoviesTask(parseMoviesTask);
            String FORECAST_URL = String.format(Utility.API_URL, BuildConfig.OPEN_WEATHER_MAP_API_KEY ,  Utility.PAGE_DEFAULT_VALUE);
            fetchFromServerTask.execute(FORECAST_URL);
        } else {
            Toast.makeText(getActivity(), R.string.NOInternetConnection, Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String> {

        private AsyncTask parseAsyncTask;


        public FetchMoviesTask(AsyncTask parseAsyncTask) {
            this.parseAsyncTask = parseAsyncTask;
        }

        @Override
        protected String doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonString = null;

            try {
                Uri builtUri = Uri.parse(params[0]);

                URL url = new URL(builtUri.toString());

                // Create the request to MoviesDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonString = buffer.toString();
                return moviesJsonString;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            if (parseAsyncTask != null && jsonString != null) {
                try {
                    Log.d(LOG_TAG, "json :" + jsonString);
                    parseAsyncTask.execute(jsonString);
                } catch (Exception ex) {
                    Log.e(LOG_TAG, "Error ", ex);
                }

            }
        }
    }

    public class ParseMoviesTask extends AsyncTask<Object, Void, Movie[]> {

        public ParseMoviesTask() {
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
                moviesAdapter = new ImagesGridAdapter(getActivity(), movies);
                if (moviesGridView != null) {
                    moviesGridView.setAdapter(moviesAdapter);
                }
            }
        }
    }
}
