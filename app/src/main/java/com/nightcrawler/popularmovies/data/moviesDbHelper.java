package com.nightcrawler.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class moviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favMovies.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public moviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_MOVIELIST_TABLE = "CREATE TABLE "
              + moviesContract.moviesContractEntry.TABLE_NAME + " (" +
                moviesContract.moviesContractEntry.MOVIE_ID + " TEXT , " +
                moviesContract.moviesContractEntry.MOVIE_OVERVIEW + " TEXT  , " +
                moviesContract.moviesContractEntry.MOVIE_RELEASE_DATE + " TEXT , " +
                moviesContract.moviesContractEntry.MOVIE_USER_RATING + " TEXT , " +
                moviesContract.moviesContractEntry.MOVIE_TITLE  + " TEXT , "+
                moviesContract.moviesContractEntry.MOVIE_POSTERPATH  + " TEXT , "+
                moviesContract.moviesContractEntry.MOVIE_CATEGORY  + " TEXT , "+
                moviesContract.moviesContractEntry.MOVIE_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                +"); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + moviesContract.moviesContractEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}