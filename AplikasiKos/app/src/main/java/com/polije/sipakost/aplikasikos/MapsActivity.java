package com.polije.sipakost.aplikasikos;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener {

    GoogleMap mMap;
    DatabaseReference db;
    String mPost_Key = null;
    ChildEventListener mChildEventListener;

    public MapsActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = FirebaseDatabase.getInstance().getReference("dataKosts").child("detail");

        if (mapFragment != null) {
            mapFragment.onCreate(null);
            mapFragment.onResume();
            mapFragment.getMapAsync(this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //db = FirebaseDatabase.getInstance().getReference("dataKosts").child("detail");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    DataKost kostAlamat = s.getValue(DataKost.class);
                    LatLng location = new LatLng(kostAlamat.latitude, kostAlamat.longitude);
                    mMap.addMarker(new MarkerOptions().position(location).title(Integer.toString(kostAlamat.getHargaPerBulan()))).
                            setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13.0f));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if ((ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MapsActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(true);


        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}