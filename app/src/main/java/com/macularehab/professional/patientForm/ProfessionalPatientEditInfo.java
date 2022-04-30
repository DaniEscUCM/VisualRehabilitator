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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.HashMap;

public class ProfessionalPatientEditInfo extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private final String db_patients = "Patients";
    private final String db_professional = "Professional";

    private String professional_uid;
    private String patient_numeric_code;
    private HashMap<String, Object> map;

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

    private final String filenameCurrentPatient = "CurrentPatient.json";

    private TextView newPatientTextView;
    private TextView patientNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_create_new_patient);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        newPatientTextView = findViewById(R.id.professional_patient_form_newPatient_textView);
        Resources resources = this.getResources();
        newPatientTextView.setText(resources.getString(R.string.professional_patientForm_patient));

        ImageButton goBackButton = findViewById(R.id.professional_patient_patientForm_go_back_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfessionalPatientEditInfo.this.finish();
            }
        });

        patientNameTextView = findViewById(R.id.professional_patient_form_patientName_textView);

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
                continueToDifficulties();
            }
        });

        writeFields();

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

    private void writeFields() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        patientNameTextView.setText(map.get("name").toString());

        input_patient_date.setText(map.get("date").toString());
        input_patient_name.setText(map.get("name").toString());
        input_patient_first_last_name.setText(map.get("first_lastName").toString());
        input_patient_second_last_name.setText(map.get("second_lastName").toString());
        input_patient_date_of_birth.setText(map.get("date_of_birth").toString());
        input_patient_diagnostic.setText(map.get("diagnostic").toString());
        input_patient_av.setText(map.get("av").toString());
        input_patient_cv.setText(map.get("cv").toString());
        input_patient_observations.setText(map.get("observations").toString());

        professional_uid = map.get("professional_uid").toString();
        patient_numeric_code = map.get("patient_numeric_code").toString();
    }

    private void continueToDifficulties() {

        readInputs();

        if (!validateInputs()){
            showAlertMissingData();
            return;
        }

        editPatient();

        Resources resources = this.getResources();
        newPatientTextView.setText(resources.getString(R.string.professional_patientForm_new_patient));

        Intent intent = new Intent(this, ProfessionalPatientEditInfoDifficulties.class);
        startActivity(intent);
    }

    private void readInputs() {

        date = input_patient_date.getText().toString();
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

        if (av_text.equals("")) {
            input_patient_av.setError("required");
            all_correct = false;
        } else av = Float.parseFloat(av_text);

        if (cv_text.equals("")) {
            input_patient_cv.setError("required");
            all_correct = false;
        } else cv = Float.parseFloat(cv_text);

        return all_correct;
    }

    private void editPatient() {

        map.put("date", date);
        map.put("name", name);
        map.put("first_lastName", first_last_name);
        map.put("second_lastName", second_last_name);
        map.put("date_of_birth", date_of_birth);
        map.put("diagnostic", diagnostic);
        map.put("av", av_text);
        map.put("cv", cv_text);
        map.put("observations", observations);

        Gson gson = new Gson();
        String data = gson.toJson(map);

        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameCurrentPatient, data);

        databaseReference.child(db_professional).child(professional_uid).child(db_patients)
                .child(patient_numeric_code).setValue(map);
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
}
