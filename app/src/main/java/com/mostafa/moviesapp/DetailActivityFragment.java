package com.mostafa.moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mostafa.moviesapp.adapters.TrailersReviewsAdapter;
import com.mostafa.moviesapp.helpers.Utility;
import com.mostafa.moviesapp.models.Movie;
import com.mostafa.moviesapp.models.TrailerReview;
import com.mostafa.moviesapp.tasks.FetchTask;
import com.mostafa.moviesapp.tasks.ParseMoviesTask;
import com.mostafa.moviesapp.tasks.ParseTrailersReviewsTask;
import com.squareup.picasso.Picasso;

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
    private  Movie currentMovie;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

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

        // Fetching data from a parcelable object passed from MainActivity
        Movie movie = getActivity().getIntent().getParcelableExtra("movie");
        if (movie != null) {
            currentMovie = movie;
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

            trailersReviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        TrailerReview currentTrailerReview = (TrailerReview) trailersReviewsAdapter.getItem(position - 1);
                        if (currentTrailerReview.getType() != null && currentTrailerReview.getType().equals(Utility.TrailersReviewsType.Trailer.toString())) {
                            openTrailer(currentTrailerReview.getKey());
                        }
                        else
                        {
                            //Open Review url
                            openLink(currentTrailerReview.getUrl());
                        }
                    } catch (Exception ex) {
                        Log.e(LOG_TAG, "Error ", ex);
                    }
                }

            });
        }

        return rootView;
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

    private void openLink(String link){
        if (link != null && !link.isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(link));
            if (browserIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(browserIntent);
            }
        }
    }


}
