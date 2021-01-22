package com.example.bingewatchers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Activity_profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button change;
    String email;
    FirebaseFirestore db;
    DocumentReference docRef;
    ImageView userPicture;
    EditText userName, userEmail;
    static EditText dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialiseFields();
        getInfo();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        change.setEnabled(false);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changedName = userName.getText().toString();
                String changedDOB = dob.getText().toString();
                docRef.update("Name", changedName);
                docRef.update("Date of Birth", changedDOB);
                Toast.makeText(Activity_profile.this, "Credentials Changed", Toast.LENGTH_LONG).show();


            }
        });

    }


    TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            change.setEnabled(true);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void getInfo() {
        DocumentReference docRef = db.collection("Users").document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String name = document.getString("Name");
                    String username = document.getString("Username");
                    String date = document.getString("Date of Birth");
                    userEmail.setText(username);
                    userName.setText(name);
                    dob.setText(date);
                    if (mAuth.getCurrentUser().getPhotoUrl() == null) {
                        Glide.with(Activity_profile.this).asDrawable()
                                .load("https://ui-avatars.com/api/background=random?name=" + name)
                                .into(userPicture);
                    } else {
                        Glide.with(Activity_profile.this).asDrawable()
                                .load(mAuth.getCurrentUser().getPhotoUrl().toString())
                                .into(userPicture);
                    }
                    userName.addTextChangedListener(generalTextWatcher);
                    dob.addTextChangedListener(generalTextWatcher);
                }
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void initialiseFields() {
        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        db = FirebaseFirestore.getInstance();
        change = findViewById(R.id.updateInfo);
        userEmail = findViewById(R.id.userEmail);
        userName = findViewById(R.id.userName);
        dob = findViewById(R.id.dob);
        userPicture = findViewById(R.id.account_profile);

        docRef = db.collection("Users").document(email);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}