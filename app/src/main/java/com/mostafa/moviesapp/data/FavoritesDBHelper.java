package com.mostafa.moviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mostafa.moviesapp.data.FavoritesContract.ReviewEntry;
import com.mostafa.moviesapp.data.FavoritesContract.FavoriteEntry;
import com.mostafa.moviesapp.data.FavoritesContract.TrailerEntry;

/**
 * Created by Mostafa El-Abady on 1/2/2016.
 */
public class FavoritesDBHelper extends SQLiteOpenHelper {


        private static final int DATABASE_VERSION = 1;

        static final String DATABASE_NAME = "favorites.db";

        public FavoritesDBHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER  NOT NULL, " +
                FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT , " +
                FavoriteEntry.COLUMN_OVERVIEW + " TEXT , " +
                FavoriteEntry.COLUMN_POSTER_PATH + " TEXT , " +
                FavoriteEntry.COLUMN_RELEASE_DATA + " TEXT , " +
                FavoriteEntry.COLUMN_TITLE + " TEXT , " +
                FavoriteEntry.COLUMN_vote_average + " REAL " +
                " );";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +

                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                ReviewEntry.COLUMN_TITLE + " TEXT , " +

                " FOREIGN KEY (" + ReviewEntry.COLUMN_FAVORITE_ID + ") REFERENCES " +
                FavoriteEntry.TABLE_NAME + " (" + FavoriteEntry._ID + ");";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +

                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                TrailerEntry.COLUMN_TRAILER_URL + " TEXT , " +

                " FOREIGN KEY (" + TrailerEntry.COLUMN_FAVORITE_ID + ") REFERENCES " +
                FavoriteEntry.TABLE_NAME + " (" + FavoriteEntry._ID + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoriteEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.ReviewEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}