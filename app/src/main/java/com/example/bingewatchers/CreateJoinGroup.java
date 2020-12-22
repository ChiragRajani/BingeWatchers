package com.example.bingewatchers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.GetDocumentRequestOrBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CreateJoinGroup extends AppCompatActivity {
    private TextInputEditText createGroupName,joinGroupName ;
    Button create,join ;
    TextView status ;
    FirebaseAuth mAuth ;
    FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);
        createGroupName=findViewById(R.id.createGroupName) ;
        joinGroupName=findViewById(R.id.joinGroupName) ;
        create=findViewById(R.id.Create) ;
        join=findViewById(R.id.Join) ;
        status=findViewById(R.id.status) ;
        db = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance() ;
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docId=createGroupName.getText().toString().replace('\n',' ').trim()  ;
                String regEmail=mAuth.getCurrentUser().getEmail() ;
                HashMap<String,Object> groupInfo=new HashMap<String, Object> ();
                groupInfo.put("Created By",regEmail) ;
               // Map<String, Object> members= new HashMap<>();
                DocumentReference docIdRef = rootRef.collection( "Groups").document(docId);
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                System.out.println("%%%%%%%%%%%%%555b  DOCUMENT EXISTS");
                                status.setText( "Cant Create Group coz\n DOCUMENT EXISTS");
                            } else {
                                System.out.println("%%%%%%%%%%%%%555b  DOCUMENT DOESENT EXISTS");

                                db.collection("Groups")
                                        .document(docId)
                                        .set(groupInfo);
                                status.setText("Group Created with name\n"+docId);
                                rootRef.collection("Groups").document(docId).update("Members", FieldValue.arrayUnion(regEmail)) ;
                            }
                        } else {
                            System.out.println("%%%%%%%%%%%%%555b  EXCEPTION OCCURED: "+task.getException().getMessage());
                            status.setText("EXCEPTION OCCURED:\n "+task.getException().getMessage());
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
            String groupName=joinGroupName.getText().toString().replace('\n',' ').trim() ;
            String user=mAuth.getCurrentUser().getEmail() ;

                    DocumentReference docIdRef = rootRef.collection( "Groups").document(groupName);
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                            rootRef.collection("Groups").document(groupName).update("Members","erygersfgrt4g4rfgyg");
                            rootRef.collection("Groups").document(groupName).update("Members", FieldValue.arrayUnion(user)) ;
                            status.setText( "Cant Find Group with name\n"+groupName);
                            //System.out.println("%%%%%%%%%%%%%555b  DOCUMENT EXISTS");
                            //status.setText("Group Found with name\n"+groupName);

                        } else {
                            System.out.println("%%%%%%%%%%%%%555b  DOCUMENT DOESENT EXISTS");

//                            db.collection("Groups")
//                                    .document(docId)
//                                    .set(groupInfo);



                        }
                    } else {
                        System.out.println("%%%%%%%%%%%%%555b  EXCEPTION OCCURED: "+task.getException().getMessage());
                        status.setText("EXCEPTION OCCURED:\n "+task.getException().getMessage());
                    }

                    createGroupName.setText("");
                    joinGroupName.setText("");
                }
            });
        }
    });
    }
}