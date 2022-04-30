package com.macularehab.professional.patientForm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.patient.Patient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
    private String professionalName;

    private int numericCode;
    public final static String numericCodeString = "numericCodeString";
    public final static String patientInfoExtra = "patientInfoExtra";
    private final String db_patient = "Patient";
    private final String db_professional = "Professional";
    private long professionalNumericCode;
    private long patientNumericCode;

    private Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_create_new_patient);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        getProfessionalInfo();

        ImageButton goBackButton = findViewById(R.id.professional_patient_patientForm_go_back_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfessionalCreateNewPatient.this.finish();
            }
        });

        resources = ProfessionalCreateNewPatient.this.getResources();

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
                confirmContinueDialog();
            }
        });

        setDate();
        getProfessionalName();

        setUiListener();
    }

    private void setUiListener() {

        View decorView = getWindow().getDecorView();

        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 2000ms
                                    hideNavigationAndStatusBar();
                                }
                            }, 2000);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideNavigationAndStatusBar();
    }

    private void setDate() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        input_patient_date.setText(formatter.format(date));

        Log.d("Date", "default date " + date.toString());
    }

    private void getProfessionalName() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), "ProfessionalInfo.json");

        professionalName = map.get("name").toString();
    }

    private void buttonContinueClicked() {

        readInputs();
        //validateInputs();

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
        observations = input_patient_observations.getText().toString();
    }

    private boolean validateInputs() {

        boolean all_correct = true;

        String required = resources.getString(R.string.error_required_field);

        if (date.equals("")) {
            input_patient_date.setError(required);
            all_correct = false;
        }
        if (name.equals("")) {
            input_patient_name.setError(required);
            all_correct = false;
        }
        if (first_last_name.equals("")) {
            input_patient_first_last_name.setError(required);
            all_correct = false;
        }
        if (second_last_name.equals("")) {
            input_patient_second_last_name.setError(required);
            all_correct = false;
        }
        if (date_of_birth.equals("")) {
            input_patient_date_of_birth.setError(required);
            all_correct = false;
        }
        if (diagnostic.equals("")) {
            input_patient_diagnostic.setError(required);
            all_correct = false;
        }

        if (av_text.equals("")) {
            input_patient_av.setError(required);
            all_correct = false;
        } else av = Float.parseFloat(av_text);

        if (cv_text.equals("")) {
            input_patient_cv.setError(required);
            all_correct = false;
        } else cv = Float.parseFloat(cv_text);

        return all_correct;
    }

    private void showAlertMissingData() {

        String st_error = resources.getString(R.string.error_fields_empty);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(st_error)
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
        patient.setPatient_numeric_code(String.valueOf(numericCode));
        String professionalName = mAuth.getCurrentUser().getDisplayName();
        if (professionalName == null) {
            professionalName = this.professionalName;
        }
        else if (professionalName.equals("")) {
            professionalName = this.professionalName;
        }
        patient.setProfessional_name(professionalName);
        patient.setProfessional_uid(mAuth.getCurrentUser().getUid());

        addPatientToDataBase();
    }

    private void addPatientToDataBase() {

        Gson gson = new Gson();
        String patientInfo = gson.toJson(patient);

        Intent continue_to_checkBoxes = new Intent(this, ProfessionalCreateNewPatientDifficulties.class);
        continue_to_checkBoxes.putExtra(patientInfoExtra, patientInfo);
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

        databaseReference.child(db_patient).child(String.valueOf(numericCode))
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

        databaseReference.child(db_professional).child(String.valueOf(mAuth.getCurrentUser().getUid()))
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    Map<String, Object> professionalInfo = (Map<String, Object>) task.getResult().getValue();
                    if (professionalInfo != null) {
                        professionalNumericCode = (long) professionalInfo.get("professionalNumericCode");
                        patientNumericCode = (long) professionalInfo.get("numberOfPatients");
                        patientNumericCode++;
                        generateNumericCode();
                    }
                }
            }
        });
    }

    private void hideNavigationAndStatusBar() {

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        decorView.setSystemUiVisibility(uiOptions);
    }

    private void confirmContinueDialog() {

        new MaterialAlertDialogBuilder(this)
                .setMessage(resources.getString(R.string.professional_patientForm_continue_confirmationMessage))
                .setPositiveButton(resources.getString(R.string.professional_patientForm_continue_confirmationMessage_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonContinueClicked();
                    }
                })
                .setNegativeButton(resources.getString(R.string.professional_patientForm_continue_confirmationMessage_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
        .show();
    }
}
