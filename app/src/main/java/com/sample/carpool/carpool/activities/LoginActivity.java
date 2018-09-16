package com.sample.carpool.carpool.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.SessionManager;
import com.sample.carpool.carpool.models.RideDetails;
import com.sample.carpool.carpool.models.UserDetails;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_createAccount,name;
    Button signInBtn,guest_signIn_Btn,ResetBtn;
    EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    DatabaseReference mDatabaseReference;
    String userId,mail;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,SidebarActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.logo);
        tv_createAccount = findViewById(R.id.create_account);
        signInBtn = findViewById(R.id.sign_in_btn);
        guest_signIn_Btn = findViewById(R.id.guest_sign_in);
        inputEmail = findViewById(R.id.login_email_et);
        inputPassword = findViewById(R.id.password_sign_in_et);
        progressBar = findViewById(R.id.progressBar);
        ResetBtn = findViewById(R.id.btn_reset_password);
        tv_createAccount.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        ResetBtn.setOnClickListener(this);
        sessionManager = new SessionManager(this);

        auth = FirebaseAuth.getInstance();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_btn:
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Enter email!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter Password!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if(!task.isSuccessful()){
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(LoginActivity.this, SidebarActivity.class);
                            userId = auth.getCurrentUser().getUid();
                            mDatabaseReference = FirebaseDatabase.getInstance().getReference("user_details");
                                mDatabaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                            UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                                                String e_mail = userDetails.getEmail_id();
                                                String f_name = userDetails.getFirst_name();
                                                String l_name = userDetails.getLast_name();
                                                String image = userDetails.getImage_url();
                                                sessionManager.savedata(f_name,l_name,e_mail);
                                                sessionManager.saveImage(image+"");
                                            }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.create_account:
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
                break;
            case R.id.btn_reset_password:
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
                break;
        }
    }
}
