package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class IdentificationActivity extends AppCompatActivity {

    private ImageView selectLanguage;
    private int currentLanguage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_identification);

        selectLanguage = findViewById(R.id.imageView_select_language);
        selectLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage();
            }
        });

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

    private void changeLanguage() {

        if (currentLanguage == 0) {

            selectLanguage.setImageResource(R.drawable.spain);
            currentLanguage = 1;
        }
        else {
            selectLanguage.setImageResource(R.drawable.united_kingdom);
            currentLanguage = 0;
        }
    }

    public void patient(View view){
        Intent patientView = new Intent(this, PatientIdentificationActivity.class);
        //Intent patientView = new Intent(this, PatientLogin.class);
        startActivity(patientView);
    }

    public void professional(View view){
        Intent i = new Intent( this, ProfessionalIdentificationActivity.class);
        startActivity(i);
    }
}