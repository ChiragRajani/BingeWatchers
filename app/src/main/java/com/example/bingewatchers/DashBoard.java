package com.example.bingewatchers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class DashBoard extends AppCompatActivity {
    private static final String TAG = "DashBoard";
    private final ArrayList<String> mImageUrls = new ArrayList<>();
    FirebaseAuth mAuth;
    Button goToGroup;
    TextView user, list, viewEmail, viewUsername;
    FirebaseFirestore db;
    boolean doubleBackToExitPressedOnce = false;
    private ArrayList<String> mNames = new ArrayList<>();
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGroups(); // your code
                System.out.println("REEEFRESSSSHED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                pullToRefresh.setRefreshing(false);
            }
        });

        user = findViewById(R.id.user);
        setTitle("DashBoard");
        list = findViewById(R.id.list);
        goToGroup = findViewById(R.id.goToGroup);
        nv = findViewById(R.id.nv);
        dl = findViewById(R.id.activity_nav);
        View headerView = nv.getHeaderView(0);
        viewEmail = headerView.findViewById(R.id.email_id);
        viewUsername = headerView.findViewById(R.id.username);
        t = new ActionBarDrawerToggle(this, dl, R.string.drawer_open, R.string.drawer_close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user.setText("Hello User\n" + mAuth.getCurrentUser().getEmail());

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
                        Toast.makeText(DashBoard.this, "My Cart", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        startActivity(new Intent(DashBoard.this, MainActivity.class));
                        break;
                    }
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    public void getGroups() {
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        System.out.println("||||||||||||||| curent user is " + email);

        DocumentReference docRef = db.collection("Users").document(email);
        System.out.println("3453333333333333  " + docRef.toString());
        final Map<String, Object>[] messages = new Map[]{null};
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //if read successful

                    DocumentSnapshot document = task.getResult();

                    mNames = (ArrayList<String>) document.get("Groups");
                    System.out.println("888888888888888888888 " + mNames);
                    initRecyclerView1(mNames);

                    viewEmail.setText(document.get("Name").toString());

                    messages[0] = document.getData();


                } else {
                    System.out.println("error ");
                }
            }

        });

    }

    private void initRecyclerView1(ArrayList<String> mNamesv) {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        System.out.println("9999999999999999999999" + mNamesv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNamesv, mImageUrls);
        recyclerView.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

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

    private void getImages() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
//        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
//        mNames.add("Havasu Falls");
//
//        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
//        mNames.add("Trondheim");
//
//        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
//        mNames.add("Portugal");
//
//        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
//        mNames.add("Rocky Mountain National Park");
//
//
//        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
//        mNames.add("Mahahual");
//
//        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
//        mNames.add("Frozen Lake");
//
//
//        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
//        mNames.add("White Sands Desert");
//
//        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
//        mNames.add("Austrailia");
//
//        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
//        mNames.add("Washington");

//        initRecyclerView();

    }

}
