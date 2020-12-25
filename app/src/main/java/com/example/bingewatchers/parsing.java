package com.example.bingewatchers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

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
    private Context context;

    private String query  ;
    static JSONObject kl;
    public parsing(Context context,String query) {
        this.context = context;
        this.query=query ;
    }
    @Override
    protected ArrayList<String> doInBackground(Object[] objects) {
        ArrayList<String> he=new ArrayList<>() ;
        try {
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
                System.out.println("Sting Builder is    " + dta);
                kl = new JSONObject(dta.toString());
            } else {
                kl = null;
            }
       System.out.println(kl.toString().replace("\\/","")) ;
            System.out.println("================ doInBackground COMPLETED");

        } catch (IOException | JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        System.out.println("4444444444444444444444444444444444444444444444iit"+kl);
//        for(int i=0;i<kl.length();i++)
//        {
//            try {
//                he.add(kl[0].getString("title")) ;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//        for (int i = 0; i < he.size(); i++) {
//            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%   "+he.get(i)+" 777777777777777777777777777");
//        }
        return he;
    }
}
