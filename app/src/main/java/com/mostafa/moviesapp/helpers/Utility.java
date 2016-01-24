package com.mostafa.moviesapp.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Mostafa El-Abady on 12/14/2015.
 */

public class Utility {

    public static String IMAGE_SIZE = "w185";
    public static String IMAGE_URL_FORMAT = "http://image.tmdb.org/t/p/%1s%2s";
    public static String YOUTUBE_THUMBNAIL_URL_FORMAT = "http://img.youtube.com/vi/%s/0.jpg";
    public static int PAGE_DEFAULT_VALUE = 1;
    public static String MOVIES_API_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=%s&page=%d";
    public static String REVIEWS_API_URL = "http://api.themoviedb.org/3/movie/%s/reviews?&api_key=%s";
    public static String VIDEOS_API_URL = "http://api.themoviedb.org/3/movie/%s/reviews?&api_key=%s";


    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
