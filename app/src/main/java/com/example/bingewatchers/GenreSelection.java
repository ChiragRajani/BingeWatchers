package com.example.bingewatchers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreSelection extends AppCompatActivity {
    private ChipGroup chipGroup;
    private Button btn;
    String email  ;
    TextView skipLayout;
    private FirebaseAuth mAuth;
    private List<String> contactList;
    private static final String TAG = "GenreSelection";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_selection);

        mAuth = FirebaseAuth.getInstance();
        email=mAuth.getCurrentUser().getEmail() ;
        db = FirebaseFirestore.getInstance();
        btn = findViewById(R.id.submit);
        chipGroup = findViewById(R.id.chipGroup);
        skipLayout=findViewById(R.id.skipLayout) ;
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
        String s=getIntent().getSerializableExtra("from").toString() ;
        if(s.equals("Dashboard")){
            btn.setText("Update");
            skipLayout.setVisibility(View.GONE);

        }
        skipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Integer> ids = chipGroup.getCheckedChipIds();
                addGenres(email, liste(ids));
            }
        });
    }

    public void skip(){
        Intent i = new Intent(GenreSelection.this, DashBoard.class);
        startActivity(i);
    }
    Map<String, Object> liste(@NotNull List<Integer> ids) {
        List<String> genreL = new ArrayList<>();
        Map<String, Object> genreM = new HashMap<>();
        for (int i : ids) {
            int y = i - 1;
            genreL.add(contactList.get(y));
        }
        genreM.put("Genres", genreL);
        return genreM;
    }

    private void addGenres(String email, Map<String, Object> namesList) {
        try {
            db.collection("Users")
                    .document(email)
                    .update(namesList);
        } catch (@NonNull Exception e) {
            Log.w(TAG, "Error adding genres", e);
            Toast.makeText(GenreSelection.this, "Genres  Not added " + e.getMessage(), Toast.LENGTH_LONG);
        }
        Intent i = new Intent(GenreSelection.this, DashBoard.class);
        startActivity(i);

        Toast.makeText(GenreSelection.this, "Genres updated", Toast.LENGTH_LONG);
    }

    public void onBackPressed() {

        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
    }
}