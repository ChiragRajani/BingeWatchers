package com.example.bingewatchers;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText pwd;
    private Button btn;
    ChipsInput chipsInput ;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        btn = findViewById(R.id.submit);
        chipsInput = (ChipsInput) findViewById(R.id.chips_input);
        mAuth = FirebaseAuth.getInstance();
        List<Chip> contactList = new ArrayList<>();
        chipsInput.setFilterableList(contactList);
        Chip ch=new Chip() ;
        db = FirebaseFirestore.getInstance();
        chipsInput.setFilterableList(ch.getList());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              String  email1 = email.getText().toString();
                String pwd1 = pwd.getText().toString() ;

                System.out.println("email: "+email1 +"\npassword is "+pwd1+"\n");
                System.out.println("Selected List is\n"+chipsInput.getSelectedChipList().size());
                List<Chip> contactsSelected = (List<Chip>) chipsInput.getSelectedChipList();

                System.out.println(contactsSelected.toString());
//                       for (int i = 0; i <contactsSelected.size(); i++)
//                     System.out.print(contactsSelected.get(i).getInfo()+"\n");

                List<String> namesList = new ArrayList<String>();
                for(Chip person : contactsSelected){
                    namesList.add(person.getLabel());
                }
//                for (int i = 0; i <namesList.size(); i++)
//                    System.out.print(namesList.get(i)+"\n");


                Map<String, Object> user = new HashMap<>();
                user.put("Username", email1);
                user.put("password",pwd1) ;
                user.put("Genere", namesList);
                createAccount(email1,pwd1,user);

            }
        });
    }
    void updateUserinDB(Map userinfo){
        db.collection("Users")
                .add(userinfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(MainActivity.this,"Account Created and Values updated",Toast.LENGTH_SHORT) ;


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(MainActivity.this,"Account  Not created "+e.getMessage(),Toast.LENGTH_SHORT) ;
                    }
                });
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }
    public void createAccount(String email, String password,Map userInfo){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(MainActivity.this,"Account Created",Toast.LENGTH_SHORT) ;
                            updateUserinDB(userInfo);
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed."+ task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}