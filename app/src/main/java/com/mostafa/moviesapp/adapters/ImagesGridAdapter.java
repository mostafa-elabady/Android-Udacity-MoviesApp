package com.mostafa.moviesapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mostafa.moviesapp.helpers.Utility;
import com.mostafa.moviesapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mostafa El-Abady on 12/14/2015.
 */

public class ImagesGridAdapter extends BaseAdapter {

    private Context context;

    public void setMovies(Movie[] movies) {
        this.movies = movies;
    }

    private Movie[] movies;

    public ImagesGridAdapter(Context context, Movie[] movies) {
        this.context = context;
        this.movies = movies;
    }

    public int getCount() {
        return movies.length;
    }

    public Object getItem(int position) {
        return movies[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(130, 195));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0,1, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        String movieImageFullPath = String.format(Utility.IMAGE_URL_FORMAT, Utility.IMAGE_SIZE, movies[position].getPosterRelativePath());
        Picasso.with(context).load(movieImageFullPath).into(imageView);
        return imageView;
    }


}
