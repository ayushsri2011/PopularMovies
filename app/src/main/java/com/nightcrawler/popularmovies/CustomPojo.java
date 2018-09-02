package com.nightcrawler.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomPojo implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CustomPojo createFromParcel(Parcel in) {
            return new CustomPojo(in);
        }

        public CustomPojo[] newArray(int size) {
            return new CustomPojo[size];
        }
    };

    public CustomPojo(Parcel in) {
        this.poster_path = in.readString();
        this.title = in.readString();
        this.release_date = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readString();
        this.movieID = in.readString();
        this.category = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster_path);
        dest.writeString(this.title);
        dest.writeString(this.release_date);
        dest.writeString(this.overview);
        dest.writeString(this.vote_average);
        dest.writeString(this.movieID);
        dest.writeString(this.category);
    }

    @Override
    public String toString() {
        return "movie{" +
                "poster_path='" + poster_path + '\'' +
                ", title='" + title + '\'' +
                ", release_date='" + release_date + '\'' +
                ", overview='" + overview + '\'' +
                ", movieID='" + movieID + '\'' +
                ", vote_average='" + vote_average + '\'' +
                ", category='" + category + '\'' +
                '}';
    }


    //POJO class consists of get method and set method
    private String poster_path;
    private String title;
    private String release_date;
    private String overview;
    private String vote_average;
    private String movieID;
    private String category;


    public CustomPojo() {
    }

    //getting poster_path value
    public String getPosterPath() {
        return poster_path;
    }

    //setting poster_path value
    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    //getting title value
    public String getTitle() {
        return title;
    }

    //setting title value
    public void setTitle(String title) {
        this.title = title;
    }

    //getting release_date value
    public String getReleaseDate() {
        return release_date;
    }

    //setting release_date value
    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    //getting overview value
    public String getOverview() {
        return overview;
    }

    //setting overview value
    public void setOverview(String overview) {
        this.overview = overview;
    }

    //getting vote_average value
    public String getVoteAverage() {
        return vote_average;
    }

    //setting vote_average value
    public void setVoteAverage(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }
}





