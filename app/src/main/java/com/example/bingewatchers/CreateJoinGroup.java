package com.example.bingewatchers;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CreateJoinGroup extends AppCompatActivity {
    Button create, join;
    TextView status;
    FirebaseAuth mAuth;
    FirebaseFirestore db, rootRef;
    String TAG = "CreateJoinGroup";
    private TextInputEditText createGroupName, joinGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);
        createGroupName = findViewById(R.id.createGroupName);
        joinGroupName = findViewById(R.id.joinGroupName);
        create = findViewById(R.id.Create);
        join = findViewById(R.id.Join);
        status = findViewById(R.id.status);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docId = createGroupName.getText().toString().replace('\n', ' ').trim();
                String regEmail = mAuth.getCurrentUser().getEmail();
                HashMap<String, Object> groupInfo = new HashMap<String, Object>();
                groupInfo.put("Created By", regEmail);

                DocumentReference docIdRef = rootRef.collection("Groups").document(docId);
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "%%%%%%%%%%%%%555b  DOCUMENT EXISTS");
                                status.setText("Group already exists");
                            } else {
                                Log.d(TAG, "%%%%%%%%%%%%%555b  DOCUMENT DOESENT EXISTS");
                                status.setText("Group Created with name\n" + docId);

                                db.collection("Groups")
                                        .document(docId)
                                        .set(groupInfo);
                                rootRef.collection("Groups").document(docId).update("Members", FieldValue.arrayUnion(regEmail));
                                rootRef.collection("Users").document(regEmail).update("Groups Created", FieldValue.arrayUnion(docId));
                                rootRef.collection("Users").document(regEmail).update("Groups", FieldValue.arrayUnion(docId));

                            }
                        } else {
                            Log.d(TAG, "%%%%%%%%%%%%%555b  EXCEPTION OCCURED: " + task.getException().getMessage());
                            status.setText("EXCEPTION OCCURED:\n " + task.getException().getMessage());
                        }

                        createGroupName.setText("");
                        joinGroupName.setText("");
                    }
                });
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = joinGroupName.getText().toString().replace('\n', ' ').trim();
                String user = mAuth.getCurrentUser().getEmail();

                DocumentReference docIdRef = rootRef.collection("Groups").document(groupName);
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
//                            rootRef.collection("Groups").document(groupName).update("Members","erygersfgrt4g4rfgyg");
                                rootRef.collection("Groups").document(groupName).update("Members", FieldValue.arrayUnion(user));
                                rootRef.collection("Users").document(user).update("Groups", FieldValue.arrayUnion(groupName));
                                status.setText("Group Joined\n" + groupName);
                                //System.out.println("%%%%%%%%%%%%%555b  DOCUMENT EXISTS");
                                //


                            } else {
                                status.setText("Group doesn't exist with name\n" + groupName);
                                Log.d(TAG, "%%%%%%%%%%%%%555b  DOCUMENT DOES'NT EXISTS");

                            }
                        } else {
                            Log.d(TAG, "%%%%%%%%%%%%%555b  EXCEPTION OCCURED: " + task.getException().getMessage());
                            status.setText("EXCEPTION OCCURED:\n " + task.getException().getMessage());
                        }

                        createGroupName.setText("");
                        joinGroupName.setText("");
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
//    public boolean onSupportNavigateUp() {
//        finish();
//        return true;
//    }
