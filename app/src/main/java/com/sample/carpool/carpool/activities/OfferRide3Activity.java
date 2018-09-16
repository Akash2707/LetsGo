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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.sample.carpool.carpool.adapter.DriverListAdapter;
import com.sample.carpool.carpool.adapter.VehicleListAdapter;
import com.sample.carpool.carpool.models.DriverDetails;
import com.sample.carpool.carpool.models.DriverList;
import com.sample.carpool.carpool.models.VehicleDetails;
import com.sample.carpool.carpool.models.VehicleList;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by akash on 05-02-2018.
 */

public class OfferRide3Activity extends AppCompatActivity implements CustomItemClickListener{

    Button btn, btn_pop;
    private EditText driverName, licenceNo, age, mobileNo, exp;
    private RadioGroup radio_group;
    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private String userId;
    private RadioButton radioButton;
    private String regex = "^[A-Z]{2}[0-9]{2}[0-9]{4}[0-9]{7}$";
    private String vehicleID, sNo, cName;
    private RadioGroup rg_pop;
    private RadioButton rb_pop;
    private TextView tv, tv_licence_no;
    RelativeLayout rl, rl1, rl2;
    ArrayList<DriverList> driverLists;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    DriverListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntent().getExtras();
        vehicleID = getIntent().getStringExtra("VID");
        cName = getIntent().getStringExtra("CNAME");
        sNo = getIntent().getStringExtra("SEATS");
        Log.e("AKASH", vehicleID + "");

        rl1 = findViewById(R.id.rl_list);

        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();


        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("driver_details");


