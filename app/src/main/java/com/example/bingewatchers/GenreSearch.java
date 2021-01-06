package com.example.bingewatchers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class GenreSearch extends AsyncTask {
    private static final String TAG = "GenreSearch";
    static JSONObject kl;
    static ArrayList<Movie> ge = new ArrayList<>();
    private String genre;
    private Context mContext;

    GenreSearch(String genre, Context mContext) {
        this.genre = genre;
        this.mContext = mContext;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        DashBoard.adapter12 = new Recommendation_Adapter(mContext, ge);
        DashBoard.groupRecycler.setAdapter(DashBoard.adapter12);

    }

    @Override
    protected ArrayList<Movie> doInBackground(Object[] objects) {
        try {

            String url2 = "https://api.themoviedb.org/3/discover/movie?api_key=1c9e495395d2ed861f2ace128f6af0e2&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=" + genre;
            Log.d(TAG, genre + " " + url2);

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
            Log.d(TAG, "================ doInBackground COMPLETED");
            JSONArray parent = kl.getJSONArray("results");
            try {
                for (int i = 0; i < parent.length(); i++) {
                    JSONObject subparent = (JSONObject) parent.get(i);
                    try {

                        //   System.out.println("------------ ITS A MOVIE------------------------------");

                        ge.add(new Movie(subparent.getString("original_title"), subparent.getString("poster_path")));
//     ge.add(new Movie("MOVIE", subparent.getString("original_language"), subparent.getString("original_title"),
//                                    subparent.getString("vote_average"), subparent.getString("overview"), subparent.getString("release_date"), subparent.getString("poster_path")));


//                        if (subparent.getString("media_type").equals("tv")) {
//                            //System.out.println("------------ ITS A Tv series------------------------------");
//                            he.add(new Movie(subparent.getString("media_type"), subparent.getString("original_language"), subparent.getString("name"),
//                                    subparent.getString("vote_average"), subparent.getString("overview"), subparent.getString("first_air_date"), subparent.getString("poster_path")));
//
//                        }

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
        //   Log.d(TAG, "initRecyclerView: init     " + he.size());

        return ge;
    }
}
