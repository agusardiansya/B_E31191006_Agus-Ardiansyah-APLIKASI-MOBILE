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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class TambahProfilActivity extends AppCompatActivity {
    FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    DatabaseReference db;
    TextView textUserEmail;
    EditText inputNamaUser, inputNoTelp;
    Button btSimpanProfil;
    Spinner spinnerLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_profil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseDatabase.getInstance().getReference("dataKosts").child("user");
//        inputNamaUser = (EditText) findViewById(R.id.inputNamaUser);
//        inputNoTelp = (EditText) findViewById(R.id.inputNoTelp);
        spinnerLevel = (Spinner) findViewById(R.id.spinnerLevel);
        btSimpanProfil = (Button) findViewById(R.id.btSimpanProfil);

        auth = FirebaseAuth.getInstance();
        textUserEmail = (TextView) findViewById(R.id.textUserEmail);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // if user is null launch login activity
                    startActivity(new Intent(TambahProfilActivity.this, MainActivity.class));
                    finish();
                }else{

                    textUserEmail.setText(user.getEmail());

                    btSimpanProfil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            String nama = inputNamaUser.getText().toString().trim();
//                            String noTelp = inputNoTelp.getText().toString().trim();
                            String level = spinnerLevel.getSelectedItem().toString();
                            //String idUser = db.push().getKey();

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            DataProfil dataProfil = new DataProfil(level);

                            db.child(user.getUid()).setValue(dataProfil);

                            Toast.makeText(getApplicationContext(),"Data Profil Berhasil Disimpan",Toast.LENGTH_LONG).show();

                            if(level.equals("Pencari Kos")){
                                //db.child(user.getUid()).child("level").equalTo("Pencari Kos");
                                startActivity(new Intent(TambahProfilActivity.this, HomePencariActivity.class));
                                finish();
                            }else if(level.equals("Pemilik Kos")){
                                //db.child(user.getUid()).child("level").equalTo("Pemilik Kos");
                                startActivity(new Intent(TambahProfilActivity.this, HomeActivity.class));
                                finish();
                            }

                        }
                    });
                }
            }
        };
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
