package com.polije.sipakost.aplikasikos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePencariActivity extends AppCompatActivity {
    TextView textEmail;
    DatabaseReference db;
    TextView textLevel;
    FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    ImageButton btCariKos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pencari);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        textEmail = (TextView) findViewById(R.id.textEmail);
        textLevel = (TextView) findViewById(R.id.textLevel);
        db = FirebaseDatabase.getInstance().getReference("dataKosts").child("user");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // if user is null launch login activity
                    startActivity(new Intent(HomePencariActivity.this, MainActivity.class));
                    finish();
                }else{
                    textEmail.setText(user.getEmail());
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            DataProfil dataProfil = new DataProfil();
                            String level = dataProfil.setLevel(dataSnapshot.child(user.getUid()).getValue(DataProfil.class).getLevel().toString());

                            textLevel.setText(level);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        };

        btCariKos = (ImageButton) findViewById(R.id.btCariKos);
        btCariKos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePencariActivity.this, TampilKosActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

}
