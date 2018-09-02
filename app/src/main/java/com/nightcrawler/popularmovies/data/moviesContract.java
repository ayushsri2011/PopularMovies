package com.nightcrawler.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class moviesContract {

    public static final String AUTHORITY = "com.nightcrawler.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "favMovies";


    public static final class moviesContractEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "favMovies";
        public static final String MOVIE_ID = "movieID";
        public static final String MOVIE_TITLE = "movieTitle";
        public static final String MOVIE_USER_RATING = "userRating";
        public static final String MOVIE_RELEASE_DATE = "releaseDate";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_POSTERPATH = "posterPath";
        public static final String MOVIE_CATEGORY = "category";
        public static final String MOVIE_TIMESTAMP = "timestamp";


    }
}
