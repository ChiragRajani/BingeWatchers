package com.example.bingewatchers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoard extends AppCompatActivity {
    private static final String TAG = "DashBoard";
    static protected boolean isCardShowing = false, isProgressShowing = false;
    static SwipeRefreshLayout.OnRefreshListener refreshListener;
    static RecyclerView shimmerRecycler, groupRecycler;
    static Recommendation_Adapter adapter12;
    FirebaseAuth mAuth;
    Button goToGroup, suggest;
    TextView user, viewEmail, viewUsername, movieName, notif_status, hideSheet, rc;
    EditText movieReview1;
    FirebaseFirestore db;
    ViewGroup progressView, CardView;
    View v;
    ViewGroup viewGroup, viewCard;
    ImageView mProgressBar;
    AnimationDrawable animationDrawable;
    SwitchMaterial inform;
    FirebaseFirestore rootRef;
    ListView list;
    java.util.ArrayList<Movie> he = new java.util.ArrayList<>();
    ArrayList<HashMap<String, Object>> sugg = new ArrayList<>();
    SwipeRefreshLayout pullToRefresh;
    boolean doubleBackToExitPressedOnce = false;
    int SWITCH_CHECKED_STATUS = 1; // 1 if it is checked and 0 if its not
    String name;
    ArrayList<String> genres = new ArrayList<>();
    CircleImageView dp_view;
    View headerView;
    DatabaseReference myRef;
    int i1 = 0, x = 0, u = 0;

    TextView mvName, suggestedBy, suggestedOn, rating;
    ImageView mvPoster;
    Button hide, showAnother;
    LinearLayout suggestionLayout;
    RecyclerViewAdapter adapter1;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout bottom_sheet;
    // ----------------------------------------------------
    private SensorManager mSensorManager;
    Vibrator vib;
    //----------------------------------------------------
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    //------------------------------------------------
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if (mAccel > 20) {

                // Vibrate for 500 milliseconds
//                vib.vibrate(200);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vib.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.EFFECT_HEAVY_CLICK));
                } else {
                    vib.vibrate(200);
                }
                showSuggestionCard();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        setTitle("DashBoard");

        vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        suggest = findViewById(R.id.button);
        rootRef = FirebaseFirestore.getInstance();
        inform = findViewById(R.id.notif_info);
        list = findViewById(R.id.listview);
        notif_status = findViewById(R.id.notif_status);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        shimmerRecycler = findViewById(R.id.recyclerView);
        groupRecycler = findViewById(R.id.recc_recycler);
        user = findViewById(R.id.user);
        rc = findViewById(R.id.textView3);
        v = this.findViewById(android.R.id.content).getRootView();
        viewGroup = (ViewGroup) v;
        viewCard = (ViewGroup) v;
        bottom_sheet = findViewById(R.id.watched_movie);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        suggestionLayout = findViewById(R.id.suggentionLayout);
        showProgressingView();
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

        //----------------------------------------------------
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        //----------------------------------------------------


        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNames = new ArrayList<>();
                mImageUrls = new ArrayList<>();

                System.out.println("Refresh");
                
                getGroups();
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
                    case R.id.profile: {
                        startActivity(new Intent(DashBoard.this, Activity_profile.class));
                        Toast.makeText(DashBoard.this, "My Account", Toast.LENGTH_SHORT).show();
                        break;
                    }
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
                        break;
                    }
                    case R.id.updateGenere: {
                        Intent i = new Intent(DashBoard.this, GenreSelection.class);
                        i.putExtra("from", "Dashboard");
                        startActivity(i);
                        Toast.makeText(DashBoard.this, "My Account", Toast.LENGTH_SHORT).show();
                        break;
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
                                    + y.getDescription() + "\n\n" + name + "'s Review:" + movieReview1.getText().toString() + "\n";

                            Message obj = new Message(name, desc, Calendar.getInstance().getTime().toString(),
                                    mAuth.getCurrentUser().getEmail(), y.getPoster(),
                                    "Suggestion");

                            myRef.child(groupName).push().setValue(obj);


                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            Toast.makeText(DashBoard.this, "Message Sent to all groups", Toast.LENGTH_SHORT).show();

                            for (String i1 : mNames) {
                                DocumentReference docRef = db.collection("Groups").document(i1);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document = task.getResult();
                                        List<String> members = (List<String>) document.get("Members");

                                    for (String i : members) {
                                            if (!i.equals(mAuth.getCurrentUser().getEmail()))
                                                db.collection("Users").document(i).update("Suggestion", FieldValue.arrayUnion(new Suggestion(y, mAuth.getCurrentUser().getEmail(), Calendar.getInstance().getTime().toString(), i1)));

                                        }

                                    }
                                });
                            }

                        } catch (IndexOutOfBoundsException | NullPointerException  e) {
                            Toast.makeText(DashBoard.this, "Movie Name Cannot Be Empty!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                    movieReview1.setText("");
                    movieName.setText("");
                    DashBoard.refreshListener.onRefresh();
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
//        hideProgressingView();


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
                    try {
                        DocumentSnapshot document = task.getResult();
                        mNames = (ArrayList<String>) document.get("Groups");
                        genres = (ArrayList<String>) document.get("Genres");
                        sugg = (ArrayList<HashMap<String, Object>>) document.get("Suggestion");

                        Map kv1 = new HashImages("s").getHash2();

                        if (genres == null) {
                            String url = "https://api.themoviedb.org/3/trending/movie/week?api_key=1c9e495395d2ed861f2ace128f6af0e2";
                            new GenreSearch(getApplicationContext(), url).execute();
                        } else {
                            for (String i : genres) {
                                String url = "https://api.themoviedb.org/3/discover/movie?api_key=1c9e495395d2ed861f2ace128f6af0e2&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres="
                                        + kv1.get(i).toString() + "&with_original_language=hi";
                                new GenreSearch(getApplicationContext(), url).execute();

                            }
                        }
                        pullToRefresh.setRefreshing(false);
                        // pullToRefresh.setRefreshing(false);


                        name = document.get("Name").toString();


                        viewEmail.setText(name);
                        user.setText("Welcome " + name + "! Your groups here,");
                        rc.setText("Hand picked recommendations for ya");
                        //suggestionLayout.setBackgroundResource(R.drawable.dashboard_card);
//
                       if(mAuth.getCurrentUser().getPhotoUrl() ==null) {
                           Glide.with(DashBoard.this).asDrawable()
                                   .load("https://ui-avatars.com/api/background=random?name=" + name)
                                   .into(dp_view);
                       }
                       else{
                           Glide.with(DashBoard.this).asDrawable()
                                   .load(mAuth.getCurrentUser().getPhotoUrl().toString())
                                   .into(dp_view);
                       }

                        messages[0] = document.getData();
                        Map kv = new HashImages().getHash1();


                        if (mNames != null) {
                            for (String i : mNames) {
                                String o = i.substring(0, 1).toLowerCase();
                                mImageUrls.add(kv.get(o).toString());
                            }

                            initRecyclerView1(mNames, mImageUrls, name);

                        }
                        hideProgressingView();


                    } catch (NullPointerException e) {
                        hideProgressingView();
                        pullToRefresh.setRefreshing(false);
//                        System.out.println("|||||||||||||||||||||||"+kv.get(o).toString());
                        e.printStackTrace();
                    }
                }

            }

        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (t.onOptionsItemSelected(item))
            return true;
        if (id == R.id.sugg_button) {
            if (!isCardShowing) {
                showSuggestionCard();
            } else {
                hideSuggestionCard();
            }
        }

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
        if(isCardShowing==true)
            hideSuggestionCard();
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                show.setText("Close sheet");
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

    public void showProgressingView() {
        if (!isProgressShowing) {
            isProgressShowing = true;
            progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);
            viewGroup.addView(progressView);
            mProgressBar = progressView.findViewById(R.id.loadingif);
            mProgressBar.setBackgroundResource(R.drawable.popcorrn_loading);
            animationDrawable = (AnimationDrawable) mProgressBar.getBackground();
            mProgressBar.setVisibility(View.VISIBLE);
            animationDrawable.start();
        }
    }

    public void showSuggestionCard() {

        if (!isCardShowing) {


            CardView = (ViewGroup) getLayoutInflater().inflate(R.layout.random_suggest, null);
            viewGroup.addView(CardView);
            mvName = CardView.findViewById(R.id.mvName);
            suggestedBy = CardView.findViewById(R.id.sugstBy);
            rating = CardView.findViewById(R.id.rating);
            suggestedOn = CardView.findViewById(R.id.suggestedOn);
            mvPoster = CardView.findViewById(R.id.mvPoster);
            hide = CardView.findViewById(R.id.hide);
            showAnother = CardView.findViewById(R.id.show1);
            isCardShowing = true;

            try {
                Collections.shuffle(sugg);
                HashMap<String, Object> i = (HashMap<String, Object>) sugg.get(u);
                HashMap<String, String> i1 = (HashMap<String, String>) i.get("y");

                Glide.with(getApplicationContext()).asDrawable()
                        .load(i1.get("poster"))
                        .into(mvPoster);
                rating.setText("IMDB rating " + i1.get("rating"));
                mvName.setText(i1.get("movieName"));
                suggestedBy.setText(i.get("sender").toString() + " (" + i.get("grpname") + ")");
                suggestedOn.setText("On " + i.get("date").toString().substring(0, 10));

                u++;
                u = u % sugg.size();
                showAnother.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            HashMap<String, Object> i2 = (HashMap<String, Object>) sugg.get(u);
//                            String i2 = sugg.get(u);
                            HashMap<String, String> i12 = (HashMap<String, String>) i2.get("y");
                            System.out.println(i12.get("movieName"));
                            System.out.println("------------------------" + sugg.size() + " " + u);
                            Toast.makeText(getApplicationContext(), "Showing" + u + "/" + i2.size(), Toast.LENGTH_SHORT);
                            u++;
                            u = u % sugg.size();
                            Glide.with(getApplicationContext()).asDrawable()
                                    .load(i12.get("poster"))
                                    .into(mvPoster);
                            rating.setText("IMDB rating " + i12.get("rating"));
                            mvName.setText(i12.get("movieName"));
                            suggestedBy.setText(i2.get("sender").toString() + " (" + i2.get("grpname") + ")");
                            suggestedOn.setText("On " + i2.get("date").toString().substring(0, 10));


                        } catch (NullPointerException | ClassCastException e) {
                            e.printStackTrace();
                            System.out.println("333333333333333333333333333 " + e.getMessage() + "size is   " + sugg.size() + "   u is    " + u);
                        }
                    }
                });

            } catch (Exception e) {

                mvName.setText("Your friends suggested no movies.");
                rating.setText("Do you even have friends?");
                suggestedOn.setText("");
                suggestedBy.setText("");
                showAnother.setEnabled(false);

                Glide.with(getApplicationContext()).asDrawable()
                        .load("https://images.pond5.com/sad-lonely-man-party-hat-footage-087122933_iconl.jpeg")
                        .into(mvPoster);

            }

            hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    u = 0;
                    hideSuggestionCard();
                }
            });

        }

    }

    public void hideSuggestionCard() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(CardView);
        isCardShowing = false;

    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
        mProgressBar.setVisibility(View.GONE);
        animationDrawable.stop();
    }

    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
    //------------------------------------------------
}
