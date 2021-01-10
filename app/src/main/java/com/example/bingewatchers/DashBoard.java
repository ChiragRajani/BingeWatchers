package com.example.bingewatchers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoard extends AppCompatActivity {
    private static final String TAG = "DashBoard";
    static SwipeRefreshLayout.OnRefreshListener refreshListener;
    FirebaseAuth mAuth;
    Button goToGroup, suggest;
    TextView user, viewEmail, viewUsername, movieName, notif_status, hideSheet;
    EditText movieReview1;
    FirebaseFirestore db;
    Switch inform;
    FirebaseFirestore rootRef;
    ListView list;
    java.util.ArrayList<Movie> he = new java.util.ArrayList<>();
    SwipeRefreshLayout pullToRefresh;
    boolean doubleBackToExitPressedOnce = false;
    int SWITCH_CHECKED_STATUS = 1; // 1 if it is checked and 0 if its not
    String name;
    ArrayList<String> genres = new ArrayList<>();
    CircleImageView dp_view;
    View headerView;
    DatabaseReference myRef;
    int i1 = 0, x = 0;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout bottom_sheet;
    static RecyclerView shimmerRecycler,groupRecycler;

    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    RecyclerViewAdapter adapter1;
    static Recommendation_Adapter adapter12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        setTitle("DashBoard");


        suggest = findViewById(R.id.button);
        rootRef = FirebaseFirestore.getInstance();
        inform = findViewById(R.id.notif_info);
        list = findViewById(R.id.listview);
        notif_status = findViewById(R.id.notif_status);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        shimmerRecycler = findViewById(R.id.recyclerView);
        groupRecycler = findViewById(R.id.recc_recycler);
        user = findViewById(R.id.user);

        bottom_sheet = findViewById(R.id.watched_movie);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);


        list = findViewById(R.id.listview);
        goToGroup = findViewById(R.id.goToGroup);
        nv = findViewById(R.id.nv);
        dl = findViewById(R.id.activity_nav);
        headerView = nv.getHeaderView(0);
        hideSheet = findViewById(R.id.hideSheet);
        viewEmail = headerView.findViewById(R.id.email_id);
        myRef = FirebaseDatabase.getInstance().getReference("Group Chats");
        dp_view = headerView.findViewById(R.id.dp_view);
        viewUsername = headerView.findViewById(R.id.username);

        movieReview1 = (EditText) findViewById(R.id.movieReview);
        movieName = findViewById(R.id.movieName);
        t = new ActionBarDrawerToggle(this, dl, R.string.drawer_open, R.string.drawer_close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        inform.setChecked(true);
        notif_status.setText("Notification will be sent to everyone");

        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNames = new ArrayList<>();
                mImageUrls = new ArrayList<>();
                getGroups();
                System.out.println("Refresh");
                // your code
            }
        };
        pullToRefresh.setOnRefreshListener(refreshListener);

        getGroups();

        viewUsername.setText(mAuth.getCurrentUser().getEmail());


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.profile:
                        Toast.makeText(DashBoard.this, "My Account", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.groups: {
                        Toast.makeText(DashBoard.this, "Settings", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DashBoard.this, CreateJoinGroup.class));
                        break;
                    }
                    case R.id.logout: {
                        Toast.makeText(DashBoard.this, "Logout", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        Intent i = new Intent(DashBoard.this, MainActivity.class);
                        i.putExtra("from", "logout");
                        startActivity(i);
                    }
                    default:
                        return true;
                }
                return true;
            }
        });

        inform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    SWITCH_CHECKED_STATUS = 1;
                    notif_status.setText("Tell in every group also");
                } else {
                    SWITCH_CHECKED_STATUS = 0;
                    notif_status.setText("Just add into your profile");
                }

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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                x = 1;
                i1 = i;
                he = parsing.he1;
                Movie y = he.get(i);
                String mvieName = y.getMovieName();
                movieName.setText(mvieName);
                list.setAdapter(null);
            }
        });

        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SWITCH_CHECKED_STATUS == 1) {
                    for (String groupName : mNames) {
                        try {

                            Movie y = he.get(i1);
                            String mvieName = y.getMovieName();

                            String desc = mvieName + "(" + y.getMovieDate().substring(0, 4) + ")\n\n"
                                    + y.getDescription() + "\n\n" + name + "'s Review:" + movieReview1.getText().toString();

                            Message obj = new Message(name, desc, Calendar.getInstance().getTime().toString(),
                                    mAuth.getCurrentUser().getEmail(), "https://image.tmdb.org/t/p/w500" + y.getPoster(),
                                    "Suggestion");

                            myRef.child(groupName).push().setValue(obj);


                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            Toast.makeText(DashBoard.this, "Message Sent to all groups", Toast.LENGTH_SHORT).show();

                        } catch (IndexOutOfBoundsException | NullPointerException e) {
                            Toast.makeText(DashBoard.this, "Movie Name Cannot Be Empty!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                    movieReview1.setText("");
                    movieName.setText("");
                }
                rootRef.collection("Users").document(mAuth.getCurrentUser().getEmail()).update("Movies Watched", FieldValue.arrayUnion(movieName.getText().toString()));

            }
        });

        hideSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }


    private void initRecyclerView1(ArrayList<String> mNames1, ArrayList<String> mImageUrls1, String name) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        shimmerRecycler.setLayoutManager(layoutManager);
        adapter1 = new RecyclerViewAdapter(this, mNames1, mImageUrls1, name);
        shimmerRecycler.setAdapter(adapter1);

    } //For group displays

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getGroups() {
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();


        DocumentReference docRef = db.collection("Users").document(email);

        final Map<String, Object>[] messages = new Map[]{null};
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //if read successful

                    DocumentSnapshot document = task.getResult();
                    mNames = (ArrayList<String>) document.get("Groups");
                    genres = (ArrayList<String>) document.get("Genres");

                    Map kv1 = new HashImages("s").getHash2();

                    for (String i : genres) {

                        try {
                            new GenreSearch(kv1.get(i).toString(), getApplicationContext()).execute();
                        } catch (NullPointerException e) {

                        }
                    }


                    pullToRefresh.setRefreshing(false);
                    name = document.get("Name").toString();


                    viewEmail.setText(name);
                    user.setText("Welcome " + name + "! Your groups here,");

//                    https://picsum.photos/
                    Glide.with(DashBoard.this).asDrawable()
                            .load("https://ui-avatars.com/api/background=random?name=" + name)
                            .into(dp_view);


                    messages[0] = document.getData();
                    Map kv = new HashImages().getHash1();
                    if (mNames != null) {
                        for (String i : mNames) {
                            String o = i.substring(0, 1);
                            mImageUrls.add(kv.get(o).toString());
                        }
                        initRecyclerView1(mNames, mImageUrls, name);
                    }

                }
            }

        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (t.onOptionsItemSelected(item))
            return true;


        if (id == R.id.add_button) {

            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                show.setText("Close sheet");
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                show.setText("Expand sheet");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
