package com.example.bingewatchers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieInfo extends AppCompatActivity {
  static TextView id,type,director,writer,actors,genre,title,releasedDate,
            runtime,imdb_rating,language,boxOffice,plot,productions ;
  static  ImageView poster ;
  static Context mContext ;

    static  ViewGroup progressView;
    static  protected boolean isProgressShowing = false;
    static MovieInfo x ;

    static JSONObject movieInfo ;
    String s ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        showProgressingView() ;
          initializeFields() ;
          mContext=getApplicationContext() ;
        x = this;
       s= getIntent().getSerializableExtra("MovieID").toString() ;
//        id.setText("ID IS \n"+s);
        new parsing(getApplicationContext(),s,1).execute() ;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressingView();
                setFields(movieInfo);
            }
        }, 1000);

    }
     public static void setJSONOBJECT(JSONObject kl){
        movieInfo=kl;

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
        poster=findViewById(R.id.poster) ;
    }
    public static void setFields(JSONObject kl){
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
      }catch (JSONException|NullPointerException e){
          e.printStackTrace();
          System.out.println("44444444444 Error in setting field is "+e.getMessage());
      }
    }
    public void showProgressingView() {
        if (!isProgressShowing) {
            isProgressShowing = true;
            progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);
            View v = this.findViewById(android.R.id.content).getRootView();
            ViewGroup viewGroup = (ViewGroup) v;
            viewGroup.addView(progressView);
        }
    }
    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }
}
