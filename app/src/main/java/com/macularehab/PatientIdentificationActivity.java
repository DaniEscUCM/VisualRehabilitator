package com.macularehab;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

//Buttons
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class PatientIdentificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_login_patient_choose);
    }
}


