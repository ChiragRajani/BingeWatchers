package com.example.bingewatchers;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    private EditText email, pwd;
    private FirebaseFirestore db;
    private Button btn;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 9001;
    private TextView reg;
    private FirebaseAuth mAuth;
    ViewGroup progressView, viewGroup;
    View v;
    ProgressBar mProgressBar;
    boolean isProgressShowing = false;
    TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        email = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        warning = findViewById(R.id.textView6);
        btn = findViewById(R.id.submit);
        reg = findViewById(R.id.reg);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        v = this.findViewById(android.R.id.content).getRootView();
        viewGroup = (ViewGroup) v;
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn1();
                        break;
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email1 = email.getText().toString();
                String pwd1 = pwd.getText().toString();
                showProgressingView();
                signIn(email1, pwd1);


            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressingView();
//                Intent i = new Intent(MainActivity.this, GenreSelection.class);
                Intent i = new Intent(MainActivity.this, SignUp.class);

                startActivity(i);
            }
        });
    }

    private void signIn1() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            System.out.println(account.getEmail() + " " + account.getDisplayName());

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    void updateUserinDB(Map userinfo, String regEmail) {
        try {
            db.collection("Users")
                    .document(regEmail)
                    .set(userinfo);

            db.collection("Groups").document("Default").update("Members", FieldValue.arrayUnion(regEmail));
            db.collection("Users").document(regEmail).update("Groups", FieldValue.arrayUnion("Default"));


        } catch (@NonNull Exception e) {
            Log.w(TAG, "Error adding document", e);


        }
        Log.d(TAG, "===========DocumentSnapshot added with ID: ");

    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // [START_EXCLUDE silent]
//        showProgressBar();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Map<String, Object> userM = new HashMap<>();
                            userM.put("Username", user.getEmail());
                            userM.put("password", "Signed By google");
                            userM.put("Date of Birth", "not provided");
                            userM.put("Name", user.getDisplayName());

                            updateUserinDB(userM, user.getEmail());
                            check();

                            Intent i = new Intent(MainActivity.this, DashBoard.class);
                            hideProgressingView();
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            hideProgressingView();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            warning.setText(task.getException().getMessage() + "/nPlease try again.");

                        }


                    }
                });
    }

    private void signIn(String email1, String password) {
        mAuth.signInWithEmailAndPassword(email1, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if (mAuth.getCurrentUser() != null) {
                                Intent i = new Intent(MainActivity.this, DashBoard.class);
                                hideProgressingView();
                                startActivity(i);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            hideProgressingView();
                            warning.setText(task.getException().getMessage() + "/nPlease try again.");

                        }
                    }
                });
    }

    public void showProgressingView() {
        if (!isProgressShowing) {
            isProgressShowing = true;
            progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.circle_progressbar, null);
            viewGroup.addView(progressView);
            mProgressBar = progressView.findViewById(R.id.progressBar1);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    void check() {


        DocumentReference docIdRef = db.collection("Users").document(mAuth.getCurrentUser().getEmail());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Group Chats").child("Default");
                        Message obj = new Message(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getDisplayName() + " just Joined the group", Calendar.getInstance().getTime().toString(), mAuth.getCurrentUser().getEmail(), "activity");
                        myRef.push().setValue(obj);

                    }
                }
            }
        });
    }

}