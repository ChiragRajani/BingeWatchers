package com.example.bingewatchers;

import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class CreateJoinGroup extends AppCompatActivity {
    Button create, join;
    TextView status;
    FirebaseAuth mAuth;
    FirebaseFirestore db, rootRef;
    DocumentReference userInfo;
    String userName;
    DatabaseReference myRef;
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
        userInfo = rootRef.collection("Users").document(mAuth.getCurrentUser().getEmail());
        userInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    userName = document.get("Name").toString();
                }
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docId = createGroupName.getText().toString().replace('\n', ' ').trim();
                String regEmail = mAuth.getCurrentUser().getEmail();
                if (docId == null || docId.equals("")) {
                    status.setText("Group name cannot be empty");
                    createGroupName.setText("");
                    joinGroupName.setText("");
                } else {
                    HashMap<String, Object> groupInfo = new HashMap<String, Object>();
                    groupInfo.put("Created By", regEmail);

                    DocumentReference docIdRef = rootRef.collection("Groups").document(docId);
                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    status.setText("Group already exists");
                                } else {

                                    status.setText("Group Created with name\n" + docId);

                                    db.collection("Groups")
                                            .document(docId)
                                            .set(groupInfo);
                                    rootRef.collection("Groups").document(docId).update("Members", FieldValue.arrayUnion(regEmail));
                                    rootRef.collection("Users").document(regEmail).update("Groups Created", FieldValue.arrayUnion(docId));
                                    rootRef.collection("Users").document(regEmail).update("Groups", FieldValue.arrayUnion(docId));

                                }
                            } else {

                                status.setText("EXCEPTION OCCURED:\n " + task.getException().getMessage());
                            }

                            createGroupName.setText("");
                            joinGroupName.setText("");
                            DashBoard.refreshListener.onRefresh();
                        }
                    });
                }
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = joinGroupName.getText().toString().replace('\n', ' ').trim();
                String user = mAuth.getCurrentUser().getEmail();
                if (groupName == null || groupName.equals("")) {
                    status.setText("Group name cannot be empty");
                    createGroupName.setText("");
                    joinGroupName.setText("");
                } else {
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

                                    myRef = FirebaseDatabase.getInstance().getReference("Group Chats").child(groupName);
                                    Message obj = new Message(userName, userName + " just Joined the group", Calendar.getInstance().getTime().toString(), user, "activity");
                                    myRef.push().setValue(obj);


                                    status.setText("Group Joined\n" + groupName);


                                } else {
                                    status.setText("Group doesn't exist with name\n" + groupName);


                                }
                            } else {

                                status.setText("EXCEPTION OCCURED:\n " + task.getException().getMessage());
                            }

                            createGroupName.setText("");
                            joinGroupName.setText("");
                            DashBoard.refreshListener.onRefresh();
                        }
                    });
                }
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

