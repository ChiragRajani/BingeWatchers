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

import com.bumptech.glide.Glide;

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
    ImageView img ;
    @SuppressLint("StaticFieldLeak")
    TextView name;
    private int req = 1;
    LayoutInflater inflater ;



    public parsing(Context context, String query, int req, ListView list) {
        this.context = context;
        this.query = query;
        this.req = req;
        this.list = list;
    }

    public parsing() {

    }

    public parsing(Context context, String query, int i) {
        this.context = context;
        this.query = query;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.navigation_drawer, null);
        name = view.findViewById(R.id.name12);
        img = view.findViewById(R.id.image_view12);
    }

    @Override
    protected ArrayList<Movie> doInBackground(Object[] objects) {

        try {

            System.out.println("444444444444444444444444444444444444444444444444444 QUERY FOR&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            String url2 = " https://api.themoviedb.org/3/search/multi?api_key=1c9e495395d2ed861f2ace128f6af0e2&language=en-US&query=" + query + "&page=1&include_adult=false";

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
            System.out.println("================ doInBackground COMPLETED");
            JSONArray parent = kl.getJSONArray("results");
            try {
                for (int i = 0; i < parent.length(); i++) {
                    JSONObject subparent = (JSONObject) parent.get(i);
                    try {
                        if (subparent.getString("media_type").equals("movie")) {
                            //   System.out.println("------------ ITS A MOVIE------------------------------");
                            he.add(new Movie(subparent.getString("media_type"), subparent.getString("original_language"), subparent.getString("title"),
                                    subparent.getString("vote_average"), subparent.getString("overview"), subparent.getString("release_date"), subparent.getString("poster_path")));

                        }
                        if (subparent.getString("media_type").equals("tv")) {
                            //System.out.println("------------ ITS A Tv series------------------------------");
                            he.add(new Movie(subparent.getString("media_type"), subparent.getString("original_language"), subparent.getString("name"),
                                    subparent.getString("vote_average"), subparent.getString("overview"), subparent.getString("first_air_date"), subparent.getString("poster_path")));

                        }

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



        return he;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        he1 = he;
        initRecyclerView1(he);

        if (req == 0) {
            adapter = new ListViewAdapter(context, he);
            he1 = he;
            list.setAdapter(adapter);
            he = new ArrayList<>();
            adapter = null;
        } else {

        }

    }

    private void initRecyclerView1(ArrayList<Movie> he) {
        try {
              Log.d(TAG, "gggggggggggggggggggggggg   xyz        " + he.size());
           Log.d(TAG,he.get(0).getMovieDate()) ;
            name.setText("yaha movie name");
//            name.setText(he.get(0).getMovieName());
            Glide.with(context).asDrawable()
                    .load(he.get(0).getPoster())
                    .into(img);


//            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//            RecyclerView recyclerView = view.findViewById(R.id.recc_recycler);
//            recyclerView.setLayoutManager(layoutManager);
//            Recommendation_Adapter adapter = new Recommendation_Adapter(context, he);
//            recyclerView.setAdapter(adapter);


        } catch (Exception ne) {
            System.out.println("222222222222222222222222222" + ne.getMessage());
        }
    }
}

class Movie {
    String type;
    String language;
    String title;
    String rating;
    String description;
    String date;
    String poster;

    public Movie(String type, String language, String title, String rating, String description, String date, String poster) {
        this.date = date;
        this.description = description;
        this.language = language;
        this.rating = rating;
        this.title = title;
        this.language = language;
        this.poster = poster;

    }

    public Movie() {

    }

    public String getMovieName() {
        return title;
    }

    public String getMovieDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getLanguage() {
        return language;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getPoster() {
        return "https://image.tmdb.org/t/p/w500" + poster;
    }

}