        AlertDialog.Builder alert_builder = new AlertDialog.Builder(OfferRide3Activity.this);
        alert_builder.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.activity_offer_ride_3, null);
        rl2 = findViewById(R.id.relative_driver_popup);
        rg_pop = view.findViewById(R.id.rg_driver_pop);
        btn_pop = view.findViewById(R.id.btn_driver_pop_up);
        alert_builder.setView(view);
        final AlertDialog dialog = alert_builder.create();
        dialog.show();


        rg_pop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                i = rg_pop.getCheckedRadioButtonId();
                switch (i) {
                    case R.id.driver_yes:
                        Log.e("Akash", "yees");
                        btn_pop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.cancel();

                                setContentView(R.layout.activity_offer_ride_3);

                                rl1 = findViewById(R.id.rl_driver_list);
                                rl2 = findViewById(R.id.relative_driver_popup);
                                recyclerView = findViewById(R.id.offered_ride_list_v2);
                                progressBar = findViewById(R.id.progressBar_offer);
                                tv = findViewById(R.id.driver_list_tv);
                                tv_licence_no = findViewById(R.id.licence_no_tv);
                                rl2.setVisibility(View.GONE);
                                rl1.setVisibility(View.VISIBLE);

                                linearLayoutManager = new LinearLayoutManager(OfferRide3Activity.this);
                                driverLists = new ArrayList<>();
                                adapter = new DriverListAdapter(OfferRide3Activity.this,driverLists, OfferRide3Activity.this);

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
                                                DriverDetails driverDetails = postSnapshot.getValue(DriverDetails.class);
                                                String licence_no = driverDetails.getLicence_no();
                                                String owner_no = driverDetails.getVehicle_owner_id();
                                                if (owner_no.equals(userId)) {
                                                    DriverList list = postSnapshot.getValue(DriverList.class);
                                                    progressBar.setVisibility(View.GONE);
                                                    driverLists.add(list);
                                                    recyclerView.setAdapter(adapter);
                                                    Log.e("AKASH", driverLists.size() + "");
                                                }

                                            }
                                            if (driverLists.size() == 0) {
                                                Log.e("AKASH", driverLists.size() + "");
                                                progressBar.setVisibility(View.GONE);
                                                tv.setText("Oops!! You don't have any driver registered....");
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Log.e("AKASH", driverLists.size() + "");
                                            progressBar.setVisibility(View.GONE);
                                            tv.setText("Oops!! You don't have any driver registered....");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                        });
                        break;

                    case R.id.driver_no:
                        Log.e("Akash", "no");
                        btn_pop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("Akash", "btn");

                                dialog.cancel();


                                setContentView(R.layout.activity_offer_ride_3);


                                driverName = findViewById(R.id.activity_offer_ride_3_dn_et);
                                licenceNo = findViewById(R.id.activity_offer_ride_3_ln_et1);
                                age = findViewById(R.id.activity_offer_ride_3_age_et2);
                                mobileNo = findViewById(R.id.activity_offer_ride_3_mn_et3);
                                exp = findViewById(R.id.activity_offer_ride_3_de_et4);
                                radio_group = findViewById(R.id.rg_gender_driver);





                                rl = findViewById(R.id.relative_driver_details);
                                rl2 = findViewById(R.id.relative_driver_popup);

                                rl2.setVisibility(View.GONE);
                                rl.setVisibility(View.VISIBLE);


                                btn = findViewById(R.id.activity_offer_ride_3_btn);
                                btn.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        final String driver_id = UUID.randomUUID().toString();
                                        final String vehicleOwnerId = userId;
                                        final String dName = driverName.getText().toString().trim();
                                        final String licenceNumber = licenceNo.getText().toString().trim();
                                        final String dage = age.getText().toString().trim();
                                        final String mobileNumber = mobileNo.getText().toString().trim();
                                        final String experience = exp.getText().toString().trim();
                                        int selectedBtnId = radio_group.getCheckedRadioButtonId();
                                        radioButton = findViewById(selectedBtnId);

                                        if (TextUtils.isEmpty(dName)) {
                                            Toast.makeText(OfferRide3Activity.this, "Enter Driver's Name!!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(licenceNumber)) {
                                            Toast.makeText(OfferRide3Activity.this, "Enter Licence Number!!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!licenceNumber.matches(regex)) {
                                            Toast.makeText(OfferRide3Activity.this, "Enter valid Licence Number!!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(dage)) {
                                            Toast.makeText(OfferRide3Activity.this, "Enter Driver's Age!!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (selectedBtnId == -1) {
                                            Toast.makeText(OfferRide3Activity.this, "Select gender!!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(mobileNumber)) {
                                            Toast.makeText(OfferRide3Activity.this, "Enter Driver's Number!!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (mobileNumber.length() != 10) {
                                            Toast.makeText(OfferRide3Activity.this, "Enter valid Mobile number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (TextUtils.isEmpty(experience)) {
                                            Toast.makeText(OfferRide3Activity.this, "Enter Driver's Experience!!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        Driver(driver_id, vehicleOwnerId, dName, licenceNumber, dage, radioButton.getText() + "", mobileNumber, experience);

                                        Intent intent = new Intent(OfferRide3Activity.this, OfferRide1Activity.class);

                                        intent.putExtra("DID", driver_id);
                                        intent.putExtra("VID", vehicleID);
                                        intent.putExtra("NAME", cName);
                                        intent.putExtra("SEATS_NO", sNo);

                                        startActivity(intent);

                                    }
                                });

                            }
                        });
                }
            }
        });


    }

    private void Driver(String driver_id, String vehicleOwnerId, String dName, String licenceNumber, String dage, String gender, String mobileNumber, String experience) {

                                DriverDetails driverDetails = new DriverDetails(driver_id, vehicleOwnerId, dName, licenceNumber, dage, gender, mobileNumber, experience);
                                mFirebaseDatabase.child(licenceNumber).setValue(driverDetails);
                            }

    @Override
    public void onItemClick(View v, int position) {
        DriverList model = driverLists.get(position);
        final String licence_no = model.getLicence_no();
        Toast.makeText(this, licence_no, Toast.LENGTH_SHORT).show();
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DriverDetails driverDetails = postSnapshot.getValue(DriverDetails.class);
                    String lice_no =driverDetails.getLicence_no();
                    String owner_no = driverDetails.getVehicle_owner_id();
                    if (licence_no.equals(lice_no)) {
                        String driver_id = driverDetails.getDriver_id();


                        Intent intent = new Intent(OfferRide3Activity.this, OfferRide1Activity.class);

                        intent.putExtra("DID", driver_id);
                        intent.putExtra("VID", vehicleID);
                        intent.putExtra("NAME", cName);
                        intent.putExtra("SEATS_NO", sNo);

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