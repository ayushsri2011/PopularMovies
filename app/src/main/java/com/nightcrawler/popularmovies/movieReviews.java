package com.nightcrawler.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class movieReviews extends AppCompatActivity {

    ListView list;List<String> li;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        String mID=intent.getStringExtra("movieID");
        String category=intent.getStringExtra("category");
//        category="movie";
        int movieID=Integer.parseInt(mID);
        ArrayList<String> reviewList=new ArrayList<>();
        try {

            reviewList=CommonUtils.getReviews(movieReviews.this,movieID,category);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        final ListView list= findViewById(R.id.listView1);

        li= new ArrayList<>();
        li=reviewList;

        if(li.isEmpty())
        li.add("No reviews yet.");

        ArrayAdapter<String> adp=new ArrayAdapter<>(getBaseContext(),R.layout.list,li);
        list.setAdapter(adp);


    }


    @Override
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
