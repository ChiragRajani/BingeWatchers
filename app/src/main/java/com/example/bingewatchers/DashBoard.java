package com.example.bingewatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class DashBoard extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button signout,goToGroup;
    TextView user,list;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        user = findViewById(R.id.user);
        signout = findViewById(R.id.button);
        list=findViewById(R.id.list) ;
        goToGroup=findViewById(R.id.goToGroup) ;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user.setText("Hello User\n" + mAuth.getCurrentUser().getEmail());
        getGroups();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent i = new Intent(DashBoard.this, MainActivity.class);
                startActivity(i);
            }
        });
        goToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(DashBoard.this,CreateJoinGroup.class) ;
                startActivity(i) ;
            }
        });
    }

    public void getGroups() {
        //  Map<String, Object> user = new HashMap<>();

        String email = mAuth.getCurrentUser().getEmail();
        System.out.println("||||||||||||||| curent user is " + email);
        db.collection("Users").document(email).get();
        DocumentReference docRef = db.collection("Users").document(email);
        System.out.println("3453333333333333  "+docRef.toString());
        final Map<String, Object>[] messages = new Map[]{null};
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //if read successful

                    DocumentSnapshot document = task.getResult();
                    ArrayList<String> groups= (ArrayList<String>) document.get("Groups");
                    System.out.println("888888888888888888888 "+document.get("Group"));
                    if(groups!=null) {
                        list.setText("");
                        for (String i : groups) {

                            list.append(i+"\n");
                        }

                    }
                    else
                        list.setText("Oppos youre no into any group");

                    messages[0] = document.getData();

                    Toast.makeText(getApplicationContext(), document.getId() + " => " + document.getData(), Toast.LENGTH_LONG).show();

                } else {
                    System.out.println("error ");
                }
            }

        });
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
