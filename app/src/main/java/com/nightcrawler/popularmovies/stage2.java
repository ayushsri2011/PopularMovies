package com.nightcrawler.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.nightcrawler.popularmovies.CommonUtils.generateAddress;

public class stage2 extends AppCompatActivity {
//    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private Button button_next;    private Button button_prev;
    private int search_type,page,total_pages=0;
    private ArrayList<CustomPojo> movieList;
    private ProgressBar pb2;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb2= findViewById(R.id.refreshdata_progress);
        pb2.setVisibility(View.INVISIBLE);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        Bundle args = intent.getBundleExtra("BUNDLE");
        movieList = (ArrayList<CustomPojo>)args.getSerializable("ARRAYLIST");
        recyclerView= findViewById(R.id.bohe);
//        picture = findViewById(R.id.picture);

        recyclerView.setLayoutManager(new GridLayoutManager(stage2.this, 2));
        adapter=new CustomAdapter(this);
        populateRecyclerViewValues(movieList);

        total_pages=args.getInt("total_pages");
        search_type=args.getInt("search_type");
        category=args.getString("category","movie");

        page=args.getInt("page");
        button_next= findViewById(R.id.next_button);
        button_prev= findViewById(R.id.prev_button);


        button_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb2.setVisibility(View.VISIBLE);
                if(!CommonUtils.checkConnectivity(stage2.this)) {
                    CommonUtils.alert(stage2.this);
                }
                else {
                    if (page == 1) {
                        CommonUtils.alert2(stage2.this);
                    } else {
                        page--;
                        String address_temp;
                        if (search_type == 0)
                            address_temp = CommonUtils.generateAddress(stage2.this,getString(R.string.REQUEST_TOPRATED), page,category);
                        else
                            address_temp = CommonUtils.generateAddress(stage2.this,getString(R.string.REQUEST_POPULAR), page,category);

                        Query q = new Query();

                        try {
                            q.execute(address_temp).get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }

                        jsonProcessing c = new jsonProcessing(q.local_response,category);
                        movieList = c.process();
                        populateRecyclerViewValues(movieList);
                    }

                }
                pb2.setVisibility(View.INVISIBLE);
            }
        });




        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb2.setVisibility(View.VISIBLE);
                if(!CommonUtils.checkConnectivity(stage2.this)) {

                    CommonUtils.alert(stage2.this);
                }
                else
                {
                    if(page==total_pages)
                    {
                        CommonUtils.alert3(stage2.this);
                    }
                    else
                    {
                        page++;
                        String ty;
                        if(search_type==0)
                            ty="top_rated";
                        else
                            ty="popular";

                        String address_temp = generateAddress(stage2.this,ty,page,category);
                        Query q=new Query();

                        try {
                            q.execute(address_temp).get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        jsonProcessing c=new jsonProcessing(q.local_response,category);
                        movieList=c.process();
                        populateRecyclerViewValues(movieList);
                    }}
                pb2.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void populateRecyclerViewValues(ArrayList<CustomPojo> movieList) {

        adapter.setListContent(movieList);
        recyclerView.setAdapter(adapter);
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

