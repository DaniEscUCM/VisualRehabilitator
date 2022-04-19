package com.macularehab.patient.professional;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfessionalProfile extends AppCompatActivity {

    private TextView professionalNameTextView;
    private ImageView profileImageView;

    private String professionalUID;

    private final String filenameCurrentPatient = "CurrentPatient.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_professional_profile);

        professionalNameTextView = findViewById(R.id.patient_professional_profile_name);
        profileImageView = findViewById(R.id.patient_professional_profile_profileImage);

        Button contactProfessionalButton = findViewById(R.id.patient_professional_profile_contactProfessional_button);
        contactProfessionalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToContactProfessionalActivity();
            }
        });

        ImageButton backButton = findViewById(R.id.patient_professional_profile_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        readProfessionalName();
        loadProfessionalPhoto();
    }

    private void readProfessionalName() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        String name = "";
        if (map.containsKey("professional_name")) {

            name = String.valueOf(map.get("professional_name"));
        }

        professionalUID = String.valueOf(map.get("professional_uid"));

        professionalNameTextView.setText(name);
    }

    private void loadProfessionalPhoto() {

        String imagePath = professionalUID + ".jpg";
        final String[] imageURL = {""};

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProfilePics").child(imagePath);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Uri imageUri = uri;
                imageURL[0] = imageUri.toString();
                Picasso.get().load(uri).into(profileImageView);
            }
        });
    }

    private void goToContactProfessionalActivity() {

        Intent intent = new Intent(this, ContactProfessional.class);
        startActivity(intent);
    }
}
