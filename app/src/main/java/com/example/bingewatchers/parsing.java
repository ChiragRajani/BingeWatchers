package com.example.bingewatchers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class parsing extends AsyncTask{
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private  ListView list;
    private String query  ;
    ListViewAdapter adapter;
    static JSONObject kl;
    static  ArrayList<Movie> he=new ArrayList<>() ;
    static  ArrayList<Movie> he1=new ArrayList<>() ;
    private int req  ;
    public parsing(Context context, String query, int req, ListView list) {
        this.context = context;
        this.query=query ;
        this.req=req ;
        this.list=list ;
    }
    @Override
    protected ArrayList<Movie> doInBackground(Object[] objects) {

        try {
            ArrayList<Movie> he1 = new ArrayList<>();


            System.out.println("444444444444444444444444444444444444444444444444444 QUERY FOR&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            //System.out.println("Number recieved is     " + MainActivity.number);
            //String url = "http://in.carregistrationapi.com/api/reg.asmx/CheckIndia?RegistrationNumber=" + MainActivity.number + "&username=Aditya123";
            String url2 = " https://api.themoviedb.org/3/search/multi?api_key=1c9e495395d2ed861f2ace128f6af0e2&language=en-US&query="+query+"&page=1&include_adult=false" ;
//            String url2 = " https://shrouded-falls-48764.herokuapp.com/vehicle-info/MP04SV8479";
            //String url = "https://www.w3schools.com/xml/plant_catalog.xml";

            URL url = new URL(url2);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            int statusCode = urlConnection.getResponseCode();
            kl = new JSONObject();
            System.out.println("Status code is   " + statusCode);
            if (statusCode == 200) {
                InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader read = new InputStreamReader(it);
                BufferedReader buff = new BufferedReader(read);
                StringBuilder dta = new StringBuilder();
                String chunks;
                while ((chunks = buff.readLine()) != null) {
                    dta.append(chunks);
                }
              //  System.out.println("Sting Builder is    " + dta);
                kl = new JSONObject(dta.toString());
            } else {
                kl = null;
            }
     //  System.out.println(kl.toString().replace("\\/","")) ;
            System.out.println("================ doInBackground COMPLETED");
//        if(req==0)
//            sendFullDetails(kl);
//        else
//            sendNameDetails(kl);
           JSONArray parent =kl.getJSONArray("results") ;
           try {
                    for(int i=0;i<parent.length();i++)
                    {
                        JSONObject subparent = (JSONObject) parent.get(i);
                        try {
                                if(subparent.getString("media_type").equals("movie")) {
                             //   System.out.println("------------ ITS A MOVIE------------------------------");
                                he.add(new Movie(subparent.getString("media_type"), subparent.getString("original_language"), subparent.getString("title"),
                                        subparent.getString("vote_average"), subparent.getString("overview"), subparent.getString("release_date")));
//
//                                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//                                System.out.println("this:          "+subparent.getString("media_type") + "\nthis:          " + subparent.getString("original_language") + "\nthis:          " +
//                                        subparent.getString("title") + "\nthis:          " + subparent.getString("vote_average") + "\nthis:          " +
//                                        subparent.getString("overview") + "\nthis:          " + subparent.getString("release_date"));
                            }
                            if(subparent.getString("media_type").equals("tv")){
                                //System.out.println("------------ ITS A Tv series------------------------------");
                                he.add(new Movie(subparent.getString("media_type"), subparent.getString("original_language"), subparent.getString("name"),
                                        subparent.getString("vote_average"), subparent.getString("overview"), subparent.getString("first_air_date")));

//                                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//                                System.out.println("this:          "+subparent.getString("media_type") + "\nthis:          " + subparent.getString("original_language") +
//                                        "\nthis:          " + subparent.getString("name") + "\nthis:          " +
//                                        subparent.getString("vote_average") + "\nthis:          " + subparent.getString("overview") + "\nthis:          "
//                                        + subparent.getString("first_air_date"));

                            }
//                            he1.add(he.get(0));
//                            he1.add(he.get(1));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(subparent);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        } catch (IOException | JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        //System.out.println("4444444444444444444444444444444444444444444444iit"+kl);

     //   return he;

        return he;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        adapter = new ListViewAdapter(context, he);

        list.setAdapter(adapter);
        he=new ArrayList<>() ;
        adapter=null ;

    }



}

class Movie{
    String type ;
    String language ;
    String title;  ;
    String rating   ;
    String description   ;
    String date ;

    public Movie(String type,String language,String title,String rating,String description,String date){
        this.date=date;
        this.description=description ;
        this.language=language ;
        this.rating=rating ;
        this.title=title ;
        this.language=language ;
    }
    public String getMovieName(){
        return title ;
    }
    public String getMovieDate(){
        return date ;
    }

}
