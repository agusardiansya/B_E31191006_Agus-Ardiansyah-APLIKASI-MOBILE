package com.polije.sipakost.aplikasikos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailKosActivity extends AppCompatActivity {
    private String mPost_Key = null;
    TextView jenisKosDetail, jmlDetail, namaKosDetail, hargaDetail, minBayarDetail, almt, kamar, luas, kamarMandi, umum, namaPemilik, telpPemilik;
    ImageView imageDetail;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseDatabase.getInstance().getReference("dataKosts").child("detail");

        mPost_Key = getIntent().getExtras().getString("idKos");
        namaKosDetail = (TextView) findViewById(R.id.namaKosDetail);
        jenisKosDetail = (TextView) findViewById(R.id.jenisKosDetail);
        jmlDetail = (TextView) findViewById(R.id.jmlDetail);
        hargaDetail = (TextView) findViewById(R.id.hargaDetail);
        minBayarDetail = (TextView) findViewById(R.id.minBayarDetail);
        almt = (TextView) findViewById(R.id.almt);
        kamar = (TextView) findViewById(R.id.kamar);
        luas = (TextView) findViewById(R.id.luas);
        kamarMandi = (TextView) findViewById(R.id.kamarMandi);
        umum = (TextView) findViewById(R.id.umum);
        namaPemilik = (TextView) findViewById(R.id.namaPemilik);
        telpPemilik = (TextView) findViewById(R.id.telpPemilik);
        imageDetail = (ImageView) findViewById(R.id.imageDetail);

        db.child(mPost_Key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nama = (String) dataSnapshot.child("namaKos").getValue();
                String jenis = (String) dataSnapshot.child("jenisKos").getValue();
                String jml = (String) dataSnapshot.child("jml").getValue().toString();
                String harga = (String) dataSnapshot.child("hargaPerBulan").getValue().toString();
                String bayar = (String) dataSnapshot.child("minBayar").getValue().toString();
                String alamat = dataSnapshot.child("alamat").getValue().toString();
                String fasKamar = dataSnapshot.child("fasKamar").getValue().toString();
                String fasKamarMandi = dataSnapshot.child("fasKamarMandi").getValue().toString();
                String fasUmum = dataSnapshot.child("fasUmum").getValue().toString();
                String namaUser = dataSnapshot.child("namaUser").getValue().toString();
                String luasKamar = dataSnapshot.child("luasKamar").getValue().toString();
                String telpUser = dataSnapshot.child("telpUser").getValue().toString();
                String image = (String) dataSnapshot.child("imageURL").getValue();

                namaKosDetail.setText(nama);
                jenisKosDetail.setText(jenis);
                jmlDetail.setText("Ada "+ jml + " Kamar");
                hargaDetail.setText("Harga : "+harga + " /Bulan");
                minBayarDetail.setText("Minimal Bayar : "+bayar + " Bulan");
                almt.setText(alamat);
                kamar.setText(fasKamar);
                kamarMandi.setText(fasKamarMandi);
                umum.setText(fasUmum);
                namaPemilik.setText("Nama : "+namaUser);
                telpPemilik.setText("No. Telp : "+telpUser);
                luas.setText("Luas Kamar : "+luasKamar);

                Picasso.with(DetailKosActivity.this).load(image).into(imageDetail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
