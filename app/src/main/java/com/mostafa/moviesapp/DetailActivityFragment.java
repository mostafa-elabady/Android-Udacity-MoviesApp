package com.mostafa.moviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mostafa.moviesapp.adapters.TrailersReviewsAdapter;
import com.mostafa.moviesapp.data.FavoritesContract;
import com.mostafa.moviesapp.data.FavoritesDBHelper;
import com.mostafa.moviesapp.helpers.Utility;
import com.mostafa.moviesapp.models.Movie;
import com.mostafa.moviesapp.models.TrailerReview;
import com.mostafa.moviesapp.tasks.FetchTask;
import com.mostafa.moviesapp.tasks.ParseTrailersReviewsTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */

public class DetailActivityFragment extends Fragment {


    private ListView trailersReviewsListView;
    private TextView titleTextView;
    private ImageView movieImageView;
    private TextView overviewTextView;
    private TextView releaseDateTextView;
    private TextView averageVoteTextView;
    private TrailersReviewsAdapter trailersReviewsAdapter;
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private Movie currentMovie;
    private FloatingActionButton favoritesFloatingActionButton;

    private boolean isMovieFavorite = false;

    public DetailActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        favoritesFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.favorites_fab);
        favoritesFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMovieFavorite) {
                    deleteMovieFromDB();
                } else {
                    insertMovieToDB(currentMovie);
                }
                checkIfMovieAlreadyFavorited(currentMovie.getId());
                updateFloatingActionButtonSource();
            }
        });
        trailersReviewsListView = (ListView) rootView.findViewById(R.id.movie_trailers_reviews_list);
        trailersReviewsAdapter = new TrailersReviewsAdapter(getContext());

        View header = View.inflate(getActivity(), R.layout.movieheader, null);
        trailersReviewsListView.addHeaderView(header);
        trailersReviewsListView.setAdapter(trailersReviewsAdapter);

        titleTextView = (TextView) header.findViewById(R.id.movie_detail_title);
        overviewTextView = (TextView) header.findViewById(R.id.movie_detail_overview);
        movieImageView = (ImageView) header.findViewById(R.id.movie_detail_image);
        releaseDateTextView = (TextView) header.findViewById(R.id.movie_detail_releasedate);
        averageVoteTextView = (TextView) header.findViewById(R.id.movie_detail_avergatevote);


        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetching data from a parcelable object passed from MainActivity
        Bundle bundle = getArguments();
        Movie movie = bundle.getParcelable("movie");
        if (movie != null) {
            currentMovie = movie;
            checkIfMovieAlreadyFavorited(movie.getId());
            updateFloatingActionButtonSource();
            titleTextView.setText(movie.getTitle());
            overviewTextView.setText(movie.getOverview());
            String movieImageFullPath = String.format(Utility.IMAGE_URL_FORMAT, Utility.IMAGE_SIZE, movie.getPosterRelativePath());
            Picasso.with(getActivity()).load(movieImageFullPath).into(movieImageView);
            String fullReleaseDate = movie.getReleaseDate();
            if (fullReleaseDate.length() > 4)
                releaseDateTextView.setText(fullReleaseDate.substring(0, 4));
            else
                releaseDateTextView.setText(fullReleaseDate);

            averageVoteTextView.setText(String.format("%s / 10", movie.getVoteAverage()));
            if (Utility.isConnected(getContext())) {
                ParseTrailersReviewsTask parseTrailersTask = new ParseTrailersReviewsTask(getActivity(), trailersReviewsAdapter, Utility.TrailersReviewsType.Trailer);
                FetchTask fetchTrailersTask = new FetchTask(parseTrailersTask);
                String trailersUrl = String.format(Utility.VIDEOS_API_URL, movie.getId(), BuildConfig.MOVIES_DB_API_KEY);
                fetchTrailersTask.execute(trailersUrl);

                ParseTrailersReviewsTask parseReviewsTask = new ParseTrailersReviewsTask(getActivity(), trailersReviewsAdapter, Utility.TrailersReviewsType.Review);
                FetchTask fetchReviewsTask = new FetchTask(parseReviewsTask);
                String reviewsurl = String.format(Utility.REVIEWS_API_URL, movie.getId(), BuildConfig.MOVIES_DB_API_KEY);
                fetchReviewsTask.execute(reviewsurl);
            }
            // insertMovieToDB(movie);
            trailersReviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        TrailerReview currentTrailerReview = (TrailerReview) trailersReviewsAdapter.getItem(position - 1);
                        if (currentTrailerReview.getType() != null && currentTrailerReview.getType().equals(Utility.TrailersReviewsType.Trailer.toString())) {
                            openTrailer(currentTrailerReview.getKey());
                        } else {
                            //Open Review url
                            openLink(currentTrailerReview.getUrl());
                        }
                    } catch (Exception ex) {
                        Log.e(LOG_TAG, "Error ", ex);
                    }
                }

            });
        }
    }


    private boolean insertMovieToDB(Movie currentMovie) {
        FavoritesDBHelper dbHelper = FavoritesDBHelper.getInstance(getActivity());
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        try {
            //Insert Movie
            ContentValues movieValues = new ContentValues();
            movieValues.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID, currentMovie.getId());
            movieValues.put(FavoritesContract.FavoriteEntry.COLUMN_OVERVIEW, currentMovie.getOverview());
            movieValues.put(FavoritesContract.FavoriteEntry.COLUMN_POSTER_PATH, currentMovie.getPosterRelativePath());
            movieValues.put(FavoritesContract.FavoriteEntry.COLUMN_RELEASE_DATA, currentMovie.getReleaseDate());
            movieValues.put(FavoritesContract.FavoriteEntry.COLUMN_TITLE, currentMovie.getTitle());
            movieValues.put(FavoritesContract.FavoriteEntry.COLUMN_vote_average, currentMovie.getVoteAverage());
            writableDatabase.beginTransaction();
            long id = writableDatabase.insert(FavoritesContract.FavoriteEntry.TABLE_NAME, null, movieValues);
            if (id == -1)
                return false;

            //Insert Trailers and Reviews
            if (trailersReviewsAdapter != null) {
                ArrayList<TrailerReview> items = trailersReviewsAdapter.getItems();
                if (items != null && items.size() > 0) {
                    for (TrailerReview tv : items) {
                        ContentValues trailerreviewsValues = new ContentValues();
                        trailerreviewsValues.put(FavoritesContract.TrailerReviewEntry.COLUMN_MOVIE_ID, currentMovie.getId());
                        trailerreviewsValues.put(FavoritesContract.TrailerReviewEntry.COLUMN_TYPE, tv.getType());
                        trailerreviewsValues.put(FavoritesContract.TrailerReviewEntry.COLUMN_TRAILER_KEY, tv.getKey());
                        trailerreviewsValues.put(FavoritesContract.TrailerReviewEntry.COLUMN_TRAILER_SITE, tv.getSite());
                        trailerreviewsValues.put(FavoritesContract.TrailerReviewEntry.COLUMN_REVIEW_CONTENT, tv.getContent());
                        trailerreviewsValues.put(FavoritesContract.TrailerReviewEntry.COLUMN_REVIEW_AUTHOR, tv.getAuthor());

                        writableDatabase.beginTransaction();
                        long id2 = writableDatabase.insert(FavoritesContract.TrailerReviewEntry.TABLE_NAME, null, trailerreviewsValues);
                        if (id2 != -1) {

                        }
                    }
                }
            }

        } catch (Exception ex) {
            writableDatabase.endTransaction();
            return false;
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();


        }
        return true;
    }

    private void deleteMovieFromDB() {
        FavoritesDBHelper dbHelper = FavoritesDBHelper.getInstance(getActivity());
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        try {

            writableDatabase.delete(FavoritesContract.FavoriteEntry.TABLE_NAME, FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID + "=" + currentMovie.getId(), null);
            writableDatabase.delete(FavoritesContract.TrailerReviewEntry.TABLE_NAME, FavoritesContract.TrailerReviewEntry.COLUMN_MOVIE_ID + "=" + currentMovie.getId(), null);
        } catch (Exception ex) {

        } finally {
            writableDatabase.close();
        }
    }

    private boolean checkIfMovieAlreadyFavorited(int movieId) {
        FavoritesDBHelper dbHelper = FavoritesDBHelper.getInstance(getActivity());
        try {
            Cursor cursor = null;
            String sql = "SELECT * FROM " + FavoritesContract.FavoriteEntry.TABLE_NAME + " WHERE " + FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID + "=" + movieId;
            cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                isMovieFavorite = true;
                // Found
                return true;
            } else {
                isMovieFavorite = false;
                // Not Found
                return false;
            }
        } catch (Exception ex) {
            return false;
        } finally {

        }
    }

    private void updateFloatingActionButtonSource() {
        if (favoritesFloatingActionButton != null) {
            if (isMovieFavorite) {
                favoritesFloatingActionButton.setImageResource(R.mipmap.ic_star_white);
            } else {
                favoritesFloatingActionButton.setImageResource(R.mipmap.ic_star_yellow);
            }
        }
    }

    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_detail, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_share) {
//           // if(trailersReviewsAdapter.ge)
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void shareMovie(String key) {
        String youTubeLink = "https://www.youtube.com/watch?v=" + key;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, youTubeLink);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share To"));
    }


    private void openTrailer(String key) {
        if (key != null && !key.isEmpty()) {
            String link = "https://www.youtube.com/watch?v=" + key;
            openLink(link);
        }
    }

    private void openLink(String link) {
        if (link != null && !link.isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(link));
            if (browserIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(browserIntent);
            }
        }
    }


}
