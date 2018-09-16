package com.sample.carpool.carpool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.CustomItemClickListener;
import com.sample.carpool.carpool.adapter.OfferListAdapter;
import com.sample.carpool.carpool.adapter.VehicleListAdapter;
import com.sample.carpool.carpool.models.OfferList;
import com.sample.carpool.carpool.models.VehicleDetails;
import com.sample.carpool.carpool.models.VehicleList;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by akash on 01-02-2018.
 */


public class OfferRide2Activity extends AppCompatActivity implements CustomItemClickListener {

    private Button btn, btn_pop;
    private EditText registerNo, seatsNo, carName;
    private Spinner cartT, brand, fuelT;
    private FirebaseAuth auth;
    private String userId;
    private DatabaseReference mFirebaseDatabase;
    private String regex = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$";
    private RadioGroup rg_pop;
    private RadioButton rb_pop;
    private TextView tv, tv_car_no;
    RelativeLayout rl, rl1, rl2;
    ArrayList<VehicleList> vehicleList;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    VehicleListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rl1 = findViewById(R.id.rl_list);


        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();


        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("vehicle_details");


        AlertDialog.Builder alert_builder = new AlertDialog.Builder(OfferRide2Activity.this);
        alert_builder.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.activity_offer_ride_2, null);
        rl2 = findViewById(R.id.relative_popup);
        rg_pop = view.findViewById(R.id.rg_pop);
        btn_pop = view.findViewById(R.id.btn_pop_up);
        alert_builder.setView(view);
        final AlertDialog dialog = alert_builder.create();
        dialog.show();


        rg_pop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                i = rg_pop.getCheckedRadioButtonId();
                switch (i) {
                    case R.id.yes:
                        Log.e("Akash", "yees");
                        btn_pop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.cancel();

                                setContentView(R.layout.activity_offer_ride_2);

                                rl1 = findViewById(R.id.rl_list);
                                rl2 = findViewById(R.id.relative_popup);
                                recyclerView = findViewById(R.id.offered_ride_list_vl);
                                progressBar = findViewById(R.id.progressBar_offer);
                                tv = findViewById(R.id.vehicle_list_tv);
                                tv_car_no = findViewById(R.id.car_no_tv);
                                rl2.setVisibility(View.GONE);
                                rl1.setVisibility(View.VISIBLE);

                                linearLayoutManager = new LinearLayoutManager(OfferRide2Activity.this);
                                vehicleList = new ArrayList<>();
                                adapter = new VehicleListAdapter(OfferRide2Activity.this, vehicleList, OfferRide2Activity.this);
//                                adapter = new VehicleListAdapter(OfferRide2Activity.this,vehicleList, new CustomItemClickListener() {
//                                    @Override
//                                    public void onItemClick(View v, int position) {
//                                        Log.d("Akash", "clicked position:" + position);
//                                        String postId = vehicleList.get(position).getRegistration_no();
//                                    }
//                                });
                                recyclerView.setLayoutManager(linearLayoutManager);

                                progressBar.setVisibility(View.VISIBLE);

                                mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.e("akashhh", "hello");
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                Boolean hasChild = dataSnapshot.hasChildren();
                                                Log.e("AKASH", hasChild + "");
                                                VehicleDetails vehicleDetails = postSnapshot.getValue(VehicleDetails.class);
                                                String car_no = vehicleDetails.getRegistration_no();
                                                String owner_no = vehicleDetails.getVehicle_owner_id();
                                                if (owner_no.equals(userId)) {
                                                    VehicleList list = postSnapshot.getValue(VehicleList.class);
                                                    progressBar.setVisibility(View.GONE);
                                                    vehicleList.add(list);
                                                    recyclerView.setAdapter(adapter);
                                                    Log.e("AKASH", vehicleList.size() + "");
                                                }
                                            }if (vehicleList.size() == 0) {
                                                Log.e("AKASH", vehicleList.size() + "");
                                                progressBar.setVisibility(View.GONE);
                                                tv.setText("Oops!! You don't have any vehicles registered....");
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Log.e("AKASH", vehicleList.size() + "");
                                            progressBar.setVisibility(View.GONE);
                                            tv.setText("Oops!! You don't have any vehicles registered....");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                        });
                        break;

                    case R.id.no:
                        Log.e("Akash", "no");
                        btn_pop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("Akash", "btn");

                                dialog.cancel();


                                setContentView(R.layout.activity_offer_ride_2);

                                registerNo = findViewById(R.id.activity_offer_ride_2_reg_et);
                                seatsNo = findViewById(R.id.activity_offer_ride_2_seats_et1);
                                carName = findViewById(R.id.activity_offer_ride_2_car_name_et2);
                                cartT = findViewById(R.id.activity_offer_ride_sp);
                                brand = findViewById(R.id.activity_offer_ride_sp1);
                                fuelT = findViewById(R.id.activity_offer_ride_sp2);
                                btn = findViewById(R.id.activity_offer_ride_2_btn);

                                rl = findViewById(R.id.relative_details);
                                rl2 = findViewById(R.id.relative_popup);

                                rl2.setVisibility(View.GONE);
                                rl.setVisibility(View.VISIBLE);


                                final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(OfferRide2Activity.this,
                                        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.car_type));

                                final ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(OfferRide2Activity.this,
                                        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.car_brand));

                                final ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(OfferRide2Activity.this,
                                        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.fuel_type));

                                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                cartT.setAdapter(myAdapter);

                                myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                brand.setAdapter(myAdapter1);

                                myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                fuelT.setAdapter(myAdapter2);


                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final String vehicleId = UUID.randomUUID().toString();
                                        final String vehicleOwnerId = userId;
                                        final String registration_no = registerNo.getText().toString().trim();
                                        final String seats = seatsNo.getText().toString().trim();
                                        final String car_name = carName.getText().toString().trim();
                                        final String car_type = cartT.getSelectedItem().toString().trim();
                                        final String car_brand = brand.getSelectedItem().toString().trim();
                                        final String fuel_type = fuelT.getSelectedItem().toString().trim();

                                        if (TextUtils.isEmpty(registration_no)) {
                                            Toast.makeText(OfferRide2Activity.this, "Enter Registration Number!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!registration_no.matches(regex)) {
                                            Toast.makeText(OfferRide2Activity.this, "Enter valid Registration Number!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (TextUtils.isEmpty(seats)) {
                                            Toast.makeText(OfferRide2Activity.this, "Enter No. of Seats!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (TextUtils.isEmpty(car_name)) {
                                            Toast.makeText(OfferRide2Activity.this, "Enter Car Name!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (car_type.equalsIgnoreCase("")) {
                                            Toast.makeText(OfferRide2Activity.this, "Select car Type", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (car_brand.equalsIgnoreCase("")) {
                                            Toast.makeText(OfferRide2Activity.this, "Select brand Type", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (fuel_type.equalsIgnoreCase("")) {
                                            Toast.makeText(OfferRide2Activity.this, "Select fuel Type", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        Vehicle(vehicleId, vehicleOwnerId, registration_no, seats, car_type, car_name, car_brand, fuel_type);

                                        Intent intent = new Intent(OfferRide2Activity.this, OfferRide3Activity.class);
                                        intent.putExtra("VID", vehicleId);
                                        intent.putExtra("CNAME", car_name);
                                        intent.putExtra("SEATS", seats);
                                        startActivity(intent);
                                    }
                                });

                            }
                        });
                }
            }
        });


    }


    private void Vehicle(String vehicleId, String vehicleOwnerId, String registration_no, String seats, String car_type, String car_name, String car_brand, String fuel_type) {
        VehicleDetails vehicleDetails = new VehicleDetails(vehicleId, vehicleOwnerId, registration_no, seats, car_type, car_name, car_brand, fuel_type);
        mFirebaseDatabase.child(registration_no).setValue(vehicleDetails);
    }

    @Override
    public void onItemClick(View v, int position) {
        VehicleList model = vehicleList.get(position);
        final String reg_no = model.getRegistration_no();
        Toast.makeText(this, reg_no, Toast.LENGTH_SHORT).show();
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    VehicleDetails vehicleDetails = postSnapshot.getValue(VehicleDetails.class);
                    String car_no = vehicleDetails.getRegistration_no();
                    String owner_no = vehicleDetails.getVehicle_owner_id();
                    if (reg_no.equals(car_no)) {
                        String vehicle_id = vehicleDetails.getVehicle_id();
                        String car_name = vehicleDetails.getCar_name();
                        String seats_no = vehicleDetails.getSeats_no();

                        Intent intent = new Intent(OfferRide2Activity.this, OfferRide3Activity.class);
                        intent.putExtra("VID", vehicle_id);
                        intent.putExtra("CNAME", car_name);
                        intent.putExtra("SEATS", seats_no);
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
