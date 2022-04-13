package com.macularehab.professional;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.ExercisesActivity;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.HashMap;

public class ProfessionalPatientHome extends AppCompatActivity {

    private TextView patientName_textView;
    private Button dataButton;
    private Button exercisesButton;
    private final String filenameCurrentPatient = "CurrentPatient.json";
    private final String isFocus = "focusIsOn";
    private boolean isOn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_home_choose_data_exercise);

        patientName_textView = findViewById(R.id.patient_home_patient_name_textView);

        ImageButton goBackButton = findViewById(R.id.patient_home_back_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
                Close();
            }
        });

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

        Button logoutButton = findViewById(R.id.professional_patient_home_logout_button);
        logoutButton.setVisibility(View.INVISIBLE);

        //readPatientName();

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map= readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        Switch focus_switch = findViewById(R.id.focus_switch);
        focus_switch.setChecked((Boolean) map.get(isFocus));
        isOn=(Boolean) map.get(isFocus);
        focus_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
            HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentPatient);
            isOn=!(Boolean)mapS.get(isFocus);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

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
        saveInfo();
        Intent intent = new Intent(this, ProfessionalPatientInfo.class);
        startActivity(intent);
    }

    private void goToExercisesActivity() {
        saveInfo();
        Intent intent = new Intent(this, ExercisesActivity.class);
        startActivity(intent);
    }

    private void Close(){
        saveInfo();
        Intent intent = new Intent(this, ProfessionalHome.class);
        startActivity(intent);
    }

    private void saveInfo(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
        HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentPatient);

        mapS.put(isFocus, isOn);

        Gson gson = new Gson();
        String data = gson.toJson(mapS);
        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameCurrentPatient, data);
        databaseReference.child("Professional").child((String) mapS.get("professional_uid")).
                child("Patients").child((String) mapS.get("patient_numeric_code")).child(isFocus).setValue(isOn);
    }
}
