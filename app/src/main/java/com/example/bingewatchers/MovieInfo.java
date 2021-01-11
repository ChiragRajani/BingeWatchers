package com.example.bingewatchers;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieInfo extends AppCompatActivity {
    static protected boolean isProgressShowing = false;
    static TextView id, type, director, writer, actors, genre, title, releasedDate,
            runtime, imdb_rating, language, boxOffice, plot, productions;
    static ImageView poster;
    static Context mContext;
    static ViewGroup progressView;
    static MovieInfo x;
    static JSONObject movieInfo;
    View v ;
    ViewGroup viewGroup ;
    ImageView mProgressBar;
    AnimationDrawable animationDrawable;
    String s;

//    public static void setJSONOBJECT(JSONObject kl) {
//        movieInfo=null ;
//        movieInfo = kl;
//
//    }

    public static void setFields(JSONObject kl) {
        try {
            id.setText(kl.getString("Title"));
            type.setText(kl.getString("Type"));
            releasedDate.setText(kl.getString("Released"));
            director.setText(kl.getString("Director"));
            writer.setText(kl.getString("Writer"));
            runtime.setText(kl.getString("Runtime"));
            genre.setText(kl.getString("Genre"));
            language.setText(kl.getString("Language"));
            boxOffice.setText(kl.getString("BoxOffice"));
            actors.setText(kl.getString("Actors"));
            plot.setText(kl.getString("Plot"));
            productions.setText(kl.getString("Production"));
            Glide.with(mContext).asDrawable()
                    .load(kl.getString("Poster"))
                    .into(poster);
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("44444444444 Error in setting field is " + e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        v = this.findViewById(android.R.id.content).getRootView();
        viewGroup=(ViewGroup) v ;
        showProgressingView();
        x = this;

        initializeFields();
        mContext = getApplicationContext();

        s = getIntent().getSerializableExtra("MovieID").toString();
//        id.setText("ID IS \n"+s);
        new parsing(getApplicationContext(), s, 1).execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressingView();
                setFields(movieInfo);
            }
        }, 1500);

    }

    public void initializeFields() {

        id = findViewById(R.id.movieTitle);
        type = findViewById(R.id.type);
        releasedDate = findViewById(R.id.releasedDate);
        director = findViewById(R.id.directors);
        writer = findViewById(R.id.textView8);
        runtime = findViewById(R.id.runtime);
        genre = findViewById(R.id.genre);
        language = findViewById(R.id.languages);
        boxOffice = findViewById(R.id.boxOffice);
        actors = findViewById(R.id.actors);
        plot = findViewById(R.id.plot);
        productions = findViewById(R.id.productions);
        poster = findViewById(R.id.poster);
    }

    public void showProgressingView() {
        if (!isProgressShowing) {
            isProgressShowing = true;
            progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);

            viewGroup.addView(progressView);
            mProgressBar = progressView.findViewById(R.id.loadingif);
            mProgressBar.setBackgroundResource(R.drawable.popcorrn_loading);
            animationDrawable = (AnimationDrawable) mProgressBar.getBackground();
            mProgressBar.setVisibility(View.VISIBLE);
            animationDrawable.start();
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
        mProgressBar.setVisibility(View.GONE);
        animationDrawable.stop();
    }
}
