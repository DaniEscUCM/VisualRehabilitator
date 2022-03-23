package com.macularehab.professional;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.macularehab.R;
import com.macularehab.professional.patientForm.ProfessionalPatientEditInfo;

public class ProfessionalPatientInfo extends AppCompatActivity {

    private Button editPatientButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_patient_info);

        ImageButton backButton = findViewById(R.id.professional_patient_info_go_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfessionalPatientInfo.this.finish();
            }
        });

        editPatientButton = findViewById(R.id.professional_patient_info_editInfo_button);
        editPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditPatientActivity();
            }
        });
    }

    private void goToEditPatientActivity() {

        Intent intent = new Intent(this, ProfessionalPatientEditInfo.class);
        startActivity(intent);
    }
}
