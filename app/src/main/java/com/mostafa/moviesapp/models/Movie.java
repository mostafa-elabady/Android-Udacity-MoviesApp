package com.mostafa.moviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mostafa El-Abady on 12/14/2015.
 */
public class Movie implements Parcelable {

    public Movie() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getPosterRelativePath() {
        return PosterRelativePath;
    }

    public void setPosterRelativePath(String posterRelativePath) {
        PosterRelativePath = posterRelativePath;
    }

    private int Id;
    private String Title;
    private String Overview;
    private String PosterRelativePath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Title);
        dest.writeString(Overview);
        dest.writeString(PosterRelativePath);
        dest.writeInt(Id);

    }

    /**
     * Retrieving Movie data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Movie(Parcel in) {
        this.Title = in.readString();
        this.Overview = in.readString();
        this.PosterRelativePath = in.readString();
        this.Id = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
