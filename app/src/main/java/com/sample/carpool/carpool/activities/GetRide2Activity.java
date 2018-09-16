package com.sample.carpool.carpool.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.sample.carpool.carpool.adapter.RideSelectAdapter;
import com.sample.carpool.carpool.models.NotificationDetails;
import com.sample.carpool.carpool.models.SelectRide;
import com.sample.carpool.carpool.models.TokenDetails;
import com.sample.carpool.carpool.models.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akash on 12-02-2018.
 **/

public class GetRide2Activity extends AppCompatActivity implements CustomItemClickListener {

    Button btn;
    TextView tv;
    RideSelectAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<SelectRide> selectRides;
    RecyclerView.LayoutManager linearLayoutManager;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase1,mFirebaseDatabase2;
    private FirebaseAuth auth;
    String owner,name,gender,number,userId,id,token,pass_no;
    String token_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ride_2);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Token","");

        pass_no = getIntent().getStringExtra("pass_no");

        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("token_details");
        mFirebaseDatabase1 = FirebaseDatabase.getInstance().getReference("notification_details");
        mFirebaseDatabase2 = FirebaseDatabase.getInstance().getReference("user_details");
        recyclerView = findViewById(R.id.ride_list_1);
        tv = findViewById(R.id.activity_get_ride_2_tv1);

        if (getIntent().getExtras() != null) {
//            selectRides = this.getIntent().getExtras().getParcelableArrayList("list");
//
            Log.e("if", "done");
            selectRides = getIntent().getParcelableArrayListExtra("list");
            Log.e("SIZE", selectRides.size() + "");

            adapter = new RideSelectAdapter(GetRide2Activity.this, selectRides, GetRide2Activity.this);
            Log.e("SIZE", selectRides + "");
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        if (selectRides.size() == 0) {
            Log.e("else", "error");
            tv.setText("Oops!! No rides match at this moment...");
        }

        mFirebaseDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserDetails userDetails =  postSnapshot.getValue(UserDetails.class);
                    String id = postSnapshot.getKey();
                     Log.e("AKASH",id+"");
               if(id.equals(userId)){
                   Log.e("hello","if");
                   name =   userDetails.getFirst_name()+ " " + userDetails.getLast_name();
                   number = userDetails.getMobile_number();
                   gender = userDetails.getGender();
               }
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onItemClick(View v, int position) {
        SelectRide model = selectRides.get(position);
        owner = model.getVehicle_owner_id();
        sendFCMPush();
        notifyDetails(token_id,token,owner,userId,name,gender,number,pass_no);
        Toast.makeText(this,owner, Toast.LENGTH_SHORT).show();

    }


    private void sendFCMPush() {

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    TokenDetails tokenDetails = postSnapshot.getValue(TokenDetails.class);
                    id = tokenDetails.getUser_id();
                    if(owner.equals(id)){
                        token_id = tokenDetails.getToken_Id();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final String Legacy_SERVER_KEY = "AIzaSyDNMA1RJH_LT9oY-mdwWrKEgoxFMjLETIc";
        String msg = "You have a notification..!";
        String title = "Let's Go";
        String token = token_id;
        Log.e("hey","infnctn");
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

    private void notifyDetails(String token_id, String token,String owner, String userId, String name, String gender, String number, String pass_no) {
        NotificationDetails notificationDetails = new NotificationDetails(token_id,token,owner,userId,name,gender,number,pass_no);
        mFirebaseDatabase1.child(userId).setValue(notificationDetails);
    }

}
