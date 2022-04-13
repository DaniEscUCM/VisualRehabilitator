package com.macularehab.professional;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;
import com.macularehab.professional.patientForm.ProfessionalPatientEditInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ProfessionalPatientInfo extends AppCompatActivity {

    private Button editPatientButton;
    private TextView textView_lastTest;

    private TextView textView_nameTitle;

    private TextView textView_date;
    private TextView textView_name;
    private TextView textView_first_lastName;
    private TextView textView_second_lastName;
    private TextView textView_gender;
    private TextView textView_date_of_birth;
    private TextView textView_diagnostic;
    private TextView textView_av;
    private TextView textView_cv;
    private TextView textView_observations;

    private final String filenameCurrentPatient = "CurrentPatient.json";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_patient_info);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        textView_nameTitle = findViewById(R.id.textView_patientInfo_nameTitle);

        textView_date = findViewById(R.id.textView_patientInfo_date);
        textView_name = findViewById(R.id.textView_patientInfo_name);
        textView_first_lastName = findViewById(R.id.textView_patientInfo_first_lastName);
        textView_second_lastName = findViewById(R.id.textView_patientInfo_second_lastName);
        textView_gender = findViewById(R.id.textView_patientInfo_gender);
        textView_date_of_birth = findViewById(R.id.textView_patientInfo_date_of_birth);
        textView_diagnostic = findViewById(R.id.textView_patientInfo_diagnostic);
        textView_av = findViewById(R.id.textView_patientInfo_av);
        textView_cv = findViewById(R.id.textView_patientInfo_cv);
        textView_observations = findViewById(R.id.textView_patientInfo_observations);

        textView_lastTest = findViewById(R.id.professional_patient_info_lastTest_text);

        ImageButton backButton = findViewById(R.id.professional_patient_info_go_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfessionalPatientInfo.this.finish();
            }
        });

        editPatientButton = findViewById(R.id.professional_patient_info_editInfo_button);
        editPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditPatientActivity();
            }
        });

        Button tests = findViewById(R.id.button_tests);
        tests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTests();
            }
        });

        Button deletePatientButton = findViewById(R.id.professional_patient_info_deletePatient_button);
        deletePatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePatient();
            }
        });

        fillFields();
    }

    private void fillFields() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        String date = "";
        if (map.containsKey("date") && map.get("date") != null) {
            date = map.get("date").toString();
        }
        String name = map.get("name").toString();
        String first_lastName = map.get("first_lastName").toString();
        String second_lastName = map.get("second_lastName").toString();
        String gender = map.get("gender").toString();
        String date_of_birth = map.get("date_of_birth").toString();
        String diagnostic = map.get("diagnostic").toString();
        String av = String.valueOf(map.get("av"));
        String cv = String.valueOf(map.get("cv"));
        String observations = map.get("observations").toString();

        String lastTest = map.get("date_last_test").toString();
        String[] lastTestAux = lastTest.split(" ");
        lastTest = lastTestAux[0].replaceAll("_", "/");

        textView_nameTitle.setText(name);

        textView_date.setText(date);
        textView_name.setText(name);
        textView_first_lastName.setText(first_lastName);
        textView_second_lastName.setText(second_lastName);
        textView_gender.setText(gender);
        textView_date_of_birth.setText(date_of_birth);
        textView_diagnostic.setText(diagnostic);
        textView_av.setText(av);
        textView_cv.setText(cv);
        textView_observations.setText(observations);

        textView_lastTest.setText(lastTest);

        ArrayList<Boolean> arrayList = new ArrayList<Boolean>((Collection<? extends Boolean>) map.get("checkBox"));
        setCheckBoxesClicked(arrayList);
    }

    private void setCheckBoxesClicked(ArrayList<Boolean> arrayList) {

        LinearLayout layout = (LinearLayout) findViewById(R.id.professional_patient_info_linearLayout);
        int count = 0;
        for (int i = 0; i < layout.getChildCount(); i++) {

            View v = layout.getChildAt(i);
            if (v instanceof CheckBox) {

                ((CheckBox) v).setChecked(arrayList.get(count));
                ((CheckBox) v).setClickable(false);
                count++;
            } else if (v instanceof LinearLayout) {

                LinearLayout linearLayout = (LinearLayout) v;
                for (int j = 0; j < linearLayout.getChildCount(); j++) {
                    View view = linearLayout.getChildAt(j);
                    if (view instanceof LinearLayout) {
                        LinearLayout linearLayout2 = (LinearLayout) view;
                        for (int k = 0; k < linearLayout2.getChildCount(); k++) {
                            View view2 = linearLayout2.getChildAt(k);

                            if (view2 instanceof CheckBox) {
                                ((CheckBox) view2).setChecked(arrayList.get(count));
                                ((CheckBox) view2).setClickable(false);
                                count++;
                            }
                        }
                    }
                }
            }
        }
    }

    private void goToEditPatientActivity() {

        Intent intent = new Intent(this, ProfessionalPatientEditInfo.class);
        startActivity(intent);
    }

    private  void goToTests(){
        Intent intent = new Intent(this, ProfessionalTestsHistoryActivity.class);
        startActivity(intent);
    }

    private void deletePatient() {

        confirmDeletePatient();
    }

    private void confirmDeletePatient() {

        Resources resources = this.getResources();
        String confirm = resources.getString(R.string.professional_patientInfo_confirmDeletePatient);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(confirm)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePatientFromDataBase();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deletePatientFromDataBase() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        String patientCode = String.valueOf(map.get("patient_numeric_code"));
        String patientUid = "";
        if (map.containsKey("patient_uid")) {
            patientUid = String.valueOf(map.get("patient_uid"));
        }

        databaseReference.child("Professional").child(mAuth.getUid()).child("Patients").child(patientCode).removeValue();
        databaseReference.child("PatientsNumericCodes").child(patientCode).removeValue();
        databaseReference.child("Patient").child(patientCode).removeValue();
        databaseReference.child("Patient").child(patientUid).removeValue();

        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameCurrentPatient, "");

        this.finish();
    }
}
