package com.example.bingewatchers;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    private EditText grpName;
    private FloatingActionButton show;

    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout bottom_sheet;
    private int btnFunc = 0;
    private String message, name, notgrpname;

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
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        message = grpName.getText().toString();


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

                    messageToBeSent obj = new messageToBeSent(name, message, Calendar.getInstance().getTime().toString(), mAuth.getCurrentUser().getEmail(), "message");
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
                    System.out.println("55555555555555555555555555555555555555555555555 query is "+charSequence.toString());
                    new parsing(getApplicationContext(),charSequence.toString(),0).execute() ;
                    show.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_send));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                Toast.makeText(ChatWindow.this, "SLIDING!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}