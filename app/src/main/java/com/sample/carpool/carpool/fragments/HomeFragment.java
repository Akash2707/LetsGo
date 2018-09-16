package com.sample.carpool.carpool.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.SessionManager;
import com.sample.carpool.carpool.activities.GetRide1Activity;
import com.sample.carpool.carpool.activities.OfferRide1Activity;
import com.sample.carpool.carpool.activities.OfferRide2Activity;
import com.sample.carpool.carpool.adapter.OfferListAdapter;
import com.sample.carpool.carpool.adapter.RideSelectAdapter;
import com.sample.carpool.carpool.models.OfferList;
import com.sample.carpool.carpool.models.RideDetails;
import com.sample.carpool.carpool.models.SelectRide;
import com.sample.carpool.carpool.models.TokenDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class HomeFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private static final int REQUEST_LOCATION = 177;
    Button o_ride, g_ride;
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    LatLng latLng;
    Context context;
    GoogleApiClient mGoogleApiClient;
    SessionManager session;
    FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    String userId,token_id;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token","");


        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("token_details");
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        token_id = token;

        registerToken(userId,token_id);
        
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map_offer_ride, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

        } else {
            Log.e("DB", "PERMISSION GRANTED");
            setMap();
        }


        /*
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        o_ride = view.findViewById(R.id.offer_ride_btn);
        g_ride = view.findViewById(R.id.get_ride_btn);
        o_ride.setOnClickListener(this);
        g_ride.setOnClickListener(this);
        return view;
    }

    private void registerToken(String userId, String token_id) {

        TokenDetails tokenDetails = new TokenDetails(userId,token_id);
        mFirebaseDatabase.child(userId).setValue(tokenDetails);

    }


    public void setMap() {
        if (!(ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        latLng = new LatLng(latitude, longitude);
                        Geocoder geocoder = new Geocoder(context);
                        try {
                            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                            String str = addressList.get(0).getLocality() + " ";
                            str += addressList.get(0).getCountryName();
                            if (mMap != null) {
                                Log.e("DB","mapNET");
                                mMap.clear();
                                mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));
                                PermissionCheck();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
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
                });

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                            String str = addressList.get(0).getLocality() + " ";
                            str += addressList.get(0).getCountryName();
                            if (mMap != null) {
                                Log.e("DB","mapGPS");
                                mMap.clear();
                                mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));
                                PermissionCheck();

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
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
                });

            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("TAG","ready");
        mMap = googleMap;
        Geocoder geocoder = new Geocoder(getContext());
        try {
            if (latLng != null) {
                mMap.clear();
                List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                String str = addressList.get(0).getLocality() + " ";
                str += addressList.get(0).getCountryName();
                mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));
                PermissionCheck();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.offer_ride_btn:
                startActivity(new Intent(getContext(), OfferRide2Activity.class));
                break;

            case R.id.get_ride_btn:
                startActivity(new Intent(getActivity(), GetRide1Activity.class));
        }

    }

    public void PermissionCheck(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
