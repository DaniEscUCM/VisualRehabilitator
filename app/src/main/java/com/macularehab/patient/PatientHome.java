package com.macularehab.patient;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.macularehab.IdentificationActivity;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.HashMap;
import java.util.Map;

public class PatientHome extends AppCompatActivity {

    private TextView welcome_name_text;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_home);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        patientUID = mAuth.getUid();

        welcome_name_text = findViewById(R.id.patient_home_welcome_name);
        String[] username = mAuth.getCurrentUser().getEmail().split("@");
        welcome_name_text.setText(username[0]);

        Button logOut_button = findViewById(R.id.button_patient_home_logout);
        logOut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

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
                    patientNumericCode = String.valueOf(patientInfo.get(patientNumericCode));

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

        welcome_name_text.setText("Patient");

        Intent mainActivity = new Intent(this, IdentificationActivity.class);
        startActivity(mainActivity);
    }
}
