package com.example.bingewatchers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo extends AppCompatActivity {
    TextView grpname ;
    String GroupName ;
    ListView membersList ;
    FirebaseFirestore db ;
    ArrayAdapter<String> arrayAdapter ;
   List<String> members=new ArrayList<>() ;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        grpname=findViewById(R.id.groupname) ;
        membersList=findViewById(R.id.membersList) ;
        db = FirebaseFirestore.getInstance();
        grpname.setText(getIntent().getSerializableExtra("Group Name").toString());
        GroupName=getIntent().getSerializableExtra("Group Name").toString() ;
        getMembers(GroupName) ;
        DocumentReference docRef = db.collection("Groups").document(GroupName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document=task.getResult() ;
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" +document.get("Members"));
                members=(List<String>)document.get("Members") ;
                for(String i:members)
                    System.out.println("******************************** "+ i);


                arrayAdapter  = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, members);
                // membersList.setAdapter(arrayAdapter);
                membersList.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

            }
        });

    }
    public void getMembers(String groupName) {

    }
}