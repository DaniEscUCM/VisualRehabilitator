package com.macularehab.patient.data;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.R;
import com.macularehab.exercises.ChooseExerciseActivity;
import com.macularehab.internalStorage.DownloadPatientData;
import com.macularehab.internalStorage.UploadPatientData;
import com.macularehab.patient.PatientHome;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.patient.professional.ProfessionalProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class PatientDataInfoActivity extends AppCompatActivity {

    private TextView textView_nameTitle;
    private TextView textView_lastTest;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_patient_info);

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

        Button patientHistoryTestButton = findViewById(R.id.button_tests);
        patientHistoryTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTests();
            }
        });

        Button patientExercisesHistoryButton = findViewById(R.id.professional_patient_info_exercisesHistory_button);
        patientExercisesHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToExercisesHistory();
            }
        });

        Button professionalProfileButton = findViewById(R.id.professional_patient_info_professionalProfile_button);
        professionalProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfessionalProfile();
            }
        });

        Button uploadDataButton = findViewById(R.id.professional_patient_info_dataManagement_uploadData_button);
        uploadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPatientData();
            }
        });

        Button downloadButton = findViewById(R.id.professional_patient_info_dataManagement_downloadData_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPatientData();
            }
        });

        setInvisibleButtons();

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

    private void close(){

        Intent intent = new Intent(this, PatientHome.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setInvisibleButtons();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setInvisibleButtons();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInvisibleButtons();
        hideNavigationAndStatusBar();
    }

    private void setInvisibleButtons() {

        //LinearLayout editAndDeleteLinearLayot = findViewById(R.id.professional_patient_info_editAndDelete_linearLayout);
        //editAndDeleteLinearLayot.setVisibility(View.INVISIBLE);

        LinearLayout editAndDeleteLinearLayot = findViewById(R.id.linearLayoutPatientInfo);
        LinearLayout deleteView = findViewById(R.id.professional_patient_info_editAndDelete_linearLayout);
        editAndDeleteLinearLayot.removeView(deleteView);
    }

    private void fillFields() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        String date = map.get("date").toString();
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

    private  void goToTests() {

        Intent intent = new Intent(this, PatientTestHistoryActivity.class);
        startActivity(intent);
    }

    private void goToExercisesHistory() {

        Intent intent = new Intent(this, ChooseExerciseActivity.class);
        startActivity(intent);
    }

    private void goToProfessionalProfile() {

        Intent intent = new Intent(this, ProfessionalProfile.class);
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

    private void uploadPatientData() {

        UploadPatientData uploadPatientData = new UploadPatientData();
        uploadPatientData.upload(getApplicationContext(), filenameCurrentPatient);
    }

    private void downloadPatientData() {

        new DownloadPatientData(getApplicationContext());
    }
}
