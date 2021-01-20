package com.example.bingewatchers;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

public class ChatWindow<ArrayList> extends AppCompatActivity {

    EditText movieName;
    ListView chatList;
    ListView list;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    ListViewAdapter adapter;
    java.util.ArrayList<Message> chats = new java.util.ArrayList<>();
    java.util.ArrayList<Movie> he = new java.util.ArrayList<>();
    Button btnSug;
    EditText movieReview;
    TextView hideSheet;

    private EditText grpName;
    private FloatingActionButton show;
    private String TAG = "CHAT WINDOW";
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout bottom_sheet;
    private int btnFunc = 0;
    private String message, name, notgrpname;
    private int i1, x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_windiow);
        grpName = findViewById(R.id.grpName);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        show = findViewById(R.id.show);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        list = findViewById(R.id.listview);
        notgrpname = getIntent().getSerializableExtra("Group Name").toString();
        setTitle(notgrpname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        name = getIntent().getSerializableExtra("Name").toString();
        myRef = FirebaseDatabase.getInstance().getReference("Group Chats").child(notgrpname);
        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("Group Chats").child(notgrpname).child("message");
        mAuth = FirebaseAuth.getInstance();
        //t = new ActionBarDrawerToggle(this, dl, R.string.drawer_open, R.string.drawer_close);


        btnSug = findViewById(R.id.button);
        movieReview = findViewById(R.id.movieReview);
        movieName = findViewById(R.id.movieName);

        db = FirebaseFirestore.getInstance();
        chatList = findViewById(R.id.chatsList);
        java.util.ArrayList<Message> chats = new java.util.ArrayList<>();
        message = grpName.getText().toString();
        ChatListAdapter chatAdapter = new ChatListAdapter(this, chats);
        hideSheet = findViewById(R.id.hideSheet);

        chatList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Message obj = dataSnapshot.getValue(Message.class);

                if (obj.getType() != null) {
                    chats.add(obj);
                    System.out.println("-------------- " + obj.getMessage());
                    chatList.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnFunc == 0) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    } else {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    }
                }
                if (btnFunc == 1) {

                    Message obj = new Message(name, message, Calendar.getInstance().getTime().toString(), mAuth.getCurrentUser().getEmail(), "message");
                    myRef.push().setValue(obj);

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
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_arrow_upward));

                } else {
                    btnFunc = 1;

                    show.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_send));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                x = 1;
                i1 = i;
                he = parsing.he1;
                Movie y = he.get(i);
                String selected = ((TextView) view.findViewById(R.id.movieName)).getText().toString();
//                String mvieName = y.getMovieName();
                Toast.makeText(ChatWindow.this, selected, Toast.LENGTH_SHORT).show();
                movieName.setText(selected);
                list.setAdapter(null);
            }
        });


        btnSug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Movie y = he.get(i1);
                    String mvieName = y.getMovieName();

                    String desc = mvieName + "(" + y.getMovieDate().substring(0, 4) + ")\n\n" + y.getDescription() + "\n\n" + name + "'s Review:" + movieReview.getText().toString();

                    Message obj = new Message(name, desc, Calendar.getInstance().getTime().toString(),
                            mAuth.getCurrentUser().getEmail(), y.getPoster(),
                            "Suggestion");
                    myRef.push().setValue(obj);
                    movieReview.setText("");
                    movieName.setText("");
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    DocumentReference docRef = db.collection("Groups").document(notgrpname);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            List<String> members = (List<String>) document.get("Members");

                            for (String i : members) {
                                if (!i.equals(mAuth.getCurrentUser().getEmail())) {
                                    Suggestion suggested = new Suggestion(y, name, Calendar.getInstance().getTime().toString(), notgrpname);
                                    db.collection("Users").document(i).update("Suggestion", FieldValue.arrayUnion(suggested));
                                    System.out.println("5555555555555555555555555555555555555  " + suggested.getSender());
                                }
                            }

                        }
                    });
                    DashBoard.refreshListener.onRefresh();
                    db.collection("Users").document(mAuth.getCurrentUser().getEmail()).update("Movies Watched", FieldValue.arrayUnion(movieName.getText().toString()));
                } catch (IndexOutOfBoundsException | NullPointerException e) {
                    Toast.makeText(ChatWindow.this, "Movie Name Cannot Be Empty!!", Toast.LENGTH_SHORT).show();

                }
            }
        }); //Suggest button

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_SETTLING:
                    case BottomSheetBehavior.STATE_COLLAPSED: {
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
//                Toast.makeText(ChatWindow.this, "SLIDING!!", Toast.LENGTH_SHORT).show();
            }
        });

        movieName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (x == 0) {

                    new parsing(getApplicationContext(), charSequence.toString(), 0, list).execute();
                }
                x = 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        hideSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
//        public boolean onOptionsItemSelected(MenuItem item) {
//            if (item.getItemId() == android.R.id.home) {
//                finish();
//            }
//            return super.onOptionsItemSelected(item);
//        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
//getActionBar().setDrawerListener(toggle);


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.group_info) {
            Intent i = new Intent(ChatWindow.this, GroupInfo.class);
            i.putExtra("Group Name", notgrpname);
            startActivity(i);
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}