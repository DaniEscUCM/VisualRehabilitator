package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfessionalPageActivity extends AppCompatActivity {

    private TextView welc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_page);

        welc = (TextView) findViewById(R.id.textView_welcome_professional);
        String uname = getIntent().getStringExtra("username");
        welc.setText("Welcome "+ uname);

        Button firstPatientButton =(Button) findViewById(R.id.button_firstpatient);
        firstPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain(v);
            }
        });

        Button button_add_patient =(Button) findViewById(R.id.button_newpatient);
        button_add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_patient(v);
            }
        });
    }

    public void goToMain(View view){
        Intent i = new Intent( this, MainActivity.class);
        startActivity(i);
    }

    public void add_patient(View view){
        Intent i = new Intent( this, MapTestLeftExplanationActivity.class);
        startActivity(i);
    }
}