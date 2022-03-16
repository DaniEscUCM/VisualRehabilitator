package com.macularehab.professional;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;
import com.macularehab.patient.Patient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class ProfessionalCreateNewPatient extends AppCompatActivity {

    private EditText input_patient_date;
    private EditText input_patient_name;
    private EditText input_patient_first_last_name;
    private EditText input_patient_second_last_name;
    private Spinner input_patient_gender;
    private EditText input_patient_date_of_birth;
    private EditText input_patient_diagnostic;
    private EditText input_patient_av;
    private EditText input_patient_cv;
    private EditText input_patient_observations;
    private Button button_continue;

    private String date;
    private String name;
    private String first_last_name;
    private String second_last_name;
    private String gender;
    private String date_of_birth;
    private String diagnostic;
    private String av_text;
    private float av;
    private String cv_text;
    private float cv;
    private String observations;

    private Patient patient;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private int numericCode;
    public final static String numericCodeString = "numericCodeString";
    private final String patientsWithNoAccount = "Patient";
    private long professionalNumericCode;
    private long patientNumericCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_create_new_patient);
        //setContentView(R.layout.activity_professional_new_patient_difficulties);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        getProfessionalInfo();

        input_patient_date = findViewById(R.id.input_create_patient_todays_date);
        input_patient_name = findViewById(R.id.input_create_patient_name);
        input_patient_first_last_name = findViewById(R.id.input_create_patient_first_last_name);
        input_patient_second_last_name = findViewById(R.id.input_create_patient_second_last_name);
        input_patient_gender = findViewById(R.id.spinner_input_create_patient_gender);
        input_patient_date_of_birth = findViewById(R.id.input_create_patient_date_of_birth);
        input_patient_diagnostic = findViewById(R.id.input_create_patient_diagnostic);
        input_patient_av = findViewById(R.id.input_create_patient_av);
        input_patient_cv = findViewById(R.id.input_create_patient_cv);
        input_patient_observations = findViewById(R.id.input_create_patient_observations);

        button_continue = findViewById(R.id.button_create_patient_continue);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonContinueClicked();
            }
        });

        setDate();
        //generateNumericCode();
    }

    private void setDate() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        input_patient_date.setText(formatter.format(date));

        Log.d("Date", "default date " + date.toString());
    }

    private void buttonContinueClicked() {

        readInputs();
        validateInputs();

        if (!validateInputs()){
            showAlertMissingData();
            return;
        }
        createPatient();
    }

    private void readInputs() {

        date = input_patient_date.getText().toString();
        Log.d("Date", "after read date " + date);
        name = input_patient_name.getText().toString();
        first_last_name = input_patient_first_last_name.getText().toString();
        second_last_name = input_patient_second_last_name.getText().toString();
        gender = input_patient_gender.getSelectedItem().toString();
        date_of_birth = input_patient_date_of_birth.getText().toString();
        diagnostic = input_patient_diagnostic.getText().toString();
        av_text = input_patient_av.getText().toString();
        cv_text = input_patient_cv.getText().toString();
    }

    //TODO utilizar string.xml y tambien falta comprobar que no exista un usuario con el mismo nombre
    private boolean validateInputs() {

        boolean all_correct = true;

        if (date.equals("")) {
            input_patient_date.setError("required");
            all_correct = false;
        }
        if (name.equals("")) {
            input_patient_name.setError("required");
            all_correct = false;
        }
        if (first_last_name.equals("")) {
            input_patient_first_last_name.setError("required");
            all_correct = false;
        }
        if (second_last_name.equals("")) {
            input_patient_second_last_name.setError("required");
            all_correct = false;
        }
        if (date_of_birth.equals("")) {
            input_patient_date_of_birth.setError("required");
            all_correct = false;
        }
        if (diagnostic.equals("")) {
            input_patient_diagnostic.setError("required");
            all_correct = false;
        }

        if (av == 0) {
            input_patient_av.setError("required");
            all_correct = false;
        } else av = Float.parseFloat(av_text);

        if (cv == 0) {
            input_patient_cv.setError("required");
            all_correct = false;
        } else cv = Float.parseFloat(cv_text);

        return all_correct;
    }

    private void showAlertMissingData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("There are some fields that ar not filled up")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createPatient() {

        patient = new Patient();
        patient.setDate(date);
        patient.setName(name);
        patient.setFirst_lastName(first_last_name);
        patient.setSecond_lastName(second_last_name);
        patient.setGender(gender);
        patient.setDate_of_birth(date_of_birth);
        patient.setDiagnostic(diagnostic);
        patient.setAv(av);
        patient.setCv(cv);
        patient.setObservations(observations);
        patient.setProfessional_name("Unknown");
        patient.setProfessional_uid(mAuth.getCurrentUser().getUid());

        addPatientToDataBase();
    }

    private void addPatientToDataBase() {

        databaseReference.child(patientsWithNoAccount)
                .child(String.valueOf(numericCode)).setValue(patient);

        Intent continue_to_checkBoxes = new Intent(this, ProfessionalCreateNewPatientDifficulties.class);
        continue_to_checkBoxes.putExtra(numericCodeString, numericCode);
        startActivity(continue_to_checkBoxes);
    }

    private void generateNumericCode() {

        numericCode = (int) (professionalNumericCode*100000+patientNumericCode*100);

        int min = 01;
        int max = 99;
        Random random = new Random();
        numericCode += random.nextInt(max-min) + min;
        checkNumericCode();
    }

    private void checkNumericCode() {

        databaseReference.child(patientsWithNoAccount).child(String.valueOf(numericCode))
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    String value = String.valueOf(task.getResult().getValue());
                    if (!value.equals("null")) {
                        generateNumericCode();
                    }
                }
            }
        });
    }

    private void getProfessionalInfo() {

        databaseReference.child("Professional").child(String.valueOf(mAuth.getCurrentUser().getUid()))
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    Map<String, Object> mapa = (Map<String, Object>) task.getResult().getValue();
                    if (mapa != null) {
                        professionalNumericCode = (long) mapa.get("professionalNumericCode");
                        patientNumericCode = (long) mapa.get("numberOfPatients");
                        patientNumericCode++;
                        generateNumericCode();
                    }
                }
            }
        });
    }
}
