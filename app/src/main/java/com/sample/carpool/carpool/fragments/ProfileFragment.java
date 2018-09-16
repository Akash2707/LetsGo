package com.sample.carpool.carpool.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.SessionManager;
import com.sample.carpool.carpool.activities.EditProfileActivity;
import com.sample.carpool.carpool.models.UserDetails;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView tv_name,tv_email;
    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private String userId;
    private SimpleDraweeView profile;
    String fname,lname,mail;
    SessionManager sessionManager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        tv_name = view.findViewById(R.id.name);
        tv_email = view.findViewById(R.id.email);
        profile = view.findViewById(R.id.profile_pic);
        sessionManager = new SessionManager(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("user_details");

        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        Log.e("ID",userId);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);

            }
        });

        tv_name.setText(sessionManager.fname()+""+sessionManager.lname());
        tv_email.setText(sessionManager.mail());
        profile.setImageURI(sessionManager.image());

        return view;
    }

}
