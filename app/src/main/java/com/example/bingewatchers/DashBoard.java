package com.example.bingewatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DashBoard extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button signout, goToGroup;
    TextView user, list;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        mAuth = FirebaseAuth.getInstance();
        if (checkUser()){
            user = findViewById(R.id.user);
        signout = findViewById(R.id.button);
        list = findViewById(R.id.list);
        goToGroup = findViewById(R.id.goToGroup);

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
                Intent i = new Intent(DashBoard.this, CreateJoinGroup.class);
                startActivity(i);
            }
        });
    }
    }

    public void getGroups() {
        //  Map<String, Object> user = new HashMap<>();

        String email = mAuth.getCurrentUser().getEmail();
        System.out.println("||||||||||||||| curent user is " + email);
        db.collection("Users").document(email).get();
        DocumentReference docRef = db.collection("Users").document(email);
        System.out.println("3453333333333333  " + docRef.toString());
        final Map<String, Object>[] messages = new Map[]{null};
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //if read successful

                    DocumentSnapshot document = task.getResult();
                    ArrayList<String> groups = (ArrayList<String>) document.get("Groups");
                    System.out.println("888888888888888888888 " + document.get("Group"));
                    if (groups != null) {
                        list.setText("");
                        for (String i : groups) {

                            list.append(i + "\n");
                        }

                    } else
                        list.setText("Oppos youre no into any group");

                    messages[0] = document.getData();

                    Toast.makeText(getApplicationContext(), document.getId() + " => " + document.getData(), Toast.LENGTH_LONG).show();

                } else {
                    System.out.println("error ");
                }
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();

        checkUser();

    }

    public boolean checkUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent i = new Intent(DashBoard.this, MainActivity.class);
            startActivity(i);
//            System.out.println(" user logged in" + currentUser.getEmail());
            return false;
        } else {

            System.out.println("User is logged i");
            return true;
        }

    }
}
