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
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterRelativePath() {
        return posterRelativePath;
    }

    public void setPosterRelativePath(String posterRelativePath) {
        this.posterRelativePath = posterRelativePath;
    }
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    private int id;
    private String title;
    private String overview;
    private String posterRelativePath;
    private  String releaseDate;
    private int voteCount;
    private double voteAverage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterRelativePath);
        dest.writeInt(id);
        dest.writeString(releaseDate);
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);

    }

    /**
     * Retrieving Movie data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Movie(Parcel in) {
        this.title = in.readString();
        this.overview = in.readString();
        this.posterRelativePath = in.readString();
        this.id = in.readInt();
        this.releaseDate = in.readString();
        this.voteCount = in.readInt();
        this.voteAverage = in.readDouble();
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
