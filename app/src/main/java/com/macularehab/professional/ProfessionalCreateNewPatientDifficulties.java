package com.macularehab.professional;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;

import java.util.ArrayList;
import java.util.Map;

//TODO
public class ProfessionalCreateNewPatientDifficulties extends AppCompatActivity {

    private ArrayList<Boolean> arrayList;
    private int numericCode;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public final static String numericCodeString = "numericCodeString";
    private final String patientsWithNoAccount = "Patient";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_create_new_patient_difficulties);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();

        Button continue_button = findViewById(R.id.button_create_new_patient_difficulties_continue);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCheckBoxesClicked();
            }
        });

        getPatient();
    }

    private void getPatient() {

        Intent intent = getIntent();
        numericCode = intent.getIntExtra(ProfessionalCreateNewPatient.numericCodeString, 0);

        if (numericCode == 0) {
            Log.w("Patient Code", "Was not read correctly");
        }
    }

    private void getCheckBoxesClicked() {

        arrayList = new ArrayList<>(25);

        LinearLayout layout = (LinearLayout) findViewById(R.id.create_new_patient_linear_layout);
        int count = 0;
        for (int i = 0; i < layout.getChildCount(); i++) {

            View v = layout.getChildAt(i);
            if (v instanceof CheckBox) {
                if (((CheckBox) v).isChecked()) {
                    arrayList.add(true);
                }
                else {
                    arrayList.add(false);
                }
                count++;
            }
        }

        Log.i("Difficulties", arrayList.toString());
        //System.out.println(arrayList.toString());

        addPatientInformation();
    }

    private void addPatientInformation() {

        Log.w("Patient Code", String.valueOf(numericCode));
        databaseReference.child(patientsWithNoAccount).child(String.valueOf(numericCode))
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Patient", "No se ha conseguido");
                }
                else {
                    Map<String, Object> patientInfo = (Map<String, Object>) task.getResult().getValue();
                    Log.i("Patient Info", patientInfo.toString());
                    patientInfo.put("checkBox", arrayList);
                    databaseReference.child(patientsWithNoAccount).child(String.valueOf(numericCode)).setValue(patientInfo);
                    ProfessionalCreateNewPatientDifficulties.this.continueWithNextActivity();
                }
            }
        });
    }

    private void continueWithNextActivity() {

        Intent continueActivity = new Intent(this, ProfessionalCreateNewPatientShowNumericCode.class);
        continueActivity.putExtra(ProfessionalCreateNewPatientDifficulties.numericCodeString, numericCode);
        startActivity(continueActivity);
    }
}
