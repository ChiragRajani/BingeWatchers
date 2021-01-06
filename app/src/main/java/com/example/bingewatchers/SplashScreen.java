package com.example.bingewatchers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    private static int SplashOut = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                // FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    Intent i = new Intent(SplashScreen.this, DashBoard.class);
                    startActivity(i);
                    System.out.println(" user logged in" + currentUser.getEmail());
                } else {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    System.out.println("no user is logged i");
                }
                finish();
            }
        }, SplashOut);
    }
}