package com.example.bingewatchers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    static EditText dob;
    static String regEmail;
    private TextInputEditText email1, pwd, name;
    private Button btn;
    private TextView tv4;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String regPwd, regName, regDOB;
    ViewGroup progressView, viewGroup;
    View v;
    ProgressBar mProgressBar;
    boolean isProgressShowing = false;
    private ChipGroup chipGroup;
    private List<String> contactList;


//    -------------------------------------------


//    -------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        chipGroup = findViewById(R.id.chipGroup);

        contactList = new ArrayList<>();

        contactList.add("Action");
        contactList.add("Adventure");
        contactList.add("Animation");
        contactList.add("Comedy");
        contactList.add("Crime");
        contactList.add("Documentary");
        contactList.add("Drama");
        contactList.add("Family");
        contactList.add("Fantasy");
        contactList.add("History");
        contactList.add("Horror");
        contactList.add("Music");
        contactList.add("Mystery");
        contactList.add("Romance");
        contactList.add("Science Fiction");
        contactList.add("TV Movie");
        contactList.add("Thriller");
        contactList.add("War");
        contactList.add("Western");


        email1 = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        btn = findViewById(R.id.submit);
        dob = findViewById(R.id.dob);
        name = findViewById(R.id.Name);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        v = this.findViewById(android.R.id.content).getRootView();
        viewGroup = (ViewGroup) v;
        tv4 = findViewById(R.id.textView4);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> ids = chipGroup.getCheckedChipIds();
                regEmail = email1.getText().toString();
                regPwd = pwd.getText().toString();
                regDOB = dob.getText().toString();
                regName = name.getText().toString();

                if (regEmail.equals("") || regPwd.equals("") || regDOB.equals("") || regName.equals("")) {
                    tv4.setText("*All fields are mandatory.");
                } else if (ids.size() == 0) {
                    tv4.setText("Please select atleast one genre.");
                } else {

                    System.out.println("email: " + regEmail + "\npassword is " + regPwd + "\nDOB is " + regDOB + "\nregName" + regName);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Username", regEmail);
                    user.put("password", regPwd);
                    user.put("Date of Birth", regDOB);
                    user.put("Name", regName);
                    user.put("Genres", liste(ids));
                    createAccount(regEmail, regPwd, user);
                    showProgressingView();
                }

            }
        });

    }

    void updateUserinDB(Map userinfo) {
        try {
            db.collection("Users")
                    .document(regEmail)
                    .set(userinfo);

            db.collection("Groups").document("Default").update("Members", FieldValue.arrayUnion(regEmail));
            db.collection("Users").document(regEmail).update("Groups", FieldValue.arrayUnion("Default"));

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Group Chats").child("Default");
            Message obj = new Message(regName, regName + " just Joined the group", Calendar.getInstance().getTime().toString(), regEmail, "activity");
            myRef.push().setValue(obj);

            hideProgressingView();
        } catch (@NonNull Exception e) {
            Log.w(TAG, "Error adding document", e);
            Toast.makeText(SignUp.this, "Account  Not created " + e.getMessage(), Toast.LENGTH_SHORT);

        }
        Log.d(TAG, "===========DocumentSnapshot added with ID: ");
        Toast.makeText(SignUp.this, "Account Created and Values updated", Toast.LENGTH_SHORT);
    }

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
                            tv4.setText("");
                            Toast.makeText(SignUp.this, "Account Created", Toast.LENGTH_SHORT);
                            updateUserinDB(userInfo);
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(SignUp.this, DashBoard.class);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            tv4.setText("Authentication failed." + task.getException().getMessage());

                            email1.setText("");
                            pwd.setText("");
                            dob.setText("");
                            name.setText("");
                            chipGroup.clearCheck();
                            hideProgressingView();

                        }

                    }
                });
    }

    public void showProgressingView() {
        if (!isProgressShowing) {
            isProgressShowing = true;
            progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.circle_progressbar, null);
            viewGroup.addView(progressView);
            mProgressBar = progressView.findViewById(R.id.progressBar1);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }

    ArrayList<String> liste(@NotNull List<Integer> ids) {
        ArrayList<String> genreL = new ArrayList<>();

        for (int i : ids) {
            int y = i - 1;
            if (y > 19) {
                break;
            } else
                genreL.add(contactList.get(y));
        }
        return genreL;
    }


}