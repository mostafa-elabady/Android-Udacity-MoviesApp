package com.mostafa.moviesapp.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mostafa.moviesapp.models.TrailerReview;

/**
 * Created by Mostafa El-Abady on 1/23/2016.
 */
public class TrailersReviewsAdapter extends BaseAdapter {


    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void addTrailersReviews(TrailerReview[] trailerreviews) {
    }
}
