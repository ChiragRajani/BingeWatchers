package com.example.bingewatchers;

import com.google.common.collect.BiMap;

import org.json.JSONArray;
import org.json.JSONException;

class Movie {
  String type;
  String language;
  String title;
  String rating;
  String description;
  String date;
  String id;
  String poster;
  JSONArray Genres;

  public Movie(String type, String language, String title, String rating, String description, String date, String poster, JSONArray Genres) {
    this.date = date;
    this.description = description;
    this.language = language;
    this.rating = rating;
    this.title = title;
    this.language = language;
    this.poster = poster;
    this.Genres = Genres;

  }

  public Movie(String title, String poster, JSONArray Genres, String id) {
    this.poster = poster;
    this.id=id;
    this.title = title;
    this.Genres = Genres;
  }


  public String getGenres() {
    String gnrs = "";
    BiMap kv = new HashImages("s").getHash2().inverse();
    for (int i = 0; i < Genres.length(); i++) {

      try {
        gnrs = (String) kv.get(Genres.get(i)) + ", " + gnrs;
      } catch (JSONException e) {
        e.printStackTrace();
      }

    }
    return gnrs;
  }

  public String getId() {
    return id;
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

    if (poster.equals("null") || poster==null)
    {

      return "https://reelcinemas.ae/Images/Movies/not-found/no-poster.jpg" ;
    }

    else{

      return "https://image.tmdb.org/t/p/w500/" + poster;
    }


  }

}