package com.nightcrawler.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class movieTrailers extends AppCompatActivity {

    private ListView list;
    private List<String> li;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailers);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent in =getIntent();
        String movieID = in.getStringExtra("movieID");
        String category = in.getStringExtra("category");
         ArrayList<CustomPojo3> mTrailer = new ArrayList<>();
        try {
            mTrailer = CommonUtils.getTrailerList(movieTrailers.this, Integer.parseInt(movieID),category);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        list = findViewById(R.id.listView2);
        li = new ArrayList<>();

        for (int i = 0; i < mTrailer.size(); i++)
            li.add(mTrailer.get(i).getTname());

        if(li.isEmpty()) {
            li.add("No videos available");
            Toast.makeText(movieTrailers.this, "Loading", Toast.LENGTH_SHORT).show();
        }
        final ArrayAdapter<String> adp = new ArrayAdapter<>(getBaseContext(), R.layout.list, li);
        list.setAdapter(adp);
        list.setClickable(true);

        final ArrayList<CustomPojo3> temp = mTrailer;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                watchYoutubeVideo(temp.get(position).getTkey());

            }

        });

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

    private void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

}