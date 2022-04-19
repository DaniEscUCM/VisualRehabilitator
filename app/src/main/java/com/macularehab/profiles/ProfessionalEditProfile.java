package com.macularehab.profiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.macularehab.R;
import com.macularehab.professional.account.ChangeEmail;
import com.macularehab.professional.account.ChangeName;
import com.macularehab.professional.account.ChangePassword;
import com.macularehab.professional.account.ChangePhone;
import com.macularehab.professional.account.ProfessionalSignUpActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfessionalEditProfile extends AppCompatActivity {

    private static final String TAG = "EditProfile";

    private CircleImageView profileImageView;
    private Button profileChangeButton;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;

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
        databaseReference = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("ProfilePics");
        
        profileImageView = findViewById(R.id.professional_profile_editProfile_profileImage);
        nameTextView = findViewById(R.id.professional_profile_editProfile_name);
        emailTextView = findViewById(R.id.professional_profile_editProfile_email);
        phoneTextView = findViewById(R.id.professional_profile_editProfile_phone);

        Button changePasswordButton = findViewById(R.id.professional_profile_editProfile_changePassword_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangePasswordActivity();
            }
        });

        profileChangeButton = findViewById(R.id.professional_profile_editProfile_changeImage_button);
        profileChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfessionalEditProfile.this);
            }
        });

        getProfessionalPhoto();

        Button changeEmailButton = findViewById(R.id.professional_profile_editProfile_changeEmail_button);
        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangeEmailActivity();
            }
        });

        Button changeNameButton = findViewById(R.id.professional_profile_editProfile_changeName_button);
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangeNameActivity();
            }
        });

        Button changePhoneButton = findViewById(R.id.professional_profile_editProfile_changePhone_button);
        changePhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangePhoneActivity();
            }
        });

        ImageButton backButton = findViewById(R.id.professional_profile_editProfile_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getProfessionalPhoto() {

        if (mAuth.getCurrentUser().getPhotoUrl() != null) {

            String imageUri = String.valueOf(mAuth.getCurrentUser().getPhotoUrl());
            Picasso.get().load(imageUri).into(profileImageView);
        }

        readProfessionalInfo();
    }

    private void readProfessionalInfo() {

        databaseReference.child("Professional").child(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    String name = "";
                    String email = "";
                    String phone = "";

                    HashMap<String, Object> profMap = (HashMap<String, Object>) task.getResult().getValue();

                    name = String.valueOf(profMap.get("name"));

                    if (profMap.containsKey("contact_info")) {

                        HashMap<String, Object> contactMap = (HashMap<String, Object>) profMap.get("contact_info");

                        email = String.valueOf(contactMap.get("email_address"));
                        phone = String.valueOf(contactMap.get("phone_number"));
                    }

                    nameTextView.setText(name);
                    emailTextView.setText(email);
                    phoneTextView.setText(phone);

                }
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

                uploadProfileImage();
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

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(Uri.parse(myUri))
                                .build();
                        mAuth.getCurrentUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                        }
                                    }
                                });

                        /*databaseReference.child("Professional").child(mAuth.getCurrentUser().getUid())
                                .child("contact_info").child("profilePic_url")*/

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

    private void goToChangePasswordActivity() {

        Intent changePasswordIntent = new Intent(this, ChangePassword.class);
        startActivity(changePasswordIntent);
    }

    private void goToChangeEmailActivity() {

        Intent changeEmailIntent = new Intent(this, ChangeEmail.class);
        startActivity(changeEmailIntent);
    }

    private void goToChangeNameActivity() {

        Intent changeNameIntent = new Intent(this, ChangeName.class);
        startActivity(changeNameIntent);
    }

    private void goToChangePhoneActivity() {

        Intent changePhoneIntent = new Intent(this, ChangePhone.class);
        startActivity(changePhoneIntent);
    }
}
