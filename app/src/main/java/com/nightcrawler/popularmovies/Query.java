package com.nightcrawler.popularmovies;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Query extends AsyncTask<String, String,String> {

    public String local_response;

    @Override
    protected String doInBackground(String... params) {
        String line="";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String local_address=params[0];
        local_response="";
        Log.v("Address is---",local_address);
        try {
            URL url= new URL(local_address);
            Log.v("url",url.toString());

            connection = (HttpURLConnection) url.openConnection();
            Log.v("Connection",connection.toString());
            connection.connect();

            InputStream stream = connection.getInputStream();
            Log.v("stream",stream.toString());

            reader = new BufferedReader(new InputStreamReader(stream));
            Log.v("reader",reader.toString());

            StringBuilder builder = new StringBuilder();
            Log.v("buffer",builder.toString());

            line = reader.readLine();
            builder.append(line).append("\n");


            local_response=line;

        } catch (IOException e) {           e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.v("close",Integer.toString(R.string.error_close_reader));
                e.printStackTrace();
            }
        }
        return line;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.v("onPostExecute:::",result);
    }
}



