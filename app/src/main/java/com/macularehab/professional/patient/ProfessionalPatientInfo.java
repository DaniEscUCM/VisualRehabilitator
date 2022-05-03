package com.macularehab.professional.patient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.R;
import com.macularehab.exercises.ChooseExerciseActivity;
import com.macularehab.exercises.Exercise;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;
import com.macularehab.professional.ProfessionalHome;
import com.macularehab.professional.patientForm.ProfessionalPatientEditInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ProfessionalPatientInfo extends AppCompatActivity {

    private Button editPatientButton;
    private Button professionalProfileButton;
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
    private ProgressBar progressBar;

    private final String filenameCurrentPatient = "CurrentPatient.json";
    private LinearLayout dataManagementButtons;

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
        progressBar = findViewById(R.id.professional_patient_info_exercisesHistory_progressBar);

        ImageButton backButton = findViewById(R.id.professional_patient_info_go_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
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

        Button exercisesHistoryButton = findViewById(R.id.professional_patient_info_exercisesHistory_button);
        exercisesHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToExercisesHistory();
            }
        });

        //TODO
        professionalProfileButton = findViewById(R.id.professional_patient_info_professionalProfile_button);
        professionalProfileButton.setVisibility(View.GONE);

        dataManagementButtons = findViewById(R.id.professional_patient_info_dataManagement_linearLayout);
        dataManagementButtons.setVisibility(View.GONE);

        fillFields();

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

        LinkedTreeMap<String, Object> exercise = (LinkedTreeMap<String, Object>) map.get("exercise");
        ArrayList<LinkedTreeMap<String, Object>> exercisesList = (ArrayList<LinkedTreeMap<String, Object>>) exercise.get("exerciseInfoList");

        int num_exercises = exercisesList.size();
        Double num_completed1 = (Double) exercise.get("exercises_completed");

        int num_completed = num_completed1.intValue();
        int progress = num_completed*100/num_exercises;

        if (progress > 100) {
            progress = 100;
        }

        Drawable progressDrawable = progressBar.getProgressDrawable();

        progressBar.setProgress(progress);
        if (progress < 20) {
            progressDrawable.setColorFilter(this.getResources().getColor(R.color.orange_dark), PorterDuff.Mode.SRC_IN);
        }
        else if (progress < 40) {
            progressDrawable.setColorFilter(this.getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
        }
        else if (progress < 55) {
            progressDrawable.setColorFilter(this.getResources().getColor(R.color.blue_light), PorterDuff.Mode.SRC_IN);
        }
        else if (progress < 65) {
            progressDrawable.setColorFilter(this.getResources().getColor(R.color.blue_dark), PorterDuff.Mode.SRC_IN);
        }
        else if (progress < 80) {
            progressDrawable.setColorFilter(this.getResources().getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
        }
        else {
            progressDrawable.setColorFilter(this.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
        }
        progressBar.setProgressDrawable(progressDrawable);

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
        String yesText = resources.getString(R.string.professional_patientInfo_delete_yes);
        String notText = resources.getString(R.string.professional_patientInfo_delete_no);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(confirm)
                .setNegativeButton(notText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(yesText, new DialogInterface.OnClickListener() {
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

        File file = new File(filenameCurrentPatient);
        file.delete();

        Intent intent = new Intent(this, ProfessionalHome.class);
        startActivity(intent);
        finish();
    }


    private void close(){
        Intent intent = new Intent(this, ProfessionalPatientHome.class);
        startActivity(intent);
    }

    private void goToExercisesHistory() {

        Intent intent = new Intent(this, ChooseExerciseActivity.class);
        startActivity(intent);
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
}
