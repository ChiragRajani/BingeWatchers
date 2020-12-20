package com.example.bingewatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreSelection extends AppCompatActivity {
    ChipsInput chipsInput,chip1;
    private Button btn;
    String email = SignUp.regEmail;
    private FirebaseAuth mAuth;
    private static final String TAG = "GenreSelection";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_selection);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btn = findViewById(R.id.submit);
//        chipsInput = (ChipsInput) findViewById(R.id.chips_input);
        chip1 = findViewById(R.id.chip1);
        List<Chip> contactList = new ArrayList<>();
//        chipsInput.setFilterableList(contactList);
        Chip ch = new Chip();

//        chipsInput.setFilterableList(ch.getList());

//        chip1.setOnCheckedChangeListener{}


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("Selected List is\n" + chipsInput.getSelectedChipList().size());
//                List<Chip> contactsSelected = (List<Chip>) chipsInput.getSelectedChipList();
//
//                System.out.println(contactsSelected.toString());
//
//                List<String> namesList = new ArrayList<String>();
//                for (Chip person : contactsSelected) {
//                    namesList.add(person.getLabel());
//                }
//                Map<String, Object> genre = new HashMap<>();
//                genre.put("Genres",namesList);
//                addGenres(email, genre);
            }
        });
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
        Intent i = new Intent(GenreSelection.this,DashBoard.class);
        startActivity(i);
        Log.d(TAG, "===========Genres Added");
        Toast.makeText(GenreSelection.this, "Genres updated", Toast.LENGTH_LONG);
    }
}