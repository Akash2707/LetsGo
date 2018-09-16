package com.sample.carpool.carpool.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.adapter.RideSelectAdapter;
import com.sample.carpool.carpool.map.GetDirections;
import com.sample.carpool.carpool.models.RideDetails;
import com.sample.carpool.carpool.models.SelectRide;
import com.sample.carpool.carpool.models.VehicleDetails;

import java.util.ArrayList;
import java.util.Calendar;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.map.GetDirections;




public class GetRide1Activity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    public static final String TAG = "SampleActivityBase";
    private static final int REQUEST_CODE_AUTOCOMPLETE_PICKUP = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE_DROP = 2;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static int value;
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private Button btn;
    private TextView totalKm;
    private EditText start_location, end_location, time, date, price, passengers;
    private Spinner rideT;
    private LatLng startLatLng;
    private LatLng endLatLng;
    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabse, mFirebaseDatabse1;
    private String userId;
    private GoogleMap mMap;
    String sLocation, eLocation, rdate, rtime, cName, seatNO ;

    ArrayList<SelectRide> rideList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    RideSelectAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ride);

        btn = findViewById(R.id.activity_get_ride_btn);
        start_location = findViewById(R.id.activity_get_ride_atv);
        end_location = findViewById(R.id.activity_get_ride_atv1);

        totalKm = findViewById(R.id.activity_get_ride_tv_km_display);
        date = findViewById(R.id.get_ride_date);
        time = findViewById(R.id.get_ride_time);
        rideT = findViewById(R.id.activity_get_ride_sp1);
        passengers = findViewById(R.id.activity_get_ride_passengers_et);
        auth = FirebaseAuth.getInstance();


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(GetRide1Activity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.ride_type));

        myAdapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        rideT.setAdapter(myAdapter);

        recyclerView = findViewById(R.id.ride_list);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rideList = new ArrayList<>();
        mFirebaseDatabse = FirebaseDatabase.getInstance().getReference("ride_details");



        start_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_location.setText("");
               // openAutocompleteActivity(1);
                 openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETE_PICKUP);

            }
        });

        end_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_location.setText("");
               // openAutocompleteActivity(2);
                openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETE_DROP);
            }
        });

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(GetRide1Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                        date.setText(day_of_month + "-" + month + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(GetRide1Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        mFirebaseDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RideDetails rideDetails = postSnapshot.getValue(RideDetails.class);
                    String sLocation = rideDetails.getStart_location();
                    String eLocation = rideDetails.getEnd_location();
                    String rdate = rideDetails.getStart_date();
                    String rtime = rideDetails.getStart_time();
                    String cName = rideDetails.getCar_name();
                    String vehicle_owner = rideDetails.getVehicle_owner_id();
                    Log.e("S_L", sLocation);
                    SelectRide selectRide = postSnapshot.getValue(SelectRide.class);
                    rideList.add(selectRide);
                    Log.e("AKASH1",vehicle_owner+"");
                    Log.e("CAAARRR",rideList.size()+"");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String startLocation = start_location.getText().toString().trim();
                final String endLocation = end_location.getText().toString().trim();
                final String startTime = time.getText().toString().trim();
                final String startDate = date.getText().toString().trim();
                final String PassengerNo = passengers.getText().toString().trim();
                final String Type = rideT.getSelectedItem().toString();

                if (TextUtils.isEmpty(startLocation)) {
                    Toast.makeText(GetRide1Activity.this, "Enter Start Location!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(endLocation)) {
                    Toast.makeText(GetRide1Activity.this, "Enter End Location!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(startDate)) {
                    Toast.makeText(GetRide1Activity.this, "Enter Date!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(startTime)) {
                    Toast.makeText(GetRide1Activity.this, "Enter Time!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Type.equalsIgnoreCase("")) {
                    Toast.makeText(GetRide1Activity.this, "Enter ride Type!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<SelectRide> tempList = new ArrayList<>();
                for (int i = 0; i <rideList.size(); i++) {

                    String sl = rideList.get(i).getStart_location();
                    String el = rideList.get(i).getEnd_location();
                    String time = rideList.get(i).getStart_time();
                    String date = rideList.get(i).getStart_date();
                    String carN = rideList.get(i).getCar_name();
                    String vOwner = rideList.get(i).getVehicle_owner_id();
                    Log.e("DATA",sl+" "+el+" "+time+" "+date+"  "+vOwner);

                    if (startLocation.equals(sl) && endLocation.equals(el) && startTime.equals(time) && startDate.equals(date)) {

                        tempList.add(rideList.get(i));
                    }

                }

                Intent intent = new Intent(GetRide1Activity.this, GetRide2Activity.class);
                intent.putExtra("pass_no",PassengerNo);
                /// Bundle bundle = new Bundle();
                intent.putParcelableArrayListExtra("list", tempList);
                Log.e("SIZE", tempList.size() + "");
                //  intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }

//
//                Log.e("SIZE", rideList.size() + "");
//
//                Intent intent = new Intent(GetRide1Activity.this, GetRide2Activity.class);
//                /// Bundle bundle = new Bundle();
//                intent.putParcelableArrayListExtra("list", rideList);
//                Log.e("SIZE", rideList.size() + "");
//                //  intent.putExtras(bundle);
//                startActivity(intent);


//                Intent intent2 = new Intent(GetRide1Activity.this, GetRide2Activity.class);
//                intent2.putParcelableArrayListExtra("test", (ArrayList<? extends Parcelable>) rideList);
//               // intent2.putExtra("LIST", (Serializable) rideList);
//                startActivity(intent2);


    private void openAutocompleteActivity(Integer codeID) {
        if (codeID == 1) {
            try {
                // The autocomplete activity requires Google Play Services to be available. The intent
                // builder checks this and throws an exception if it is not the case.
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .build(this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_PICKUP);
            } catch (GooglePlayServicesRepairableException e) {
                // Indicates that Google Play Services is either not installed or not up to date. Prompt
                // the user to correct the issue.
                GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                        0 /* requestCode */).show();
            } catch (GooglePlayServicesNotAvailableException e) {
                // Indicates that Google Play Services is not available and the problem is not easily
                // resolvable.
                String message = "Google Play Services is not available: " +
                        GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

                Log.e(TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } else if (codeID == 2) {
            try {
                // The autocomplete activity requires Google Play Services to be available. The intent
                // builder checks this and throws an exception if it is not the case.
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .build(this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_DROP);
            } catch (GooglePlayServicesRepairableException e) {
                // Indicates that Google Play Services is either not installed or not up to date. Prompt
                // the user to correct the issue.
                GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                        0 /* requestCode */).show();
            } catch (GooglePlayServicesNotAvailableException e) {
                // Indicates that Google Play Services is not available and the problem is not easily
                // resolvable.
                String message = "Google Play Services is not available: " +
                        GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

                Log.e(TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_PICKUP) {
            if (resultCode == RESULT_OK) {
                if (mMap != null)
                    mMap.clear();
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e(TAG, "Place Selected: " + place.getName());

                startLatLng = place.getLatLng();
                mMap.addMarker(new MarkerOptions()
                        .title((String) place.getName())
                        .position(place.getLatLng())
                        .snippet((String) place.getAddress()));

                // Format the place's details and display them in the TextView.
                start_location.setText(String.format("%s-%s", place.getName(), place.getAddress()));

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    Toast.makeText(GetRide1Activity.this, Html.fromHtml(attributions.toString()), Toast.LENGTH_LONG).show();
                } else {

                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_DROP) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                endLatLng = place.getLatLng();

                mMap.addMarker(new MarkerOptions()
                        .title((String) place.getName())
                        .position(place.getLatLng())
                        .snippet((String) place.getAddress()));

                // Format the place's details and display them in the TextView.
                end_location.setText(String.format("%s-%s", place.getName(), place.getAddress()));

                GetDirections getDirections = new GetDirections(startLatLng, endLatLng);
                getDirections.execute(mMap);

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    Toast.makeText(GetRide1Activity.this, Html.fromHtml(attributions.toString()), Toast.LENGTH_LONG).show();
                } else {

                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
            distanceBetween(startLatLng, endLatLng);
            totalKm.setText(value + "");
            Toast.makeText(this, value + "", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        mMap.setOnPolylineClickListener(this);

    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    mLocationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                // mMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {

        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        Toast.makeText(this, "Id: " + polyline.getId(), Toast.LENGTH_SHORT).show();
    }

    public static int distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return 0;
        }

        double distance = SphericalUtil.computeDistanceBetween(point1, point2);
        value = (int) distance / 1000;
        return value;
    }
}


