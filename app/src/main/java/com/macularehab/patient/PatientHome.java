package com.macularehab.patient;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.ExercisesActivity;
import com.macularehab.IdentificationActivity;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;
import com.macularehab.patient.data.PatientDataInfoActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PatientHome extends AppCompatActivity {

    private TextView patient_username_textView;

    private String patientUID;
    private String patientNumericCode;
    private String patientUsername;
    private String professionalUID;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private final String db_patient = "Patient";
    private final String db_professionalUID = "ProfessionalUID";
    private final String db_patientNumericCode = "PatientNumericCode";
    private final String db_professional = "Professional";
    private final String db_patientS = "Patients";

    private final String filenameCurrentPatient = "CurrentPatient.json";
    private final String isFocus = "focusIsOn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_home_choose_data_exercise);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        patientUID = mAuth.getUid();

        patient_username_textView = findViewById(R.id.patient_home_patient_name_textView);
        String[] patient_username = mAuth.getCurrentUser().getEmail().split("@");
        patient_username_textView.setText(patient_username[0]);


        ImageButton goBackButton = findViewById(R.id.patient_home_back_button);
        goBackButton.setVisibility(View.GONE);

        //getProfessionalUID();

        Button dataButton = findViewById(R.id.professional_patient_home_data_button);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientData();
            }
        });

        Button exercisesButton = findViewById(R.id.professional_patient_home_exercises_button);
        exercisesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToExercises();
            }
        });

        Button logoutButton = findViewById(R.id.professional_patient_home_logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map= readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        Switch focus_switch = findViewById(R.id.focus_switch);
        focus_switch.setChecked((Boolean) map.get(isFocus));
        focus_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
                HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentPatient);
                if(!(Boolean)mapS.get(isFocus)) {
                    mapS.put(isFocus, true);

                    Gson gson = new Gson();
                    String data = gson.toJson(mapS);
                    WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
                    writeInternalStorage.write(getApplicationContext(), filenameCurrentPatient, data);
                    databaseReference.child("Professional").child((String) mapS.get("professional_uid")).
                            child("Patients").child((String) mapS.get("patient_numeric_code")).child(isFocus).setValue(true);
                }
            }{
                ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
                HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentPatient);
                if((Boolean)mapS.get(isFocus)) {
                    mapS.put(isFocus, false);

                    Gson gson = new Gson();
                    String data = gson.toJson(mapS);
                    WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
                    writeInternalStorage.write(getApplicationContext(), filenameCurrentPatient, data);
                    databaseReference.child("Professional").child((String) mapS.get("professional_uid")).
                            child("Patients").child((String) mapS.get("patient_numeric_code")).child(isFocus).setValue(true);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        patientUID = mAuth.getUid();
        getProfessionalUID();
    }

    private void getProfessionalUID() {

        databaseReference.child(db_patient).child(patientUID)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("PatientHome", "error getting professional UID");
                }
                else {

                    Map<String, Object> patientInfo = (Map<String, Object>) task.getResult().getValue();
                    professionalUID = String.valueOf(patientInfo.get(db_professionalUID));
                    patientNumericCode = String.valueOf(patientInfo.get(db_patientNumericCode));

                    loadPatientInfoToInternalStorage();
                }
            }
        });
    }

    private void loadPatientInfoToInternalStorage() {

        databaseReference.child(db_professional).child(professionalUID).child(db_patientS).child(patientNumericCode)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("PatientHome", "error getting professional UID");
                }
                else {

                    HashMap<String, Object> map = (HashMap<String, Object>) task.getResult().getValue();
                    Log.w("Here", "Aqui estamos seeee");

                    if (map == null) {
                        map = new HashMap<>();
                    }

                    writeInternalStoragePatientList(map);
                }
            }
        });
    }

    private void writeInternalStoragePatientList(HashMap<String, Object> map) {

        Gson gson = new Gson();
        String data = gson.toJson(map);

        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameCurrentPatient, data);
    }

    private void logOut() {

        FirebaseAuth.getInstance().signOut();

        patient_username_textView.setText("Patient");

        Intent mainActivity = new Intent(this, IdentificationActivity.class);
        startActivity(mainActivity);
    }

    private void goToPatientData() {

        Intent intent = new Intent(this, PatientDataInfoActivity.class);
        startActivity(intent);
    }

    private void goToExercises() {

        Intent intent = new Intent(this, ExercisesActivity.class);
        startActivity(intent);
    }
}
