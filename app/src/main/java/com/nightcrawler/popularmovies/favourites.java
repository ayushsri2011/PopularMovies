package com.nightcrawler.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;import android.database.Cursor;
import android.net.Uri;import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nightcrawler.popularmovies.data.moviesContract;
import java.util.ArrayList;import java.util.List;
import java.util.Objects;


public class favourites extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ArrayList<CustomPojo> temp;
    Uri uri = moviesContract.moviesContractEntry.CONTENT_URI;
    Cursor cursor;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private Button button_next;    private Button button_prev;
    String category;
    private static final int TASK_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        category=intent.getStringExtra("category");

//        button_next= findViewById(R.id.next_button);button_next.setVisibility(View.INVISIBLE);
//        button_prev= findViewById(R.id.prev_button);button_prev.setVisibility(View.INVISIBLE);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(TASK_LOADER_ID);
        if(loader==null){
            loaderManager.initLoader(TASK_LOADER_ID, null, this);
        }else{
            loaderManager.restartLoader(TASK_LOADER_ID, null, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, favourites.this);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                    forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    String[] args={category};
                    return getContentResolver().query(uri,null,"category=?",args,"timestamp desc");
                } catch (Exception e) {
                    Log.e("loadInBackground()ERROR", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor=data;
        cursor.moveToFirst();

        temp=new ArrayList<CustomPojo>();

        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "No favourites set yet", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                CustomPojo t=new CustomPojo();
                t.setMovieID(cursor.getString(0));
                t.setTitle(cursor.getString(4));
                t.setPosterPath(cursor.getString(5));
                t.setReleaseDate(cursor.getString(2));
                t.setVoteAverage(cursor.getString(3));
                t.setOverview(cursor.getString(1));
                t.setCategory(cursor.getString(6));
                temp.add(t);
                cursor.moveToNext();
            }
        }


        recyclerView= findViewById(R.id.bohe);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter=new CustomAdapter(this);
        populateRecyclerViewValues(temp);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
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
