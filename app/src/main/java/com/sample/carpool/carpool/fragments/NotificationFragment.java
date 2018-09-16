package com.sample.carpool.carpool.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.CustomItemClickListener;
import com.sample.carpool.carpool.adapter.NotificationListAdapter;
import com.sample.carpool.carpool.models.NotificationDetails;
import com.sample.carpool.carpool.models.NotificationList;
import com.sample.carpool.carpool.models.TokenDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.StatementEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements CustomItemClickListener {

    TextView tv;
    Button accept, reject;
    DatabaseReference mdatabaseReference = null;
    String userId;
    ArrayList<NotificationList> notificationList;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    NotificationListAdapter adapter;
    FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    String token, sender_id, token_id;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = view.findViewById(R.id.notification_list_1);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Token", "");

        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("token_details");

        tv = view.findViewById(R.id.notification_tv1);
        progressBar = view.findViewById(R.id.notification_progressBar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("notification_details");
        linearLayoutManager = new LinearLayoutManager(getActivity());
        notificationList = new ArrayList<>();
        adapter = new NotificationListAdapter(getContext(), notificationList, this);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar.setVisibility(View.VISIBLE);

        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        NotificationDetails notificationDetails = postSnapshot.getValue(NotificationDetails.class);
                        String id = notificationDetails.getReceiver_id();
                        String name = notificationDetails.getSender_name();
                        String gender = notificationDetails.getSender_gender();
                        String phone = notificationDetails.getSender_mobile();
                        String passNum = notificationDetails.getPassenger_no();
                        String sender_id = notificationDetails.getSender_id();
                        Log.e("AKASH", id + "");
                        if (userId.equals(id)) {
                            Log.e("AKASH", "if");
                            //    NotificationList nList = postSnapshot.getValue(NotificationList.class);
                            NotificationList nList = new NotificationList(name, gender, phone, passNum, sender_id, id);

                            Log.e("CHIRAG", nList.getS_name());

                            progressBar.setVisibility(View.GONE);
                            notificationList.add(nList);
                            recyclerView.setAdapter(adapter);
                            Log.e("AKASH", name + "");
                            Log.e("AKASH", notificationList.size() + "");
                        }
                        adapter.notifyDataSetChanged();
                    }
                    if (notificationList.size() == 0) {
                        Log.e("AKASH", notificationList.size() + "");
                        progressBar.setVisibility(View.GONE);
                        tv.setText("Oops!! You don't have any notifications....");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("AKASH", "else");
                    progressBar.setVisibility(View.GONE);
                    tv.setText("Oops!! You don't have any notifications....");
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

    @Override
    public void onItemClick(View v, int position) {
        int id = v.getId();
        if(id == R.id.accept){
            NotificationList model = notificationList.get(position);
            sender_id = model.getSender_id();
            sendFCMPush();
            Toast.makeText(getContext(), sender_id + "", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.reject){
            NotificationList model = notificationList.get(position);
            String sender_name = model.getS_name();
            Toast.makeText(getContext(), sender_name + "", Toast.LENGTH_SHORT).show();
        }

    }


    private void sendFCMPush() {

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    TokenDetails tokenDetails = postSnapshot.getValue(TokenDetails.class);
                    String id = tokenDetails.getUser_id();
                    if (sender_id.equals(id)) {
                        token_id = tokenDetails.getToken_Id();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final String Legacy_SERVER_KEY = "AIzaSyDNMA1RJH_LT9oY-mdwWrKEgoxFMjLETIc";
        String msg = "Your ride has been accepted..!";
        String title = "Let's Go";
        String token = token_id;
        Log.e("hey", "infnctn");
        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name image must be there in drawable
            objData.put("tag", token);

            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("to", token);
            obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }
}

