package com.nightcrawler.popularmovies;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class jsonProcessing {

    public String response;
    public String category;

    public jsonProcessing(String r, String category) {
        this.response = r;
        this.category = category;
    }

    public ArrayList<CustomPojo> process() {

        ArrayList movieList = new ArrayList();

        try {
            JSONObject jsonObj = new JSONObject(response);            // Getting JSON Array node
            JSONArray results = jsonObj.getJSONArray("results");
            // looping through All results

            System.out.println("Length of Result::" + results.length());
            for (int i = 0; i < results.length(); i++) {
                JSONObject c = results.getJSONObject(i);

                String poster_path = c.getString("poster_path");
                if(poster_path=="")
                    poster_path= c.getString("profile_path");

                String title, release_date;
                if (category.equals("movie")) {
                    title = c.getString("title");
                    release_date = c.getString("release_date");
                } else {
                    title = c.getString("original_name");
                    release_date = c.getString("first_air_date");
                }


                String overview = c.getString("overview");
                String vote_average = c.getString("vote_average");
                String movieID = c.getString("id");

                CustomPojo movie = new CustomPojo();
                movie.setPosterPath(poster_path);
                movie.setReleaseDate(release_date);
                movie.setTitle(title);
                movie.setVoteAverage(vote_average);
                movie.setOverview(overview);
                movie.setMovieID(movieID);
                movie.setCategory(category);
                movieList.add(movie);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }

    public ArrayList<CustomPojo2> process2(Context context) {
        ArrayList<CustomPojo2> reviewList = new ArrayList();


        try {
            JSONObject jsonObj = new JSONObject(response);            // Getting JSON Array node
            JSONArray results = jsonObj.getJSONArray("results");
            // looping through All results
            System.out.println("Length of Result::" + results.length());
            for (int i = 0; i < results.length(); i++) {

                JSONObject c = results.getJSONObject(i);
                String author = c.getString("author");
                String content = c.getString("content");
                CustomPojo2 review = new CustomPojo2();
                review.setAuthor(author);
                review.setContent(content);
                reviewList.add(review);
                Log.v(review.getAuthor(), review.getContent());

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error in retrieving reviews", Toast.LENGTH_SHORT).show();
        } finally {
            return reviewList;
        }
    }

    public ArrayList<CustomPojo3> process3(Context context) {
        ArrayList<CustomPojo3> trailerList = new ArrayList();

        try {
            JSONObject jsonObj = new JSONObject(response);            // Getting JSON Array node
            JSONArray results = jsonObj.getJSONArray("results");
            // looping through All results
            System.out.println("Length of Result::" + results.length());
            for (int i = 0; i < results.length(); i++) {
                CustomPojo3 trailer = new CustomPojo3();
                JSONObject c = results.getJSONObject(i);
                String tkey = c.getString("key");
                String tname = c.getString("name");

                trailer.setTname(tname);
                trailer.setTkey(tkey);
                trailerList.add(trailer);
                Log.v(trailer.getTkey(), trailer.getTname());
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error in retrieving reviews", Toast.LENGTH_SHORT).show();
        } finally {
            return trailerList;
        }
    }

    public int getTotalPages() throws JSONException {
        JSONObject temp = new JSONObject(response);

        return Integer.parseInt(temp.getString("total_pages"));
    }

    public int getTotalResults() throws JSONException {
        JSONObject temp = new JSONObject(response);

        return Integer.parseInt(temp.getString("total_results"));
    }

}
