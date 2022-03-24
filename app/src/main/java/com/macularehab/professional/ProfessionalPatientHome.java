package com.macularehab.professional;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.macularehab.ExercisesActivity;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;

import java.util.HashMap;

public class ProfessionalPatientHome extends AppCompatActivity {

    private TextView patientName_textView;
    private Button dataButton;
    private Button exercisesButton;
    private final String filenameCurrentPatient = "CurrentPatient.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_home_choose_data_exercise);

        patientName_textView = findViewById(R.id.patient_home_patient_name_textView);

        dataButton = findViewById(R.id.professional_patient_home_data_button);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToDataActivity();
            }
        });

        exercisesButton = findViewById(R.id.professional_patient_home_exercises_button);
        exercisesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToExercisesActivity();
            }
        });

        readPatientName();
    }

    private void readPatientName() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        String name = map.get("name").toString();
        String lastName = map.get("first_lastName").toString();

        patientName_textView.setText(name + " " + lastName);
    }

    private void gotToDataActivity() {

        Intent intent = new Intent(this, ProfessionalPatientInfo.class);
        startActivity(intent);
    }

    private void goToExercisesActivity() {
        Intent intent = new Intent(this, ExercisesActivity.class);
        startActivity(intent);
    }
}
