package com.example.bingewatchers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;

import androidx.recyclerview.widget.GridLayoutManager;

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
import java.util.Collections;

public class GenreSearch extends AsyncTask {
    private static final String TAG = "GenreSearch";
    static JSONObject kl;
    String url2;
    static ArrayList<Movie> ge = new ArrayList<>();


    private Context mContext;

    GenreSearch(Context mContext, String url2) {
        this.url2 = url2;
        this.mContext = mContext;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
       // Collections.shuffle(ge);
        int orientation = mContext.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            DashBoard.groupRecycler.setLayoutManager(new GridLayoutManager(mContext, 3));
        } else {
            // code for landscape mode
            DashBoard.groupRecycler.setLayoutManager(new GridLayoutManager(mContext, 5));
        }


        DashBoard.groupRecycler.setAdapter(DashBoard.adapter12);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DashBoard.adapter12 = new Recommendation_Adapter(mContext, ge);
    }

    @Override
    protected ArrayList<Movie> doInBackground(Object[] objects) {
        try {

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
                for (int i = 0; i < parent.length()/2; i++) {
                    JSONObject subparent = (JSONObject) parent.get(i);
                    try {

                        ge.add(new Movie(subparent.getString("title"), subparent.getString("poster_path"), subparent.getJSONArray("genre_ids"), subparent.getString("id"),subparent.getString("backdrop_path")));


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (IOException | JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        return ge;
    }
}
