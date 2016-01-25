package com.mostafa.moviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mostafa.moviesapp.R;
import com.mostafa.moviesapp.helpers.Utility;
import com.mostafa.moviesapp.models.TrailerReview;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by Mostafa El-Abady on 1/23/2016.
 */
public class TrailersReviewsAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<TrailerReview>  trailerReviews;

    public  TrailersReviewsAdapter(Context context){
        this.context = context;
        trailerReviews = new ArrayList<>();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getCount() {
        return trailerReviews.size();
    }

    @Override
    public Object getItem(int position) {
        return trailerReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trailer_review, parent, false);
        }
        FrameLayout trailerLayout = (FrameLayout)convertView.findViewById(R.id.movie_trailer_item_layout);
        ImageView trailerImageView =(ImageView)convertView.findViewById(R.id.movie_trailer_item_image);
        TrailerReview currentTrailerReview = this.trailerReviews.get(position);
        if(currentTrailerReview.getType() != null && currentTrailerReview.getType().equals(Utility.TrailersReviewsType.Trailer.toString())){
            // Trailer
            trailerLayout.setVisibility(View.VISIBLE);
            String thumbnailImageFullPath = String.format(Utility.YOUTUBE_THUMBNAIL_URL_FORMAT, currentTrailerReview.getKey());
            Picasso.with(context).load(thumbnailImageFullPath).into(trailerImageView);

        }
        else {
            //Review

            trailerLayout.setVisibility(View.GONE);
            TextView contentTextView = (TextView)convertView.findViewById(R.id.movie_review_textView);
            TextView authorTextView = (TextView)convertView.findViewById(R.id.movie_review_author_textView);

            String reviewContent = currentTrailerReview.getContent();
            if(reviewContent != null && !reviewContent.isEmpty() && reviewContent.length()>250){
                //Show only 250 Chars, user can view all if he clicks on the review
                reviewContent = reviewContent.substring(0,250)+"...";
            }
            contentTextView.setText(reviewContent);
            authorTextView.setText(currentTrailerReview.getAuthor());
        }
        return  convertView;
    }

    public void addTrailersReviews(TrailerReview[] trailerreviews) {
        for(TrailerReview t : trailerreviews){
            this.trailerReviews.add(t);
        }
        notifyDataSetChanged();
    }
}
