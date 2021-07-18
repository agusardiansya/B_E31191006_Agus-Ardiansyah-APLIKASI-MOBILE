package com.polije.sipakost.aplikasikos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.polije.sipakost.aplikasikos.TwoFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class TambahActivity extends AppCompatActivity {
    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";
    // Root Database Name for Firebase Database.
    //String Database_Path = "All_Image_Uploads_Database";
    Button btPilihCover, btSubmit, btPicker;
    ImageView imageCover;
    EditText inputNamaKos, inputJml, inputHargaPerBulan,
            inputMinBayar, inputLuasKamar, fasilitasKamar, fasilitasKamarMandi, fasilitasUmum, inputAlamat, namaUser, telpUser;
    TextView tvAddress;
    Spinner spinner;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    List<DataKost> dataKosts;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;

    int PLACE_PICKER_REQUEST = 1;
    String lat,lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("dataKosts");
        dataKosts = new ArrayList<>();

        btPilihCover = (Button) findViewById(R.id.btPilihCover);
        btPilihCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });

        btPicker = (Button) findViewById(R.id.btPicker);
        btPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(TambahActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        imageCover = (ImageView) findViewById(R.id.imageCover);

        inputNamaKos = (EditText) findViewById(R.id.inputNamaKos);
        inputJml = (EditText) findViewById(R.id.inputJml);
        inputHargaPerBulan = (EditText) findViewById(R.id.inputHargaPerBulan);
        inputMinBayar = (EditText) findViewById(R.id.inputMinBayar);
        inputLuasKamar = (EditText) findViewById(R.id.inputLuasKamar);
        inputAlamat = (EditText) findViewById(R.id.inputAlamat);
        fasilitasKamar = (EditText) findViewById(R.id.fasilitasKamar);
        fasilitasKamarMandi = (EditText) findViewById(R.id.fasilitasKamarMandi);
        fasilitasUmum = (EditText) findViewById(R.id.fasilitasUmum);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        namaUser = (EditText) findViewById(R.id.namaUser);
        telpUser = (EditText) findViewById(R.id.telpUser);
        spinner = (Spinner) findViewById(R.id.spinner);
        btSubmit = (Button) findViewById(R.id.btSubmit);

//        auth = FirebaseAuth.getInstance();
//        authListener = new FirebaseAuth.AuthStateListener(){
//
//            @Override
//            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user == null) {
//                    // if user is null launch login activity
//                    startActivity(new Intent(TambahActivity.this, MainActivity.class));
//                    finish();
//                }else {
//                    //namaUser.setText(user.getEmail());
//                    //DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("dataKosts");
//                    databaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            DataProfil dp = new DataProfil();
//                            String nama = dp.setNama(dataSnapshot.child("user").child(user.getUid()).getValue(DataProfil.class).getNama().toString());
//                            String noTelp = dp.setNoTelp(dataSnapshot.child("user").child(user.getUid()).getValue(DataProfil.class).getNoTelp().toString());
//                            namaUser.setText(nama);
//                            telpUser.setText(noTelp);
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//            }
//        };

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Calling method to upload selected image on Firebase storage.
                InsertDataKost();


            }

        });

        progressDialog = new ProgressDialog(TambahActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            Place place = PlacePicker.getPlace(TambahActivity.this, data);
            inputAlamat.setText(place.getAddress());
            tvAddress.setText(place.getLatLng().toString());
            String latLang=tvAddress.getText().toString().trim();

            int index, index2,index3;
            index = latLang.indexOf("(") + 1;
            index2= latLang.indexOf(",");
            index3= latLang.indexOf(")");

            lat = latLang.substring(index, index2);
            lang = latLang.substring(index2 + 1, index3);

//                tvAddress.setText(lat);
//                tvAddress2.setText(lang);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                imageCover.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                btPilihCover.setText("Gambar Terpilih");

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void InsertDataKost() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Input Data Kost...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String namaKos = inputNamaKos.getText().toString().trim();
                            Integer jml = Integer.parseInt(inputJml.getText().toString().trim());
                            Integer hargaPerBulan = Integer.parseInt(inputHargaPerBulan.getText().toString().trim());
                            Integer minBayar = Integer.parseInt(inputMinBayar.getText().toString().trim());
                            String jenisKos = spinner.getSelectedItem().toString();
                            String luasKamar = inputLuasKamar.getText().toString().trim();
                            String fasKamar = fasilitasKamar.getText().toString().trim();
                            String fasKamarMandi = fasilitasKamarMandi.getText().toString().trim();
                            String fasUmum = fasilitasUmum.getText().toString().trim();
                            String alamat = inputAlamat.getText().toString().trim();
                            double latitude = Double.parseDouble(lat);
                            double longitude = Double.parseDouble(lang);
                            String nm = namaUser.getText().toString().trim();
                            String tlp = telpUser.getText().toString().trim();

                            if (!TextUtils.isEmpty(namaKos)) {
                                // Getting image upload ID.
                                String idKos = databaseReference.push().getKey();

                                @SuppressWarnings("VisibleForTests")
                                DataKost dk = new DataKost(idKos, namaKos, jenisKos, taskSnapshot.getDownloadUrl().toString(),
                                        luasKamar, alamat, fasKamar, fasKamarMandi, fasUmum, nm, tlp, latitude, longitude, jml, hargaPerBulan, minBayar);

                                // Adding image upload id s child element into databaseReference.
                                databaseReference.child("detail").child(idKos).setValue(dk);

                                inputNamaKos.setText("");
                                inputJml.setText("");
                                inputHargaPerBulan.setText("");
                                inputMinBayar.setText("");
                                inputLuasKamar.setText("");
                                inputAlamat.setText("");
                                namaUser.setText("");
                                telpUser.setText("");
                                fasilitasKamar.setText("");
                                fasilitasKamarMandi.setText("");
                                fasilitasUmum.setText("");

                                // Hiding the progressDialog after done uploading.
                                progressDialog.dismiss();

                                // Showing toast message after done uploading.
                                Toast.makeText(getApplicationContext(), "Input Data Kost Berhasil", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Data Kost Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(TambahActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Input Data Kost...");

                        }
                    });
        } else {

            Toast.makeText(TambahActivity.this, "Gambar dan Data Kost Tidak Boleh Kosong", Toast.LENGTH_LONG).show();

        }
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
        switch (id) {
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

}
