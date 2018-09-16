package com.sample.carpool.carpool.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.SessionManager;
import com.sample.carpool.carpool.models.UserDetails;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage = null;
    String image_uri = null;
    FirebaseStorage storage;
    StorageReference storageRef, imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    private Button btn,uplaodBtn;
    private EditText input_email,input_password,input_resetpassword,input_first_name,input_last_name,input_mobile_number;
    private RadioGroup rg_gender;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private String userId;
    private RadioButton rBtn;
    SimpleDraweeView profPic;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btn = findViewById(R.id.btn_sign_up);
        input_email = findViewById(R.id.email_et);
        progressBar = findViewById(R.id.progressBar);
        input_password= findViewById(R.id.password_sign_up_et);
        input_resetpassword= findViewById(R.id.confirm_password_et);
        input_first_name = findViewById(R.id.first_name_et);
        input_last_name = findViewById(R.id.last_name_et);
        input_mobile_number = findViewById(R.id.mobile_number_et);
        profPic = findViewById(R.id.profile_pic);
        rg_gender = findViewById(R.id.rg_gender);
        uplaodBtn = findViewById(R.id.upload_image);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("user_details");
        sessionManager = new SessionManager(this);


        //accessing the firebase storage
        storage = FirebaseStorage.getInstance();
        //creates a storage reference
        storageRef = storage.getReference();

        progressDialog = new ProgressDialog(this);


        uplaodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
               /* startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();*/

               final String email = input_email.getText().toString().trim();
               String password = input_password.getText().toString().trim();
               String reset_password = input_resetpassword.getText().toString().trim();
               final String first_name = input_first_name.getText().toString().trim();
               final String last_name = input_last_name.getText().toString().trim();
               final String mobile_number = input_mobile_number.getText().toString().trim();
              // image_uri = ;
               int selectedBtnId = rg_gender.getCheckedRadioButtonId();
               rBtn = findViewById(selectedBtnId);
//               Toast.makeText(SignUpActivity.this, rBtn.getText(), Toast.LENGTH_SHORT).show();
//               Log.e("AKASH",rBtn.getText()+"");


                if(TextUtils.isEmpty(first_name)){
                    Toast.makeText(SignUpActivity.this, "Enter First Name!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(last_name)){
                    Toast.makeText(SignUpActivity.this, "Enter Last Name!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpActivity.this,"Enter Email!!",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedBtnId==-1){
                    Toast.makeText(SignUpActivity.this, "Select your gender!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(mobile_number)){
                    Toast.makeText(SignUpActivity.this, "Enter Mobile number!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mobile_number.length() != 10){
                    Toast.makeText(SignUpActivity.this, "Enter valid Mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpActivity.this, "Enter Password!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length() < 6){
                    Toast.makeText(SignUpActivity.this, "Password too short!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.equals(reset_password)){
                    Toast.makeText(SignUpActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if(((RadioButton)findViewById(rg_gender.getCheckedRadioButtonId())).getText().toString() == null){
//                    Toast.makeText(SignUpActivity.this, "Select Gender!!", Toast.LENGTH_SHORT).show();
//                    return;
//                }else {
//                    gender = ((RadioButton)findViewById(rg_gender.getCheckedRadioButtonId())).getText().toString();
//                }

                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete" +task.isSuccessful(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);



                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_LONG).show();
                        } else if (userId == null) {
                            Log.e("TAG", "create");
                            createUser(first_name,last_name,email,rBtn.getText()+"",mobile_number,image_uri);
                            uploadImage(view);
                            sessionManager.savedata(input_first_name.getText().toString(),input_last_name.getText().toString(),input_email.getText().toString());
                            startActivity(new Intent(SignUpActivity.this, SidebarActivity.class));
                            finish();
                        }
                    }
                });

            }
        });


    }

    private void createUser(String first_name, String last_name, String email, String gender, String mobile_number,String image_uri) {

        userId = auth.getCurrentUser().getUid();

        UserDetails user = new UserDetails(first_name,last_name,email,gender,mobile_number,image_uri);

        mFirebaseDatabase.child(userId).setValue(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
    private void updateUser(String first_name, String last_name) {

        if (!TextUtils.isEmpty(first_name))
            mFirebaseDatabase.child(userId).child("first_name").setValue(first_name);

        if (!TextUtils.isEmpty(last_name))
            mFirebaseDatabase.child(userId).child("last_name").setValue(last_name);
    }

    public void selectImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(SignUpActivity.this, "Image Edited, click on Save button", Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                    Log.e("SelectedIMG", selectedImage + "");
                    profPic.setImageURI(selectedImage);
                    sessionManager.saveImage(selectedImage + "");


                }
        }
    }

    public void uploadImage(View view) {
        //create reference to images folder and assing a name to the file that will be uploaded
        if (selectedImage != null) {
            imageRef = storageRef.child("images/" + selectedImage.getLastPathSegment());
            //creating and showing progress dialog

            progressDialog.setMessage("Uploading...");
            showDailog();
            progressDialog.setCancelable(false);
            //starting upload
            uploadTask = imageRef.putFile(selectedImage);
            // Observe state change events such as progress, pause, and resume
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //sets and increments value of progressbar
                    progressDialog.incrementProgressBy((int) progress);
                }
            });
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(SignUpActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                    dissmissDialog();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(SignUpActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    dissmissDialog();
                    //showing the uploaded image in ImageView using the download url
                }
            });
        }
    }

    public void dissmissDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showDailog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dissmissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dissmissDialog();
    }
}


