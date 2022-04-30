package com.macularehab.professional.patientForm;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;
import com.macularehab.professional.patient.ProfessionalPatientInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfessionalPatientEditInfoDifficulties extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private ArrayList<Boolean> arrayList;
    private String patient_numeric_code;
    private String professional_uid;
    private final String filenameCurrentPatient = "CurrentPatient.json";
    private final String db_patients = "Patients";
    private final String db_professional = "Professional";

    private Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_create_new_patient_difficulties);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        resources = this.getResources();

        Button continue_button = findViewById(R.id.button_create_new_patient_difficulties_continue);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCheckBoxesClicked();
            }
        });

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

    private void getCheckBoxesClicked() {

        arrayList = new ArrayList<>(33);

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

        readAndWritePatient();
    }

    private void readAndWritePatient() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map =  readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        patient_numeric_code = map.get("patient_numeric_code").toString();
        professional_uid = map.get("professional_uid").toString();

        map.put("checkBox", arrayList);

        Gson gson = new Gson();
        String data = gson.toJson(map);

        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameCurrentPatient, data);

        databaseReference.child(db_professional).child(professional_uid).child(db_patients)
                .child(patient_numeric_code).setValue(map);

        returnToPatientInfoActivity();
    }

    private void returnToPatientInfoActivity() {

        Intent intent = new Intent(this, ProfessionalPatientInfo.class);
        startActivity(intent);
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
