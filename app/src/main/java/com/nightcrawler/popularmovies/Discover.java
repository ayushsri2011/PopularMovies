package com.nightcrawler.popularmovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Discover extends AppCompatActivity {


    Button mOrder;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String genres = "";
    String category = "";
    TextView discover_masthead;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        category = intent.getStringExtra("category");

        Resources res = getResources();
        final String[] genresID = res.getStringArray(R.array.genresID);
        Button b = (Button) findViewById(R.id.btn);
        discover_masthead = (TextView) findViewById(R.id.discover_masthead);

        if (category.equals("tv"))
            discover_masthead.setText(R.string.discoverTV);

        mOrder = (Button) findViewById(R.id.btnOrder);

        listItems = getResources().getStringArray(R.array.genres);
        checkedItems = new boolean[listItems.length];

        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Discover.this);
                mBuilder.setTitle("Select Genres");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            mUserItems.add(position);
                        } else {
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(true);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + genresID[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        genres = item;
                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        final String year = "2018";


        Log.v("Discover.java ", "searchResult: Type is--Discover " + category);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String address = CommonUtils.generateDiscoverAddress(Discover.this, genres, 1, category, "popularity.desc", year);
                Log.v("Address---", address);
                Query q = new Query();
                try {
                    q.execute(address).get();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
                String responseJSON = q.local_response;

                ArrayList<CustomPojo> movieList;
                if ("".equals(responseJSON)) {
                    if (!CommonUtils.checkConnectivity(Discover.this))
                        CommonUtils.alert(Discover.this);
                    else
                        Toast.makeText(Discover.this, "Error in fetching data from Network", Toast.LENGTH_SHORT).show();
                } else {
                    Log.v("searchResult", "responseJSON is not null");

                    jsonProcessing c = new jsonProcessing(responseJSON, category);
                    movieList = c.process();
                    int total_pages = 0;
                    try {
                        total_pages = c.getTotalPages();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    Intent intent2 = new Intent(Discover.this, stage2.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST", movieList);
                    args.putInt("search_type", 5);
                    args.putInt("page", 1);
                    args.putInt("total_pages", total_pages);
                    args.putString("category", category);
                    intent2.putExtra("BUNDLE", args);


                    startActivity(intent2);
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
