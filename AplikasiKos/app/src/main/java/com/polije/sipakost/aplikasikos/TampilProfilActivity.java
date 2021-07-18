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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.polije.sipakost.aplikasikos.R.id.namaUser;
import static com.polije.sipakost.aplikasikos.R.id.telpUser;

public class TampilProfilActivity extends AppCompatActivity {
    EditText profilNama, profilEmail, profilNoTelp;
    TextView tvLevel, namaDetail, telpDetail;
    FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    DatabaseReference db;
    Button btUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_profil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseDatabase.getInstance().getReference("dataKosts");
        auth = FirebaseAuth.getInstance();

        profilNama = (EditText) findViewById(R.id.profilNama);
        profilEmail = (EditText) findViewById(R.id.profilEmail);
        profilNoTelp = (EditText) findViewById(R.id.profilNoTelp);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        namaDetail = (TextView) findViewById(R.id.namaDetail);
        telpDetail = (TextView) findViewById(R.id.telpDetail);
        btUpdate = (Button) findViewById(R.id.btUpdate);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // if user is null launch login activity
                    startActivity(new Intent(TampilProfilActivity.this, MainActivity.class));
                    finish();
                }else{
                    //profilEmail.setText(user.getEmail());
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            DataProfil dp = new DataProfil();
                           // String nama = dp.setNama(dataSnapshot.child("user").child(user.getUid()).getValue(DataProfil.class).getNama().toString());
                            //String noTelp = dp.setNoTelp(dataSnapshot.child("user").child(user.getUid()).getValue(DataProfil.class).getNoTelp().toString());
                            String level = dp.setLevel(dataSnapshot.child("user").child(user.getUid()).getValue(DataProfil.class).getLevel().toString());

                            profilEmail.setText(user.getEmail());
                            //profilNama.setText(nama);
                            //profilNoTelp.setText(noTelp);
                            tvLevel.setText(level);

//                            String idKos = db.getKey();
//                            DataKost dk = new DataKost();
//                            String namaUsr = dk.setNamaUser(dataSnapshot.child("detail").child("idKos").getValue(DataKost.class).getNamaUser().toString());
//                            String noTelpUsr = dk.setTelpUser(dataSnapshot.child("detail").child("idKos").getValue(DataKost.class).getTelpUser().toString());
//                            namaDetail.setText(namaUsr);
//                            telpDetail.setText(noTelpUsr);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    btUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String nama = profilNama.getText().toString().trim();
                            String noTelp = profilNoTelp.getText().toString().trim();
                            String level = tvLevel.getText().toString().trim();
                            //String idUser = db.push().getKey();
                            //DataProfil dataProfil = new DataProfil(nama,level,noTelp);
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //db.child("user").child(user.getUid()).setValue(dataProfil);

                            Toast.makeText(getApplicationContext(),"Data Profil Berhasil Diupdate",Toast.LENGTH_LONG).show();
                            profilNama.setText(nama);
                            profilNoTelp.setText(noTelp);
                            tvLevel.setText(level);
//
//                            Query nm = db.child("detail").child(idKos).equalTo("namaUser");
//                            db.child("detail").child(idKos).equalTo("telpUser");

                        }
                    });
                }
            }
        };

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
            case android.R.id.home:
                finish();
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
