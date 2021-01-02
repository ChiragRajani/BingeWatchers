package com.example.bingewatchers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo extends AppCompatActivity {
    TextView grpname, createdBy, membersCount;
    Button leaveGroup;
    String GroupName;
    ListView membersList;
    FirebaseFirestore db;
    ArrayAdapter<String> arrayAdapter;
    List<String> members = new ArrayList<>();
    FirebaseAuth mAuth;
    String CURRENT_USER;
    DocumentReference docRef,userRef ;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        grpname = findViewById(R.id.groupname);
        membersList = findViewById(R.id.membersList);
        createdBy = findViewById(R.id.createdBy);
        membersCount = findViewById(R.id.membersCount);
        leaveGroup = findViewById(R.id.leaveGroup);
        mAuth = FirebaseAuth.getInstance();
        CURRENT_USER = mAuth.getCurrentUser().getEmail();
        db = FirebaseFirestore.getInstance();
        grpname.setText(getIntent().getSerializableExtra("Group Name").toString());
        GroupName = getIntent().getSerializableExtra("Group Name").toString();

        docRef = db.collection("Groups").document(GroupName);
        userRef= db.collection("Users").document(CURRENT_USER.trim());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                createdBy.setText("Created by: " + document.get("Created By").toString());
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + document.get("Members"));
                members = (List<String>) document.get("Members");
                membersCount.setText(members.size() + " Members");
                for(String i:members)
                    System.out.println("******************************** " + i);


                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, members);
                // membersList.setAdapter(arrayAdapter);
                membersList.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

            }
        });
        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.update("Groups", FieldValue.arrayRemove(GroupName)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
            
                            System.out.println("Sucessfully left the group");
                        DashBoard.refreshListener.onRefresh();


                        startActivity(new Intent(GroupInfo.this,DashBoard.class));
//                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Group Left", Snackbar.LENGTH_LONG);
//                        snackbar.show();
                    }

                });


            }
        });

    }

}