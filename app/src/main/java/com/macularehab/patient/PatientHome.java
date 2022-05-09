package com.macularehab.patient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.macularehab.DistanceExercisesActivity;
import com.macularehab.ExercisesActivity;
import com.macularehab.IdentificationActivity;
import com.macularehab.R;
import com.macularehab.SettingsActivity;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.UploadPatientData;
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
    private final String SHARED_PREF_FILE = "com.macularehab.sharedprefs.user_is_logged";
    private final String SHARED_PREF_PATIENT_USER_LOGGED_KEY = "patient_user_logged";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_home_choose_data_exercise);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        mAuth.updateCurrentUser(mAuth.getCurrentUser());

        patientUID = mAuth.getUid();

        patient_username_textView = findViewById(R.id.patient_home_patient_name_textView);
        String[] patient_username = mAuth.getCurrentUser().getEmail().split("@");
        patient_username_textView.setText(patient_username[0]);


        ImageButton goBackButton = findViewById(R.id.patient_home_back_button);
        goBackButton.setVisibility(View.GONE);

        ImageButton settingsButton = findViewById(R.id.settingButton);
        settingsButton.setOnClickListener(v -> gotToSettings());
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
                askLogOutConfirmation();
            }
        });

        patientUID = mAuth.getUid();
        getProfessionalUID();

        //readFocusSwitch();

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
    protected void onRestart() {
        super.onRestart();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNavigationAndStatusBar();

        /*patientUID = mAuth.getUid();
        getProfessionalUID();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        //readFocusSwitch();
        hideNavigationAndStatusBar();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        askLogOutConfirmation();
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

    private void gotToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
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

                    if (hasInternetConnection()) {
                        writeInternalStoragePatientList(map);
                    }
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

    private void goToPatientData() {

        Intent intent = new Intent(this, PatientDataInfoActivity.class);
        startActivity(intent);
    }

    private void goToExercises() {

        Intent intent = new Intent(this, DistanceExercisesActivity.class);
        startActivity(intent);
    }

    private void uploadPatientData() {
        UploadPatientData uploadPatientData = new UploadPatientData();
        uploadPatientData.upload(getApplicationContext(), filenameCurrentPatient);
    }

    private void hideNavigationAndStatusBar() {

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        decorView.setSystemUiVisibility(uiOptions);
    }

    private void logOut() {

        if (hasInternetConnection()) {
            FirebaseAuth.getInstance().signOut();
        }

        logOutAux();

        patient_username_textView.setText("Patient");

        Intent mainActivity = new Intent(this, IdentificationActivity.class);
        startActivity(mainActivity);
    }

    /**
     * Auxiliary function to change the SharedPreference
     * boolean that indicates if a patient is logged, to
     * false
     *
     */
    private void logOutAux() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SHARED_PREF_PATIENT_USER_LOGGED_KEY, false);
        editor.apply();
    }

    private boolean hasInternetConnection() {

        boolean isConnected = false;
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        return isConnected;
    }

    private void askLogOutConfirmation() {

        Resources resources = this.getResources();
        String question = resources.getString(R.string.patient_home_logOut_confirmation_message);
        String affirmative = resources.getString(R.string.patient_home_logOut_confirmation_message_yes_logOut);
        String negative = resources.getString(R.string.patient_home_logOut_confirmation_message_no_cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(question)
                .setPositiveButton(affirmative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logOut();
                    }
                })
                .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
