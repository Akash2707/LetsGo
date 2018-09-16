package com.sample.carpool.carpool.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.SessionManager;

/**
 * Created by akash on 06-03-2018.
 */

public class EditProfileActivity extends AppCompatActivity {
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage = null;
    FirebaseStorage storage;
    StorageReference storageRef, imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    SimpleDraweeView imageView;
    Button btn, btn_ep;
    SessionManager sessionManager;
    EditText fnameET, lnameET;
    TextView emailET;
    DatabaseReference mFirebaseDatabase;
    FirebaseAuth auth;
    String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        imageView = findViewById(R.id.ivProfile);
        btn = findViewById(R.id.activity_edit_profile_btn);
        btn_ep = findViewById(R.id.edit_image);
        fnameET = findViewById(R.id.activity_edit_profile_et_fn);
        lnameET = findViewById(R.id.activity_edit_profile_et_ln);
        emailET = findViewById(R.id.activity_edit_profile_tv_mail);
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("user_details");
        sessionManager = new SessionManager(this);
        //accessing the firebase storage
        storage = FirebaseStorage.getInstance();
        //creates a storage reference
        storageRef = storage.getReference();

        progressDialog = new ProgressDialog(this);

        fnameET.setText(sessionManager.fname());
        lnameET.setText(sessionManager.lname());
        emailET.setText(sessionManager.mail());

        imageView.setImageURI(sessionManager.image());


        btn_ep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailET.getText().toString().trim();
                final String first_name = fnameET.getText().toString().trim();
                final String last_name = lnameET.getText().toString().trim();
                uploadImage(view);
                sessionManager.savedata(first_name, last_name, email);
                updateUser(first_name, last_name);
               /* ProfileFragment profileFragment = new ProfileFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content,profileFragment);
                fragmentTransaction.commit();*/
                Intent intent = new Intent(EditProfileActivity.this, SidebarActivity.class);
                startActivity(intent);
                finish();

            }
        });


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
                    Toast.makeText(EditProfileActivity.this, "Image Edited, click on Save button", Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                    Log.e("SelectedIMG", selectedImage + "");
                    imageView.setImageURI(selectedImage);
                    mFirebaseDatabase.child(userId).child("image_ur").setValue(selectedImage+"");
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
                    Toast.makeText(EditProfileActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                    dissmissDialog();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(EditProfileActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
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

