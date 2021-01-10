package com.example.bingewatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class Activity_profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button change ;
    String email ;
    FirebaseFirestore db ;
    ImageView userPicture ;
    EditText userName,userEmail;
    static EditText dob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialiseFields();
        getInfo();
        change.setEnabled(false);

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

    public void getInfo(){
        DocumentReference docRef = db.collection("Users").document(email);

        final Map<String, Object>[] messages = new Map[]{null};
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String name=document.getString("Name") ;
                    String username=document.getString("Username");
                    String date=document.getString("Date of Birth");
                    userEmail.setText(username);
                    userName.setText(name);
                    dob.setText(date);
                    Glide.with(Activity_profile.this).asDrawable()
                            .load("https://ui-avatars.com/api/background=random?name=" + name)
                            .into(userPicture);
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

    public void initialiseFields(){
        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        db= FirebaseFirestore.getInstance();
        change=findViewById(R.id.updateInfo) ;
        userEmail=findViewById(R.id.userEmail) ;
        userName=findViewById(R.id.userName) ;
        dob=findViewById(R.id.dob) ;
        userPicture=findViewById(R.id.account_profile) ;
    }
}