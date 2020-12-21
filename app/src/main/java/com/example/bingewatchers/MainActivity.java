package com.example.bingewatchers;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private EditText email, pwd;
    private Button btn;
    private TextView reg;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("in on create   :   ");
        email = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        btn = findViewById(R.id.submit);
        reg = findViewById(R.id.reg);


        mAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email1 = email.getText().toString();
                String pwd1 = pwd.getText().toString();

//                String email1="cjajmera009@gsdkk.com" ;
//                String pwd1="poiuytrewq";

                signIn(email1, pwd1);


            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Registration Button Clicked!! ", Toast.LENGTH_SHORT);
                System.out.println("==================================================");
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });
    }

    private void signIn(String email1, String password) {
        mAuth.signInWithEmailAndPassword(email1, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "SIGN IN HO GYA BHENCHOOOOODDDDD!!!!!!!!!!!!",
                                    Toast.LENGTH_LONG).show();
                            System.out.println("SIGN IN HO GYA BHENCHOOOOODDDDD!!!!!!!!!!!!");

                            if (mAuth.getCurrentUser() != null) {
                                Intent i = new Intent(MainActivity.this, DashBoard.class);
                                startActivity(i);
                            }
//                                Intent i = new Intent(MainActivity.this, DashBoard.class);
//                                startActivity(i);
                            }

                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Intent i = new Intent(MainActivity.this, DashBoard.class);
            startActivity(i);

        } else {
            System.out.println("User is logged i");
        }
    }
}