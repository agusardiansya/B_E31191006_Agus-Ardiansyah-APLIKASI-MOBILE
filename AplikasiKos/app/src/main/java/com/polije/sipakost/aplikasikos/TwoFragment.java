package com.polije.sipakost.aplikasikos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.polije.sipakost.aplikasikos.R.attr.icon;
import static com.polije.sipakost.aplikasikos.R.drawable.circle;
import static com.polije.sipakost.aplikasikos.R.drawable.map;

//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link TwoFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link TwoFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class TwoFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    MapView mMapView;
    GoogleMap mMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private  int PROXIMITY_RADIUS = 10000;
    DatabaseReference db;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public TwoFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment TwoFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    public static TwoFragment newInstance(String param1, String param2) {
//        TwoFragment fragment = new TwoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_two, container,
                false);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

//        if (!CheckGooglePlayServices()) {
//            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
//        } else {
//            Log.d("onCreate","Google Play Services available.");
//        }

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                //optional. menampilkan default map
                LatLng jkt = new LatLng(-6.186486, 106.834091);
                mMap.addMarker(new MarkerOptions().position(jkt).title("Marker in Jakarta"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jkt,14.0f));

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);
                } else {
                    buildGoogleApiClient();

                    mMap.setMyLocationEnabled(true);

                }

                db = FirebaseDatabase.getInstance().getReference("dataKosts").child("detail");
                db.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(final DataSnapshot dataSnapshot, String s) {

                        LatLng lt = new LatLng(
                                dataSnapshot.child("latitude").getValue(Double.class),
                                dataSnapshot.child("longitude").getValue(Double.class)
                        );
                        int hargaPerBulan = dataSnapshot.child("hargaPerBulan").getValue(int.class);
                        mMap.addMarker(new MarkerOptions().position(lt).title(""+hargaPerBulan)).showInfoWindow();


                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                String post_key = dataSnapshot.getRef().getKey();
                                Intent i = new Intent(getContext(), DetailKosActivity.class);
                                i.putExtra("idKos",post_key);
                                startActivity(i);
                                return true;
                            }
                        });
                    }

                    @Override
                    public void onChildChanged(final DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//                btCariKosMap = (Button) v.findViewById(R.id.btCariKosMap);
//                btCariKosMap.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
            }
        });

//        btCariKosMap = (Button) v.findViewById(R.id.btCariKosMap);
//        db = FirebaseDatabase.getInstance().getReference("dataKosts").child("detail");


//                String url = getUrl(latitude, longitude, db);
//                Object[] DataTransfer = new Object[2];
//                DataTransfer[0] = mMap;
//                DataTransfer[1] = url;
//                Log.d("onClick", url);
//                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
//                getNearbyPlacesData.execute(DataTransfer);

        // Inflate the layout for this fragment
        return v;
    }

//    private boolean CheckGooglePlayServices() {
//        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
//        int result = googleAPI.isGooglePlayServicesAvailable(getContext());
//        if(result != ConnectionResult.SUCCESS) {
//            if(googleAPI.isUserResolvableError(result)) {
//                googleAPI.getErrorDialog((Activity) getContext(), result, 0).show();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    private String getUrl(double latitude, double longitude, DatabaseReference nearbyPlace) {
//        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//        googlePlacesUrl.append("location=" + latitude + "," + longitude);
//        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
//        googlePlacesUrl.append("&type=" + nearbyPlace);
//        googlePlacesUrl.append("&sensor=true");
//        googlePlacesUrl.append("&key=" + "AIzaSyA_Ztp3FAWABPd1cDLlv_Q1T2CNCCBei2w");
//        Log.d("getUrl", googlePlacesUrl.toString());
//        return (googlePlacesUrl.toString());
//    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Memberikan marker pada current location
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

//        Toast.makeText(getContext(),"Your Current Location", Toast.LENGTH_LONG).show();
//        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,longitude));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
//            Log.d("onLocationChanged", "Removing Location Updates");
//        }
//        Log.d("onLocationChanged", "Exit");
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }

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
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) { case MY_PERMISSIONS_REQUEST_LOCATION: {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (mGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
            }
            return;
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
