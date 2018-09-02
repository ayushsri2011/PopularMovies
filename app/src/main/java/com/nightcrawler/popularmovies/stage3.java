package com.nightcrawler.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nightcrawler.popularmovies.data.moviesContract;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class stage3 extends AppCompatActivity {
    private ArrayList<CustomPojo> data;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button favButton,similarButton,mReviews,mVideos;
    private String movieID,category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mReviews = findViewById(R.id.mReview);
        mVideos = findViewById(R.id.mVideos);
        favButton = findViewById(R.id.favButton);
        similarButton = findViewById(R.id.similarButton);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        data = (ArrayList<CustomPojo>)args.getSerializable("ARRAYLIST");

        ImageView poster = findViewById(R.id.poster);
        TextView title = findViewById(R.id.title);
        TextView vote_average = findViewById(R.id.vote_average);
        TextView releaseDate = findViewById(R.id.releaseDate);
        TextView synopsis = findViewById(R.id.synopsis);

        String base_image_url=getString(R.string.baseImageURL);
        String poster_path=data.get(0).getPosterPath();
        String url  =   base_image_url  +   poster_path;
        if(CommonUtils.checkConnectivity(stage3.this))
        Picasso.get().load(url).placeholder(R.drawable.ph).into(poster);


        title.setText(data.get(0).getTitle());
        vote_average.setText(new StringBuilder().append(getString(R.string.UserRating)).append(data.get(0).getVoteAverage()).toString());
        releaseDate.setText(new StringBuilder().append(getString(R.string.ReleaseDate)).append(data.get(0).getReleaseDate()).toString());
        synopsis.setText(data.get(0).getOverview().trim().equals("") ? "Synopsis unavailable." : data.get(0).getOverview());
        System.out.println("TESTING"+getString(R.string.REQUEST_POPULAR));

        movieID=data.get(0).getMovieID();
        category=data.get(0).getCategory();

        mReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(stage3.this, "Loading", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(stage3.this, movieReviews.class);
                intent.putExtra("movieID",movieID);
                intent.putExtra("category",category);
                startActivity(intent);
            }
        });

        mVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(stage3.this,movieTrailers.class);
                intent.putExtra("movieID",movieID);
                intent.putExtra("category",category);
                startActivity(intent);
            }
        });



        similarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtils.checkConnectivity(stage3.this)) {
                    try {
                        String search_type = movieID + "/similar";
                        Log.v(TAG, "searchResult: Type is--" + search_type);
                        String address = CommonUtils.generateAddress(stage3.this, search_type, 1, category);
                        Log.v("Address---", address);

                        Query q = new Query();
                        q.execute(address).get();
                        String responseJSON = q.local_response;

                        ArrayList<CustomPojo> movieList;
                        if ("".equals(responseJSON)) {
                            if (!CommonUtils.checkConnectivity(getBaseContext()))
                                CommonUtils.alert(getBaseContext());
                            else
                                Toast.makeText(getBaseContext(), "Error in fetching data from Network", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.v("searchResult", "responseJSON is not null");

                            jsonProcessing c = new jsonProcessing(responseJSON, category);
                            movieList = c.process();
                            int total_pages = c.getTotalPages();
                            Intent intent = new Intent(getBaseContext(), stage2.class);
                            Bundle args = new Bundle();
                            args.putSerializable("ARRAYLIST", movieList);
                            args.putInt("search_type", 4);
                            args.putInt("page", 1);
                            args.putInt("total_pages", total_pages);
                            args.putString("category", category);
                            intent.putExtra("BUNDLE", args);
                            startActivity(intent);

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(stage3.this, "No internet", Toast.LENGTH_SHORT).show();
            }});



        Resources res = getResources();
        final String setFav=res.getString(R.string.SetFavourite);
        final String unsetFav=res.getString(R.string.UnsetFavourite);


        String[] selectionArgs = { movieID };
        Cursor mCount = getContentResolver().query(moviesContract.moviesContractEntry.CONTENT_URI,
                        null,"movieID=?",selectionArgs,null);

        int temp= Objects.requireNonNull(mCount).getCount();
        mCount.close();

        if(temp==0)
        {
            favButton.setText(setFav);
        }
        else {
            favButton.setText(unsetFav);
        }

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(favButton.getText()==setFav)
                {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(moviesContract.moviesContractEntry.MOVIE_POSTERPATH, data.get(0).getPosterPath());
                    contentValues.put(moviesContract.moviesContractEntry.MOVIE_ID, data.get(0).getMovieID());
                    contentValues.put(moviesContract.moviesContractEntry.MOVIE_OVERVIEW, data.get(0).getOverview());
                    contentValues.put(moviesContract.moviesContractEntry.MOVIE_RELEASE_DATE, data.get(0).getReleaseDate());
                    contentValues.put(moviesContract.moviesContractEntry.MOVIE_TITLE, data.get(0).getTitle());
                    contentValues.put(moviesContract.moviesContractEntry.MOVIE_USER_RATING, data.get(0).getVoteAverage());
                    contentValues.put(moviesContract.moviesContractEntry.MOVIE_CATEGORY, data.get(0).getCategory());

                    getContentResolver().insert(moviesContract.moviesContractEntry.CONTENT_URI, contentValues);

                    Toast.makeText(stage3.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                    favButton.setText(unsetFav);
                }
                else
                {
                    Uri uri =moviesContract.moviesContractEntry.CONTENT_URI.buildUpon().appendPath(movieID).build();
                    getContentResolver().delete(uri, null, null);

                    Toast.makeText(stage3.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                    favButton.setText(setFav);
                }
            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





}






