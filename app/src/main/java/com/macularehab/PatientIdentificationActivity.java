package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//Buttons


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

public class PatientIdentificationActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_login_choose);

        startAnalytics();

        Button signUpButton = findViewById(R.id.button_choose_signup_patient);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*patientSignUp()*/;
            }
        });

        Button logInButton = findViewById((R.id.button_choose_login_patient));
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                patientLogIn();
            }
        });
    }

    public void startAnalytics() {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString("message", "El usuario ha entrado a login de paciente");
        mFirebaseAnalytics.logEvent("PatientScreen", bundle);
    }

    private void patientSignUp() {

        Intent patientSignUpView = new Intent(this, PatientSignUp.class);
        startActivity(patientSignUpView);
    }

    private void patientLogIn() {

        Intent patientLogInView = new Intent(this, PatientLogin.class);
        startActivity(patientLogInView);
    }
}


