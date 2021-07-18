package com.polije.sipakost.aplikasikos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText signInEmail, signInPwd;
    FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    DatabaseReference db;
    private ProgressBar progressBar;
    private Button btSignUp, btSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_main);

        signInEmail = (EditText) findViewById(R.id.signInEmail);
        signInPwd = (EditText) findViewById(R.id.signInPwd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btSignUp = (Button) findViewById(R.id.btSignUp);
        btSignIn = (Button) findViewById(R.id.btSignIn);
        db = FirebaseDatabase.getInstance().getReference("dataKosts").child("user");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signInEmail.getText().toString();
                final String password = signInPwd.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_email), Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_pwd), Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
//                auth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                // If sign in fails, display a message to the user. If sign in succeeds
//                                // the auth state listener will be notified and logic to handle the
//                                // signed in user can be handled in the listener.
//                                progressBar.setVisibility(View.GONE);
//                                if (!task.isSuccessful()) {
//                                    // there was an error
//                                    if (password.length() < 6) {
//                                        signInPwd.setError(getString(R.string.minimum_password));
//                                    } else {
//                                        Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
//                                    }
//                                } else {
////                                    DataProfil dataProfil = new DataProfil();
////                                    if(dataProfil.getLevel().equals("Pencari Kos")){
//                                        startActivity(new Intent(MainActivity.this, HomePencariActivity.class));
//                                        finish();
//                                    }
////                                    authListener = new FirebaseAuth.AuthStateListener() {
////                                        @Override
////                                        public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
////                                            FirebaseUser user = firebaseAuth.getCurrentUser();
////                                            db.child(user.getUid()).child("level");
////
////
////
////
////                                        }
////                                    };
//
//                                }
//
//
//
//                        });

                cekLogin(email, password);

            }
        });

    }

    private void cekLogin(String email, String password) {
//        pDialog.setMessage("Logging in ...");
//        showDialog();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
//                            authListener = new FirebaseAuth.AuthStateListener(){
//                                @Override
//                                public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                                    db.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot user : dataSnapshot.getChildren()){
                                                DataProfil data = user.getValue(DataProfil.class);
                                                FirebaseUser u = auth.getCurrentUser();
                                                if(u.getUid().equals(u.getUid())){
                                                    String level = data.getLevel();
                                                    if (level.equals("Pemilik Kos")){
//                                                db.addUser(data.getName(), data.getEmail(), data.getPhone(),
//                                                        data.getGender(), data.getAddress(),u.getUid(), "", status);
                                                        Intent intent = new Intent(MainActivity.this,
                                                                HomeActivity.class);
                                                        //hideDialog();
                                                        startActivity(intent);
                                                        finish();
                                                    } else if(level.equals("Pencari Kos")){
//                                                db.addUser(data.getName(), data.getEmail(), data.getPhone(),
//                                                        data.getGender(), data.getAddress(),u.getUid(), "", status);
                                                        Intent intent = new Intent(MainActivity.this,
                                                                HomePencariActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }

                                    });
                                }else {
                            Toast.makeText(getApplicationContext(), "Email atau password salah", Toast.LENGTH_SHORT).show();
                        }
                            };
//                            session.setLogin(true);

                        });

                    }
    }

