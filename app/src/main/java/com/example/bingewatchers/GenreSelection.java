package com.example.bingewatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreSelection extends AppCompatActivity {
   private ChipGroup chipGroup ;
    private Button btn;
    String email = SignUp.regEmail;
    private FirebaseAuth mAuth;
    private List<String> contactList;
    private static final String TAG = "GenreSelection";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_selection);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btn = findViewById(R.id.submit);
        chipGroup= findViewById(R.id.chipGroup);
        contactList = new ArrayList<>();

        contactList.add("Absurdist");
        contactList.add("Action" );
        contactList.add("Adventure");
        contactList.add("Comedy");
        contactList.add("Crime");
        contactList.add("Drama");
        contactList.add("Fantasy");
        contactList.add("Historical");
        contactList.add("Horror" );
        contactList.add("Magical realism");
        contactList.add("Mystery" );
        contactList.add("Paranoid fiction");
        contactList.add("Philosophical");
        contactList.add("Political");
        contactList.add("Romance");
        contactList.add("Saga") ;
        contactList.add("Satire");
        contactList.add("Science"  );
        contactList.add("Social") ;
        contactList.add("Speculative" );
        contactList.add("Thrille");
        contactList.add("Urban");
        contactList.add("Wester");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("------------------------------------");
                List<Integer> ids = chipGroup.getCheckedChipIds();
                System.out.println("-----------------------------------"+ids);
                addGenres(email, liste(ids));
            }
        });
    }
    Map<String,Object> liste(List<Integer> ids){
        List<String> genreL=new ArrayList<>();
        Map<String, Object> genreM = new HashMap<>();
        for(int i : ids){
            int y= i-1;
            genreL.add(contactList.get(y));
        }
        genreM.put("Genres",genreL);
        System.out.println(genreM);
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
        Log.d(TAG, "===========Genres Added");
        Toast.makeText(GenreSelection.this, "Genres updated", Toast.LENGTH_LONG);
    }
    public void onBackPressed() {

        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),DashBoard.class));
    }
}