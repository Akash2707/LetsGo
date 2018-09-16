package com.sample.carpool.carpool.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.adapter.OfferListAdapter;
import com.sample.carpool.carpool.adapter.RideSelectAdapter;
import com.sample.carpool.carpool.models.OfferList;
import com.sample.carpool.carpool.models.RideDetails;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RidesOfferedFragment extends Fragment {

    FirebaseAuth auth;
    DatabaseReference mdatabaseReference = null;
    String userId;
    TextView tv;
    ArrayList<OfferList> offerList;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    OfferListAdapter adapter;
    public RidesOfferedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("Akash","create");
        View view = inflater.inflate(R.layout.fragment_rides_offered, container, false);
        recyclerView = view.findViewById(R.id.offered_ride_list_1);
        tv = view.findViewById(R.id.rides_offered_tv1);
        progressBar = view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        Log.e("Akash","resume");
        super.onResume();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("ride_details");
        linearLayoutManager = new LinearLayoutManager(getActivity());
        offerList = new ArrayList<>();
        adapter = new OfferListAdapter(offerList);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar.setVisibility(View.VISIBLE);
            mdatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        RideDetails rideDetails = postSnapshot.getValue(RideDetails.class);
                        String sLocation = rideDetails.getStart_location();
                        String eLocation = rideDetails.getEnd_location();
                        String rdate = rideDetails.getStart_date();
                        String rtime = rideDetails.getStart_time();
                        String ownerId = rideDetails.getVehicle_owner_id();
                        Log.e("S_L", sLocation);
                        if (userId.equals(ownerId)) {
                            OfferList selectRide = postSnapshot.getValue(OfferList.class);
                            progressBar.setVisibility(View.GONE);
                            offerList.add(selectRide);
                            recyclerView.setAdapter(adapter);
                            Log.e("CAAARRR", offerList.size() + "");
                        }
                    } if(offerList.size() == 0) {
                            progressBar.setVisibility(View.GONE);
                            tv.setText("Oops!! You haven't created any rides...");
                    }
                    adapter.notifyDataSetChanged();
                }else{
                        progressBar.setVisibility(View.GONE);
                        tv.setText("Oops!! You haven't created any rides...");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        @Override
    public void onStart() {
        super.onStart();

    }
}
