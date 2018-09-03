package com.nightcrawler.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class searchContent extends AppCompatActivity {
    String category;

    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.searchTextView) EditText searchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_content);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();

        category=intent.getStringExtra("category");
//        btnSearch=(Button)findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp= searchTextView.getText().toString().trim();


                if (temp.equals("")) {
                    Toast.makeText(searchContent.this,"Enter search text and try again",Toast.LENGTH_SHORT).show();
                    return;
                }

            String address=getString(R.string.BASE_URL)+"search/"+category+"?api_key=9498a03d3c2c3de6b0eb1504be02bc9e&sort_by=vote_average.desc&query="+temp;

                Query q = new Query();
                try {
                    q.execute(address).get();
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
                String responseJSON = q.local_response;

                ArrayList<CustomPojo> movieList;
                if ("".equals(responseJSON)) {
                    if (!CommonUtils.checkConnectivity(searchContent.this))
                        CommonUtils.alert(searchContent.this);
                    else
                        Toast.makeText(searchContent.this, "Error in fetching data from Network", Toast.LENGTH_SHORT).show();
                } else {
                    Log.v("searchResult", "responseJSON is not null");

                    jsonProcessing c = new jsonProcessing(responseJSON, category);
                    movieList = c.process();
                    int total_pages = 0;
                    try {
                        total_pages = c.getTotalPages();
                        if(c.getTotalResults()==0)
                        {
                            Toast.makeText(searchContent.this, "No results found . Try again.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    Intent intent2 = new Intent(searchContent.this, stage2.class);
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
