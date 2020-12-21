package com.example.bingewatchers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private TextInputEditText email, pwd, name;
    static EditText dob ;
    private Button btn;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    FirebaseFirestore db;
    String regPwd, regName,regDOB;
    static String regEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        btn = findViewById(R.id.submit);
        dob = findViewById(R.id.dob);
        name = findViewById(R.id.Name);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regEmail = email.getText().toString();
                regPwd = pwd.getText().toString();
                regDOB = dob.getText().toString();
                regName = name.getText().toString();
                System.out.println("email: " + regEmail + "\npassword is " + regPwd + "\nDOB is " + regDOB + "\nregName" + regName);

                Map<String, Object> user = new HashMap<>();
                user.put("Username", regEmail);
                user.put("password", regPwd);
                user.put("Date of Birth", regDOB);
                user.put("Name", regName);
                createAccount(regEmail, regPwd, user);


                email.setText("");
                pwd.setText("");
            }
        });

    }

    void updateUserinDB(Map userinfo) {
        try {
            db.collection("Users")
                    .document(regEmail)
                    .set(userinfo);
        } catch (@NonNull Exception e) {
            Log.w(TAG, "Error adding document", e);
            Toast.makeText(SignUp.this, "Account  Not created " + e.getMessage(), Toast.LENGTH_SHORT);

        }
        Log.d(TAG, "===========DocumentSnapshot added with ID: ");
        Toast.makeText(SignUp.this, "Account Created and Values updated", Toast.LENGTH_SHORT);
    }
//
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void createAccount(String email, String password, Map userInfo) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUp.this, "Account Created", Toast.LENGTH_SHORT);
                           //Snackbar.makeText(SignUp.this," ", Snackbar.LENGTH_SHORT).show();
                            updateUserinDB(userInfo);
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(SignUp.this, GenreSelection.class);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
}