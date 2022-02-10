package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
//Buttons
import android.view.View;
import android.widget.Button;

//Firebase Analytics
import com.google.firebase.analytics.FirebaseAnalytics;
//Firebase Authenticator
import com.google.firebase.auth.FirebaseAuth;

public class PatientLogin extends AppCompatActivity  {

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;

    //Buttons
    Button singInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_login_patient);

        startFirebase();
        startAuthenticator();

        setupAuthenticator();
    }

    //Evento de FirebaseAnalytics
    public void startFirebase() {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString("message", "El usuario ha entrado a login de paciente");
        mFirebaseAnalytics.logEvent("PatientScreen", bundle);
    }

    public void startAuthenticator() {

        mAuth = FirebaseAuth.getInstance();
    }

    public void setupAuthenticator() {

        singInButton = findViewById(R.id.singInButton);

        singInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Settings(v);
            }
        });
    }
}
