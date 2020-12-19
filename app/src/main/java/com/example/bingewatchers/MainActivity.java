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
    private EditText email;
    private EditText pwd;
    private Button btn;
    private TextView reg,status;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("in on create   :   ");
        email = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        btn = findViewById(R.id.submit);
        reg = findViewById(R.id.reg);
        status=findViewById(R.id.status) ;

        mAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String email1 = email.getText().toString();
//                String pwd1 = pwd.getText().toString();
                String email1="chirag9893@gmail.com" ;
                String pwd1="1234567";
                signIn(email1, pwd1);


            }
        });
//        signout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              FirebaseUser user=mAuth.getCurrentUser() ;
//              mAuth.signOut();
//              if(mAuth.getCurrentUser() != null)
//                status.setText("User is " + user.getEmail()) ;
//              else
//                  status.setText("user logged out");
//            }
//        });
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
                            //FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            // FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser == null) {
                                status.setText("no user logged in");}
                            else {
                                status.setText("user is logged i"+ currentUser.getEmail());
                                Intent i = new Intent(MainActivity.this, DashBoard.class);
                                startActivity(i);
                            }
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Intent i = new Intent(MainActivity.this, DashBoard.class);
            startActivity(i);
            status.setText(" user logged in"+currentUser.getEmail());
        } else {

           status.setText("no user is logged i");
        }
//        System.out.println("in on start   :   " + currentUser.toString());
//        updateUI(currentUser);

    }
}