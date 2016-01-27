package com.mostafa.moviesapp.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mostafa El-Abady on 1/2/2016.
 */
public class FetchTask extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = FetchTask.class.getSimpleName();

    private AsyncTask parseAsyncTask;


    public FetchTask(AsyncTask parseAsyncTask) {
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
                Log.e(LOG_TAG, "Error " + ex.getMessage() + " : " + ex.getStackTrace());
            }

        }
    }
}