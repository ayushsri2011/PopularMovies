package com.nightcrawler.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private int page = 1;


    @BindView(R.id.button_topRated) Button button_topRated;
    @BindView(R.id.button_fav) Button button_favourites;
    @BindView(R.id.button_popular) Button button_popular;
    @BindView(R.id.button_upcoming) Button button_upcoming;
    @BindView(R.id.button_nowplaying) Button button_nowplaying;
    @BindView(R.id.button_discover) Button button_discover;
    @BindView(R.id.button_search) Button button_search;
    @BindView(R.id.masthead) TextView textView;

    private int search_type = -1;// 0 Top rated. 1 Popular. 2 Upcoming. 3 Now_Playing. 4.Similar 5.Discover
    private int total_pages = 0;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_page);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        category = intent.getStringExtra("category");

        if (category.equals("tv")) {
            button_nowplaying.setText(R.string.onTheAir);
            button_upcoming.setText(R.string.AiringToday);
            textView.setText(R.string.serialsStash);
        }

        if (category.equals("person")) {
            button_popular.setText(R.string.labelPopularity);
            button_topRated.setText(R.string.latest);
            button_search.setVisibility(View.INVISIBLE);
            button_nowplaying.setVisibility(View.INVISIBLE);
            button_discover.setVisibility(View.INVISIBLE);
            button_upcoming.setVisibility(View.INVISIBLE);
            textView.setText(R.string.people);
        }

        button_topRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blockButtons();

                if (!CommonUtils.checkConnectivity(MainActivity.this)) {
                    CommonUtils.alert(MainActivity.this);
                    unblockButtons();
                } else {

                    search_type = 0;
                    if(category.equals("person"))
                        searchResult("latest", page);
                    else
                        searchResult("top_rated", page);
                    Log.d(TAG, "Search Type is- topRated");

                }
            }
        });

        button_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blockButtons();

                if (!CommonUtils.checkConnectivity(MainActivity.this)) {
                    CommonUtils.alert(MainActivity.this);
                    unblockButtons();
                } else {

                    search_type = 1;
                    searchResult("popular", page);
                    Log.d(TAG, "Search Type is- popularity");

                }
            }
        });


        button_upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockButtons();
                if (!CommonUtils.checkConnectivity(MainActivity.this)) {
                    CommonUtils.alert(MainActivity.this);
                    unblockButtons();
                } else {

                    search_type = 2;
                    if (category.equals("tv"))
                        searchResult("airing_today", page);
                    else
                        searchResult("upcoming", page);
                    Log.d(TAG, "Search Type is- upcoming/airing_today");

                }
            }
        });


        button_nowplaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockButtons();
                if (!CommonUtils.checkConnectivity(MainActivity.this)) {
                    CommonUtils.alert(MainActivity.this);
                    unblockButtons();
                } else {

                    search_type = 3;
                    if (category.equals("tv"))
                        searchResult("on_the_air", page);
                    else
                        searchResult("now_playing", page);

                    Log.d(TAG, "Search Type is- now_playing/on_the_air");
                }
            }
        });

        button_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockButtons();
                Intent intent = new Intent(MainActivity.this, favourites.class);
                intent.putExtra("category", category);
                unblockButtons();
                startActivity(intent);
            }
        });

        button_discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockButtons();
                Intent intent = new Intent(MainActivity.this, Discover.class);
                intent.putExtra("category", category);
                unblockButtons();
                startActivity(intent);
            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockButtons();
                Intent intent = new Intent(MainActivity.this, searchContent.class);
                intent.putExtra("category", category);
                unblockButtons();
                startActivity(intent);
            }
        });
    }


    private void blockButtons() {
        button_topRated.setEnabled(false);
        button_favourites.setEnabled(false);
        button_popular.setEnabled(false);
        button_upcoming.setEnabled(false);
        button_nowplaying.setEnabled(false);
    }

    private void unblockButtons() {
        button_topRated.setEnabled(true);
        button_favourites.setEnabled(true);
        button_popular.setEnabled(true);
        button_upcoming.setEnabled(true);
        button_nowplaying.setEnabled(true);
    }


    private void searchResult(String type, int page) {

        try {
            Log.v(TAG, "searchResult: Type is--" + type);
            String address = CommonUtils.generateAddress(MainActivity.this, type, page, category);
            Log.v("Address---", address);

            Query q = new Query();
            q.execute(address).get();
            String responseJSON = q.local_response;

            ArrayList<CustomPojo> movieList;
            if ("".equals(responseJSON)) {
                if (!CommonUtils.checkConnectivity(MainActivity.this))
                    CommonUtils.alert(MainActivity.this);
                else
                    Toast.makeText(this, "Error in fetching data from Network", Toast.LENGTH_SHORT).show();
            } else {
                Log.v("searchResult", "responseJSON is not null");
                try {
                    jsonProcessing c = new jsonProcessing(responseJSON, category);
                    movieList = c.process();
                    total_pages = c.getTotalPages();
                    Intent intent = new Intent(this, stage2.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST", movieList);
                    args.putInt("search_type", search_type);
                    args.putInt("page", page);
                    args.putInt("total_pages", total_pages);
                    args.putString("category", category);
                    intent.putExtra("BUNDLE", args);

                    unblockButtons();

                    startActivity(intent);
                } catch (JSONException e) {
                    Log.v("searchResult", "Error in fetching details from JSON response received");
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            Log.v("Error in searchResult", "");
            e.printStackTrace();
        }
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
