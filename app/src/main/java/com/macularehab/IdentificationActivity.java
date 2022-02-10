package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class IdentificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_identification);

        Button buttonPatient =(Button) findViewById(R.id.button_patient_iden);
        buttonPatient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { patient(v);}
        });

        Button buttonProfessional =(Button) findViewById(R.id.button_professional_ident);
        buttonProfessional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                professional(v);
            }
        });
    }

    public void patient(View view){
        Intent patientView = new Intent(this, PatientLogin.class);
        startActivity(patientView);
    }

    public void professional(View view){
        Intent i = new Intent( this, ProfessionalIdentificationActivity.class);
        startActivity(i);
    }
}