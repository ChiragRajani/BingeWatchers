package com.example.bingewatchers;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private EditText email, pwd;
    private Button btn;
    private TextView reg;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
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
       // toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
      //  initNavigationDrawer();


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

//    public void initNavigationDrawer() {
//
//        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//
//                int id = menuItem.getItemId();
//
//                switch (id){
//                    case R.id.home:
//                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
//              //          drawerLayout.closeDrawers();
//                        break;
//                    case R.id.Groups:
//                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.logout:
//                        finish();
//
//                }
//                return true;
//            }
//        });
//        View header = navigationView.getHeaderView(0);
//        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
//        tv_email.setText("raj.amalw@learn2crack.com");
//        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
//
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
//
//            @Override
//            public void onDrawerClosed(View v){
//                super.onDrawerClosed(v);
//            }
//
//            @Override
//            public void onDrawerOpened(View v) {
//                super.onDrawerOpened(v);
//            }
//        };
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//    }

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
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Intent i = new Intent(MainActivity.this, DashBoard.class);
            startActivity(i);
            System.out.println(" user logged in"+currentUser.getEmail());
        } else {

            System.out.println("no user is logged i");
        }
//        System.out.println("in on start   :   " + currentUser.toString());
//        updateUI(currentUser);
    }
    public void onBackPressed() {

            super.onBackPressed();
            finishAffinity();
        }

    }