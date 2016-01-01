package com.mostafa.moviesapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mostafa.moviesapp.helpers.Utility;
import com.mostafa.moviesapp.models.Movie;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */

public class DetailActivityFragment extends Fragment {

    private TextView titleTextView;
    private ImageView movieImageView;
    private TextView overviewTextView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        titleTextView = (TextView) rootView.findViewById(R.id.movie_detail_title);
        overviewTextView = (TextView) rootView.findViewById(R.id.movie_detail_overview);
        movieImageView = (ImageView) rootView.findViewById(R.id.movie_detail_image);

        // Fetching data from a parcelable object passed from MainActivity
        Movie movie = getActivity().getIntent().getParcelableExtra("movie");
        if (movie != null) {
            titleTextView.setText(movie.getTitle());
            overviewTextView.setText(movie.getOverview());
            String movieImageFullPath = String.format(Utility.IMAGE_URL_FORMAT, Utility.IMAGE_SIZE, movie.getPosterRelativePath());
            Picasso.with(getActivity()).load(movieImageFullPath).into(movieImageView);
        }
        return rootView;
    }
}
