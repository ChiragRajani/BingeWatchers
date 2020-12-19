package com.example.bingewatchers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DashBoard extends AppCompatActivity {
    FirebaseAuth mAuth ;
    Button signout ;
    TextView user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        user= findViewById(R.id.user) ;
        signout=findViewById(R.id.button) ;
        mAuth= FirebaseAuth.getInstance();


        user.setText("Hello User\n"+  mAuth.getCurrentUser().getEmail());

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent i = new Intent(DashBoard.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}