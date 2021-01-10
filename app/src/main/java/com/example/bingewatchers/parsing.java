package com.example.bingewatchers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class parsing extends AsyncTask {
    private static final String TAG = "PARSING";
    static JSONObject kl;
    static ArrayList<Movie> he = new ArrayList<>();
    static ArrayList<Movie> he1 = new ArrayList<>();
    View view;
    ListViewAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private ListView list;
    private String query;
    @SuppressLint("StaticFieldLeak")
    ImageView img;
    @SuppressLint("StaticFieldLeak")
    TextView name;
    private int req;
    LayoutInflater inflater;


    public parsing(Context context, String query, int req, ListView list) {
        this.context = context;
        this.query = query;
        this.req = req;
        this.list = list;
    }

    public parsing() {

    }

    public parsing(Context context, String query, int req) {
        this.context = context;
        this.query = query;
        this.req=req ;
        System.out.println("5555555555555555555555555555555555555555555555  req is "+req);
    }

    @Override
    protected ArrayList<Movie> doInBackground(Object[] objects) {
        String url2 ;
        try {

            if(req==0){
                url2 = "https://api.themoviedb.org/3/search/multi?api_key=1c9e495395d2ed861f2ace128f6af0e2&language=en-US&query=" + query + "&page=1&include_adult=false";

            URL url = new URL(url2);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            int statusCode = urlConnection.getResponseCode();
            kl = new JSONObject();
            if (statusCode == 200) {
                InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader read = new InputStreamReader(it);
                BufferedReader buff = new BufferedReader(read);
                StringBuilder dta = new StringBuilder();
                String chunks;
                while ((chunks = buff.readLine()) != null) {
                    dta.append(chunks);
                }
                kl = new JSONObject(dta.toString());
            } else {
                kl = null;
            }
            JSONArray parent = kl.getJSONArray("results");
            try {
                for (int i = 0; i < parent.length(); i++) {
                    JSONObject subparent = (JSONObject) parent.get(i);
                    try {
                        if (subparent.getString("media_type").equals("movie")) {
                            //   "------------ ITS A MOVIE------------------------------"
                            he.add(new Movie(subparent.getString("media_type"), subparent.getString("original_language"), subparent.getString("title"),
                                    subparent.getString("vote_average"), subparent.getString("overview"), subparent.getString("release_date"), subparent.getString("poster_path")));
  }
                        if (subparent.getString("media_type").equals("tv")) {
//                            ------------ ITS A Tv series------------------------------"
                            he.add(new Movie(subparent.getString("media_type"), subparent.getString("original_language"), subparent.getString("name"),
                                    subparent.getString("vote_average"), subparent.getString("overview"), subparent.getString("first_air_date"), subparent.getString("poster_path")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            else if(req==1)
            {
                url2="https://api.themoviedb.org/3/movie/"+query+"+?api_key=1c9e495395d2ed861f2ace128f6af0e2&language=en-US";
                Log.d(TAG," INFO OF MOVIE WITH ID "+query+"\nhttps://api.themoviedb.org/3/movie/"+query+"?api_key=1c9e495395d2ed861f2ace128f6af0e2&language=en-US") ;
                URL url = new URL(url2);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");



                int statusCode = urlConnection.getResponseCode();
                kl = new JSONObject();
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    kl = new JSONObject(dta.toString());
                } else {
                    kl = null;
                }
                String imdb_id = kl.getString("imdb_id");
                Log.d(TAG,"IMDB ID OF "+query+" is "+imdb_id) ;
               String omdbLink= "https://www.omdbapi.com/?apikey=45881d05&i="+imdb_id ;
               url = new URL(omdbLink);
               urlConnection = (HttpURLConnection) url.openConnection();
               urlConnection.setRequestMethod("GET");
               statusCode = urlConnection.getResponseCode();
                kl = new JSONObject();
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    kl = new JSONObject(dta.toString());
                } else {
                    kl = null;
                }
            }
        }catch (IOException | JSONException | NullPointerException e) {
            e.printStackTrace();
        }

        return he;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    if(req==0)
    {
    adapter = new ListViewAdapter(context, he);
    he1 = he;
    list.setAdapter(adapter);
    he = new ArrayList<>();
    adapter = null;
} //KYA KARU?
if (req==1){
   MovieInfo.setFields(kl);
//   MovieInfo.setJSONOBJECT(kl);
}

    }

}

