package com.macularehab.professional;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;

import java.util.HashMap;

public class ProfessionalPatientHome extends AppCompatActivity {

    private TextView patientName_textView;
    private final String filenameCurrentPatient = "CurrentPatient.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_home_choose_data_exercise);

        patientName_textView = findViewById(R.id.patient_home_patient_name_textView);

        readPatientName();
    }

    private void readPatientName() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        String name = map.get("name").toString();
        String lastName = map.get("first_lastName").toString();

        patientName_textView.setText(name + " " + lastName);
    }
}
