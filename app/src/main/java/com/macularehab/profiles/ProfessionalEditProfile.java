package com.macularehab.profiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.macularehab.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfessionalEditProfile extends AppCompatActivity {

    private CircleImageView profileImageView;
    private Button closeButton, saveButton;
    private Button profileChangeButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_profile_edit_profile);

        //init
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        profileImageView = findViewById(R.id.professional_profile_editProfile_profileImage);

        Button changePasswordButton = findViewById(R.id.professional_profile_editProfile_changePassword_button);
        //changePasswordButton.setBackgroundResource(R.drawable.border_for_layout_blue);
        /*changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changePasswordButton.setSelected(!changePasswordButton.isSelected());
                //changePasswordButton.setBackground(R.drawable.border_for_layout_yellow);
                changePasswordButton.setBackgroundResource(R.drawable.border_for_layout_yellow);
            }
        });*/

        /*closeButton = findViewById(R.id.button_close);
        saveButton = findViewById(R.id.button_save);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent());
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });*/

        profileChangeButton = findViewById(R.id.professional_profile_editProfile_changeImage_button);
        profileChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfessionalEditProfile.this);
            }
        });

        getUserInfo();
    }

    private void getUserInfo() {

        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    if (snapshot.hasChild("image")) {
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                profileImageView.setImageURI(imageUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, "Error OnActivityResul", Toast.LENGTH_LONG);
            }
        }
    }

    private void uploadProfileImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        if (imageUri != null) {

            final StorageReference fileRef = storageReference.child(mAuth.getCurrentUser().getUid() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = (Uri) task.getResult();
                        myUri = downloadUri.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUri);

                        databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);

                        progressDialog.dismiss();
                    }
                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "image not selected", Toast.LENGTH_LONG).show();
        }
    }
}
