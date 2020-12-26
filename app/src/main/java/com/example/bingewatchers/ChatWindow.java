package com.example.bingewatchers;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import java.util.Map;

public class ChatWindow extends AppCompatActivity {
    private EditText grpName;
    private FloatingActionButton show;
    private String TAG = "CHAT WINDOW";
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    //EditText grpName;
    EditText movieName;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout bottom_sheet;
    private int btnFunc = 0;
    private String message, name, notgrpname;
    ListView list;
   ListViewAdapter adapter;
    ArrayList<String> msgs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_windiow);


        grpName = findViewById(R.id.grpName);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        show = findViewById(R.id.show);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        notgrpname = getIntent().getSerializableExtra("Group Name").toString();
        name = getIntent().getSerializableExtra("Name").toString();
        myRef = FirebaseDatabase.getInstance().getReference("Group Chats").child(notgrpname);
        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("Group Chats").child(notgrpname).child("message");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        message = grpName.getText().toString();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    HashMap obj = (HashMap) postSnapshot.getValue();
//                    msgs.add(new messageToBeSent(obj.get("name"),));
                    System.out.println(obj.get("message"));
                    msgs.add(obj.get("message").toString());

                    MessagesListAdapter<Message> adapter = new MessagesListAdapter<>(obj.get("senderEmail").toString(), null);
                    msgs.setAdapter(adapter);


                }


                String value;
                //value = dataSnapshot.getValue(messageToBeSent.class);
                //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+value);
//                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+dataSnapshot.getValue(messageToBeSent.class).getMessage());
//                Log.d(TAG, "Value is: " + value.getMessage());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

//        MessagesListAdapter<Message> adapter = new MessagesListAdapter<>(senderId, null);
//        messagesList.setAdapter(adapter);


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnFunc == 0) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                show.setText("Close sheet");
                    } else {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                show.setText("Expand sheet");
                    }
                }
                if (btnFunc == 1) {

                    Message obj = new Message(name, message, Calendar.getInstance().getTime(), mAuth.getCurrentUser().getEmail(), "message");
                    myRef.push().setValue(obj);
                    Toast.makeText(ChatWindow.this, "Message Sent", Toast.LENGTH_LONG).show();
                    grpName.setText("");

                }
            }
        });

        grpName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                message = grpName.getText().toString();
                if (message.equals("")) {

                    btnFunc = 0;
                    show.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add));

                } else {
                    btnFunc = 1;
                    System.out.println("55555555555555555555555555555555555555555555555 query is " + charSequence.toString());

                    new parsing(getApplicationContext(), charSequence.toString(), 0,list).execute();
                    System.out.println("55555555555555555555555555555555555555555555555 query is "+charSequence.toString());
                  //  new parsing(getApplicationContext(),charSequence.toString(),0).execute() ;
                  //   new parsing(getApplicationContext(),charSequence.toString(),0).execute() ;
                    show.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_send));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//list.setOnItemClickListener(new o
//    @Override
//    public void onClick(View view) {
//        Toast.makeText(getBaseContext(),"clicked "+list.getSelectedItem(),Toast.LENGTH_SHORT).show();
//    }
//});

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                    case BottomSheetBehavior.STATE_COLLAPSED:

                    {
                        list.setAdapter(null);
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                Toast.makeText(ChatWindow.this, "SLIDING!!", Toast.LENGTH_SHORT).show();
            }
        });

movieName.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        System.out.println("666666666666666666666666666666666666666666666"+charSequence.toString());
        new parsing(getApplicationContext(),charSequence.toString(),0,list).execute() ;

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
});
    }
}