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
import com.sample.carpool.carpool.adapter.RequestedListAdapter;
import com.sample.carpool.carpool.models.NotificationList;
import com.sample.carpool.carpool.models.OfferList;
import com.sample.carpool.carpool.models.RequestedList;
import com.sample.carpool.carpool.models.RideDetails;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RidesRequestFragment extends Fragment {

    FirebaseAuth auth;
    DatabaseReference mdatababaseReference1,mdatabaseReference = null;
    String userId;
    TextView tv;
    ArrayList<RequestedList> requestedLists;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    RequestedListAdapter adapter;
    String o_id;
    public RidesRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rides_request, container, false);
        recyclerView = view.findViewById(R.id.requested_ride_list_1);
        tv = view.findViewById(R.id.rides_requested_tv1);
        progressBar = view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        mdatababaseReference1 = FirebaseDatabase.getInstance().getReference("notification_details");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("ride_details");
        linearLayoutManager = new LinearLayoutManager(getActivity());
        requestedLists = new ArrayList<>();
        adapter = new RequestedListAdapter(requestedLists);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar.setVisibility(View.VISIBLE);
        mdatababaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        NotificationList notificationList = postSnapshot.getValue(NotificationList.class);
                        o_id = notificationList.getReceiver_id();
                        String id = notificationList.getSender_id();
                        /*if (id.equals(userId)) {
                            mdatabaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        RideDetails rideDetails = postSnapshot.getValue(RideDetails.class);
                                        String sLocation = rideDetails.getStart_location();
                                        String eLocation = rideDetails.getEnd_location();
                                        String rdate = rideDetails.getStart_date();
                                        String rtime = rideDetails.getStart_time();
                                        String ownerId = rideDetails.getVehicle_owner_id();
                                        if (o_id.equals(ownerId)) {
                                            RequestedList requestedList = postSnapshot.getValue(RequestedList.class);
                                            progressBar.setVisibility(View.GONE);
                                            requestedLists.add(requestedList);
                                            recyclerView.setAdapter(adapter);
                                            Log.e("CAAARRR", requestedLists.size() + "");
                                        }
                                        if (requestedLists.size() == 0) {
                                            progressBar.setVisibility(View.GONE);
                                            tv.setText("Oops!! You haven't requested any rides...");
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            tv.setText("Oops!! You haven't requested any rides...");
                        }*/
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    tv.setText("Oops!! You haven't requested any rides...");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
