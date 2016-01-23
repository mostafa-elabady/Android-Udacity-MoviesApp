package com.mostafa.moviesapp;

import android.sax.RootElement;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mostafa.moviesapp.adapters.TrailersReviewsAdapter;
import com.mostafa.moviesapp.helpers.Utility;
import com.mostafa.moviesapp.models.Movie;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */

public class DetailActivityFragment extends Fragment {


    private ListView trailersReviewsListView;
    private TextView titleTextView;
    private ImageView movieImageView;
    private TextView overviewTextView;
    private  TextView releaseDateTextView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        trailersReviewsListView = (ListView)rootView.findViewById(R.id.movie_trailers_reviews_list);

        View header = View.inflate(getActivity(), R.layout.movieheader, null);
        trailersReviewsListView.addHeaderView(header);
        trailersReviewsListView.setAdapter(new TrailersReviewsAdapter());

        titleTextView = (TextView) header.findViewById(R.id.movie_detail_title);
        overviewTextView = (TextView) header.findViewById(R.id.movie_detail_overview);
        movieImageView = (ImageView) header.findViewById(R.id.movie_detail_image);
        releaseDateTextView = (TextView)header.findViewById(R.id.movie_detail_releasedate);

        // Fetching data from a parcelable object passed from MainActivity
        Movie movie = getActivity().getIntent().getParcelableExtra("movie");
        if (movie != null) {
            titleTextView.setText(movie.getTitle());
            overviewTextView.setText(movie.getOverview());
            String movieImageFullPath = String.format(Utility.IMAGE_URL_FORMAT, Utility.IMAGE_SIZE, movie.getPosterRelativePath());
            Picasso.with(getActivity()).load(movieImageFullPath).into(movieImageView);
            String fullReleaseDate = movie.getReleaseDate();
            if(fullReleaseDate.length()>4)
                releaseDateTextView.setText(fullReleaseDate.substring(0,4));
                else
            releaseDateTextView.setText(fullReleaseDate);
        }

        return rootView;
    }
}
