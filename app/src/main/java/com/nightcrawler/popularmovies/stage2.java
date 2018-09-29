package com.nightcrawler.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.nightcrawler.popularmovies.CommonUtils.generateAddress;

public class stage2 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private int search_type, page, total_pages = 0;
    private ArrayList<CustomPojo> movieList;
    private ArrayList<CustomPojo> nmovieList;
    private ProgressBar pb2;
    private String category;
    Boolean isScrolling = false;

    int currentItems, totalItems, scrollOutItems,test=0,x=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb2 = findViewById(R.id.refreshdata_progress);
        pb2.setVisibility(View.INVISIBLE);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        Bundle args = intent.getBundleExtra("BUNDLE");
        movieList = (ArrayList<CustomPojo>) args.getSerializable("ARRAYLIST");

        recyclerView = findViewById(R.id.bohe);
        final GridLayoutManager lm = new GridLayoutManager(stage2.this, 2);
        recyclerView.setLayoutManager(lm);
        adapter = new CustomAdapter(this);
        adapter.setListContent(movieList);
        recyclerView.setAdapter(adapter);

        total_pages = args.getInt("total_pages");
        search_type = args.getInt("search_type");
        category = args.getString("category", "movie");

        page = args.getInt("page");

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = lm.getChildCount();
                totalItems = lm.getItemCount();
                scrollOutItems = lm.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrollOutItems +2== totalItems)) {
                    if(x==0)
                    {
                        if(category!="search")
                    fetchData();
                    pb2.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    public void fetchData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page == total_pages) {
                    CommonUtils.alert3(stage2.this);
                    x=1;
                } else {
                    page++;
                    String ty;
                    if (search_type == 0)
                        ty = "top_rated";
                    else
                        ty = "popular";

                    String address_temp = generateAddress(stage2.this, ty, page, category);
                    Query q = new Query();

                    try {
                        q.execute(address_temp).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    jsonProcessing c = new jsonProcessing(q.local_response, category);

                    if (test!=0)
                        nmovieList.clear();
                    nmovieList = c.process();
                    test=1;
                    populateRecyclerViewValues(nmovieList);
                    pb2.setVisibility(View.INVISIBLE);
                }
            }
        }, 3000);


    }

    private void populateRecyclerViewValues(ArrayList<CustomPojo> nmovieList) {
        movieList.addAll(nmovieList);
//        adapter.setListContent(movieList);
        //        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


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

