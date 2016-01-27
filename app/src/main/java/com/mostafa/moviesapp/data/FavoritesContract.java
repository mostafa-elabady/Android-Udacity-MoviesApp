package com.mostafa.moviesapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mostafa El-Abady on 1/2/2016.
 *
 * Defines table and column names for the Favorites database.
 */
public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "com.mostafa.moviesapp.FavoritesProvider";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_REVIEWS = "reviews";


    public static final class FavoriteEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATA = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_vote_average = "vote_average";


    }

    public static final class TrailerReviewEntry implements BaseColumns {


        // Table name
        public static final String TABLE_NAME = "trailerreview";

        // to use for foreign key
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_REVIEW_CONTENT = "review_text";
        public static final String COLUMN_REVIEW_AUTHOR = "review_author";
        public static final String COLUMN_TRAILER_KEY = "trailer_key";
        public static final String COLUMN_TRAILER_SITE = "trailer_site";
        public static final String COLUMN_TYPE = "trailerreview_type";

    }

    public static final class ReviewEntry implements BaseColumns {


        // Table name
        public static final String TABLE_NAME = "review";

        // to use for foreign key
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_CONTENT = "review_text";
        public static final String COLUMN_AUTHOR = "review_author";

    }

    public static final class TrailerEntry implements BaseColumns {


        // Table name
        public static final String TABLE_NAME = "trailer";

        // to use for foreign key
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TRAILER_KEY = "trailer_key";
        public static final String COLUMN_TRAILER_SITE = "trailer_site";


    }


}
