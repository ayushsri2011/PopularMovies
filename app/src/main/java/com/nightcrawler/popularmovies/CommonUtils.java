package com.nightcrawler.popularmovies;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CommonUtils {

    public static void alert(Context context){
        Resources res = context.getResources();
        new AlertDialog.Builder(context)
                .setTitle(res.getString(R.string.app_name))
                .setMessage(res.getString(R.string.internet_error))
                .setPositiveButton("OK", null).show();
    }

    public static void alert2(Context context)    {
        Resources res = context.getResources();
        new AlertDialog.Builder(context)
                .setTitle(res.getString(R.string.app_name))
                .setMessage(res.getString(R.string.error_first_page))
                .setPositiveButton("OK", null).show();
    }

    public static void alert3(Context context)    {
        Resources res = context.getResources();
        new AlertDialog.Builder(context)
                .setTitle(res.getString(R.string.app_name))
                .setMessage(res.getString(R.string.error_last_page))
                .setPositiveButton("OK", null).show();
    }

    public static String generateAddress(Context context,String search_type, int page,String category) {
        Resources res = context.getResources();
        String key= res.getString(R.string.API_KEY);
        String baseURL = res.getString(R.string.BASE_URL);

        return baseURL + category + "/" +search_type + "?api_key="+ key + "&page=" + page;
    }

    public static boolean checkConnectivity(Context context){
        try{
            ConnectivityManager conMgr =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            return netInfo != null;
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<String> getReviews(Context context,int movieID,String category) throws ExecutionException, InterruptedException {
        Resources res = context.getResources();
        String key= res.getString(R.string.API_KEY);
        String baseURL =res.getString(R.string.BASE_URL);

        String address =baseURL+category+"/"+movieID+"/reviews?api_key="+key+"&language=en-US&page=1";

        Query q=new Query();
        q.execute(address).get();
        Log.v("SHIT",q.local_response);
        String responseJSON = q.local_response;

        ArrayList<CustomPojo2> rList=new ArrayList<>();
        if("".equals(responseJSON))
        {
            if(!CommonUtils.checkConnectivity(context))
                CommonUtils.alert(context);
            else
                Toast.makeText(context,"Error in fetching data from Network", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.v("searchResult","responseJSON is not null");
            jsonProcessing c=new jsonProcessing(responseJSON,"category");
            rList=c.process2(context);

        }

        ArrayList li=new ArrayList<String>();


        for(int i=0;i<rList.size();i++) {
            li.add(rList.get(i).getContent());
            Log.d("movieReview", rList.get(i).getContent());
        }

        return li;
    }

    public static ArrayList<CustomPojo3> getTrailerList(Context context,int movieID,String category) throws ExecutionException, InterruptedException {
        Resources res = context.getResources();
        String key= res.getString(R.string.API_KEY);

        String baseURL =res.getString(R.string.BASE_URL);

        String address =baseURL+category+"/"+movieID+"/videos?api_key="+key+"&language=en-US";

        Query q=new Query();
        q.execute(address).get();
        Log.v("SHIT",q.local_response);
        String responseJSON = q.local_response;

        ArrayList<CustomPojo3> tList=new ArrayList<>();
        if("".equals(responseJSON))
        {
            if(!CommonUtils.checkConnectivity(context))
                CommonUtils.alert(context);
            else
                Toast.makeText(context,"Error in fetching data from Network", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.v("searchResult","responseJSON is not null");
            jsonProcessing c=new jsonProcessing(responseJSON,"category");
            tList=c.process3(context);
        }

        ArrayList li=new ArrayList<String>();
        System.out.println("ListSize "+tList.size());

        for(int i=0;i<tList.size();i++) {
            CustomPojo3 p=new CustomPojo3();
            p.setTkey(tList.get(i).getTkey());
            p.setTname(tList.get(i).getTname());
            li.add(p);

        }

        return li;

    }

    public static String generateDiscoverAddress(Context context,String genres, int page,String search_type,String sortBy,String year) {
        Resources res = context.getResources();
        String key= res.getString(R.string.API_KEY);
        String baseURL = res.getString(R.string.BASE_URL);

//        genres=genres.replace(",","%2C");
        String finalURL;

        finalURL=baseURL + "discover/" +search_type +
                "?api_key="+ key +"&sort_by="+sortBy+
                "&page=" + page;

        if(genres!=null || genres=="" || genres==" ")
            finalURL+="&with_genres="+genres;

        if(year!=null)
            finalURL+="&year="+year;

        return finalURL;

//        +"&include_adult="+adult   //save preferences of adult lock


    }

}

