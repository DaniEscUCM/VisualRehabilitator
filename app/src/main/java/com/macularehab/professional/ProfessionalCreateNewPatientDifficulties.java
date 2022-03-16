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
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAuth;
    public final static String numericCodeString = "numericCodeString";
    private final String db_patient = "Patient";
    private final String db_professional = "Professional";
    private final String db_numberOfPatients = "numberOfPatients";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_create_new_patient_difficulties);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

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
            else if (v instanceof LinearLayout) {

                LinearLayout linearLayout = (LinearLayout) v;
                for (int j = 0; j < linearLayout.getChildCount(); j++) {
                    View view = linearLayout.getChildAt(j);
                    LinearLayout linearLayout2 = (LinearLayout) view;
                    for (int k = 0; k < linearLayout2.getChildCount(); k++) {
                        View view2 = linearLayout2.getChildAt(k);
                        if (view2 instanceof CheckBox) {
                            if (((CheckBox) view2).isChecked()) {
                                arrayList.add(true);
                            } else {
                                arrayList.add(false);
                            }
                            count++;
                        }
                    }
                }
            }
        }

        Log.i("Difficulties", arrayList.toString());
        //System.out.println(arrayList.toString());

        addPatientInformation();
    }

    private void addPatientInformation() {

        Log.w("Patient Code", String.valueOf(numericCode));
        databaseReference.child(db_patient).child(String.valueOf(numericCode))
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
                    databaseReference.child(db_patient).child(String.valueOf(numericCode)).setValue(patientInfo);
                    //TODO Aqui tengo que modificar no pacientes, sino aquel con el codigo numero
                    databaseReference.child(db_professional).child(mAuth.getUid()).child("Patients").child(String.valueOf(numericCode)).setValue(patientInfo);
                    addNumberOfPatients();
                    ProfessionalCreateNewPatientDifficulties.this.continueWithNextActivity();
                }
            }
        });
    }

    private void addNumberOfPatients() {

        databaseReference.child(db_professional).child(mAuth.getUid()).child(db_numberOfPatients)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Patient", "No se ha conseguido");
                }
                else {

                    String result = String.valueOf(task.getResult().getValue());
                    int number = Integer.valueOf(result);
                    number++;

                    String newNumberOfPatienst = String.valueOf(number);

                    databaseReference.child(db_professional).child(mAuth.getUid()).child(db_numberOfPatients).setValue(number);
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
