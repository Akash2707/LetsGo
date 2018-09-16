package com.sample.carpool.carpool.activities;

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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.sample.carpool.carpool.map.GetDirections;
import com.sample.carpool.carpool.models.DriverDetails;
import com.sample.carpool.carpool.models.RideDetails;
import com.sample.carpool.carpool.models.VehicleDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


public class OfferRide1Activity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    public static final String TAG = "SampleActivityBase";
    private static final int REQUEST_CODE_AUTOCOMPLETE_PICKUP = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE_DROP = 2;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    private static int value;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private Button btn;
    private EditText start_location, end_location, time, date, price, rTime;
    private TextView totalKm, totalRs;
    private RadioGroup rgReturn;
    private RadioButton rbReturn;
    private Spinner rideT;
    private LatLng startLatLng;
    private LatLng endLatLng;
    private int selectedBtnId;
    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabse, mFirebaseDatabse1, mFirebaseDatabse2;
    private String userId;
    String vID, seatNo, dID, car_name;

    private GoogleMap mMap;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride);
        btn = findViewById(R.id.activity_offer_ride_btn);
        start_location = findViewById(R.id.activity_offer_ride_atv);
        end_location = findViewById(R.id.activity_offer_ride_atv1);
        totalKm = findViewById(R.id.activity_offer_ride_tv_km_display);
        totalRs = findViewById(R.id.activity_offer_ride_1_tv6);
        rgReturn = findViewById(R.id.rg_return_trip);
        date = findViewById(R.id.pickup_date);
        time = findViewById(R.id.pickup_time);
        rTime = findViewById(R.id.return_trip_time);
        rideT = findViewById(R.id.activity_offer_ride_sp3);
        price = findViewById(R.id.activity_offer_ride_et_price);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance().getReference("ride_details");
        mFirebaseDatabse1 = FirebaseDatabase.getInstance().getReference("vehicle_details");
        mFirebaseDatabse2 = FirebaseDatabase.getInstance().getReference("driver_details");
        userId = auth.getCurrentUser().getUid();

        rgReturn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rbReturn = findViewById(radioGroup.getCheckedRadioButtonId());

                if (rbReturn.getText().toString().equalsIgnoreCase("Yes")) {
                    rTime.setVisibility(View.VISIBLE);
                }
                if (rbReturn.getText().toString().equalsIgnoreCase("No")) {
                    rTime.setVisibility(View.GONE);
                }
            }
        });

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(OfferRide1Activity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.ride_type));

        myAdapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        rideT.setAdapter(myAdapter);

        start_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_location.setText("");
                //openAutocompleteActivity(1);
                openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETE_PICKUP);
            }
        });

        end_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_location.setText("");
                //openAutocompleteActivity(2);
                openAutocompleteActivity(REQUEST_CODE_AUTOCOMPLETE_DROP);
            }
        });

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_offer_ride);
        mapFragment.getMapAsync(this);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(OfferRide1Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                        date.setText(day_of_month + "-" + month + "-" + year);

                        String date1 = day_of_month + "-" + month + "-" + year;
                        date.setText(date1);
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(OfferRide1Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String time1 = hourOfDay + ":" + minute;
                                time.setText(time1);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        rTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(OfferRide1Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                rTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceKm = "0";
                priceKm = price.getText().toString();
                int total = Integer.parseInt(priceKm) * value;
                totalRs.setText(total+"");
            }
        });

         getIntent().getExtras();
        dID = getIntent().getStringExtra("DID");
        vID = getIntent().getStringExtra("VID");
        seatNo = getIntent().getStringExtra("SEATS_NO");
        car_name = getIntent().getStringExtra("NAME");
        Log.e("AKASH",vID+"");
        Log.e("AKASH",seatNo+"");


        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String rideId = UUID.randomUUID().toString();
                final String vehicleId = vID;
                final String driverId = dID;
                final String vehicleOwnerId = userId;
                final String startLocation = start_location.getText().toString().trim();
                final String endLocation = end_location.getText().toString().trim();
                final String startTime = time.getText().toString().trim();
                final String startDate = date.getText().toString().trim();
                final String returnTime = rTime.getText().toString().trim();
                final String rideType = rideT.getSelectedItem().toString();
                final String pricePerKm = price.getText().toString();
                final String availableSeats = seatNo;
                final String seatsOccupied = seatNo;
                final String tKm = totalKm.getText().toString();
                final String tRs = totalRs.getText().toString();
                final String cName = car_name;

                if (TextUtils.isEmpty(startLocation)) {
                    Toast.makeText(OfferRide1Activity.this, "Enter Start Location!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(endLocation)) {
                    Toast.makeText(OfferRide1Activity.this, "Enter End Location!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(startDate)) {
                    Toast.makeText(OfferRide1Activity.this, "Enter Date!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(startTime)) {
                    Toast.makeText(OfferRide1Activity.this, "Enter Time!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rideType.equalsIgnoreCase("")) {
                    Toast.makeText(OfferRide1Activity.this, "Select Ride Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rgReturn.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(OfferRide1Activity.this, "Enter Return Journey Details!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(rbReturn.getText().toString().equals("yes")){
                 if (TextUtils.isEmpty(returnTime)) {
                    Toast.makeText(OfferRide1Activity.this, "Enter Return Time!", Toast.LENGTH_SHORT).show();
                    return;
                    }
                }
                if (TextUtils.isEmpty(pricePerKm)) {
                    Toast.makeText(OfferRide1Activity.this, "Enter Price !", Toast.LENGTH_SHORT).show();
                    return;
                }
                Ride(rideId, vehicleId, driverId,vehicleOwnerId, startLocation, endLocation, startTime, startDate, returnTime, rideType, pricePerKm, rbReturn.getText() + "", availableSeats, seatsOccupied, tKm, tRs, cName);
                Intent intent = new Intent(OfferRide1Activity.this, SidebarActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void Ride(String rideId, String vehicleId, String driverId,String vehicleOwnerId,String startLocation, String endLocation, String startTime, String startDate, String returnTime, String rideType, String pricePerKm, String s, String availableSeats, String seatsOccupied, String tKm,String tRs,String cName) {
        RideDetails rideDetails = new RideDetails(rideId, driverId, vehicleId,vehicleOwnerId,startLocation, endLocation, startTime, startDate, s, returnTime, rideType, pricePerKm, availableSeats, seatsOccupied, tKm, tRs,cName);
        mFirebaseDatabse.child(rideId).setValue(rideDetails);
    }

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
                    Toast.makeText(OfferRide1Activity.this, Html.fromHtml(attributions.toString()), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(OfferRide1Activity.this, Html.fromHtml(attributions.toString()), Toast.LENGTH_LONG).show();
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
        GetDirections.polylineSelected(polyline);
        //System.out.println(polyline.getPoints());
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
