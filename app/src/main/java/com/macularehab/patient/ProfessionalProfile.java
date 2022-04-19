package com.macularehab.patient;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;

import java.util.HashMap;

public class ProfessionalProfile extends AppCompatActivity {

    private TextView professionalNameTextView;

    private final String filenameCurrentPatient = "CurrentPatient.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_professional_profile);

        professionalNameTextView = findViewById(R.id.patient_professional_profile_name);

        ImageButton backButton = findViewById(R.id.patient_professional_profile_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        readProfessionalName();
    }

    private void readProfessionalName() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        String name = "";
        if (map.containsKey("professional_name")) {

            name = String.valueOf(map.get("professional_name"));
        }

        professionalNameTextView.setText(name);
    }
}
