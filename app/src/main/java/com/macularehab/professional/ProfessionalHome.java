package com.macularehab.professional;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.IdentificationActivity;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;
import com.macularehab.patient.Patient;
import com.macularehab.professional.account.ChangePassword;
import com.macularehab.professional.patientForm.ProfessionalCreateNewPatient;
import com.macularehab.professional.patientList.PatientListAdapter;
import com.macularehab.profiles.ProfessionalEditProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessionalHome extends AppCompatActivity {

    private Button createNewPatientButton;
    private Button logoutButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private String professional_uid;
    private final String db_professional = "Professional";
    private final String db_patients = "Patients";

    private ArrayList<Patient> patientList;
    private RecyclerView recyclerView;
    private PatientListAdapter patientListAdapter;
    private TextView professional_name_text;
    private SearchView searchView;

    private LottieAnimationView loading_imageView;
    private ProgressDialog progressDialog;
    private View layout_loading;

    //Store Data
    private final String filenameProfessionalPatientList = "ProfessionalPatientList.json";
    private final String filenameProfessionalInfo = "ProfessionalInfo.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_home);

        Log.w("Here" , "Inicio");

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        professional_uid = mAuth.getUid();
        Log.w("Here" , "Despues de getUid");

        professional_name_text = findViewById(R.id.text_professional_home_professional_name_text);
        setProfessionalNameText();

        recyclerView = findViewById(R.id.professional_patientList_recyclerView);
        patientListAdapter = new PatientListAdapter(this, new ArrayList<Patient>());
        recyclerView.setAdapter(patientListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.w("Here" , "A ver aqui");
        //getPatientList();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) { //API 19

            ConstraintLayout constraintLayout = findViewById(R.id.professional_home_loading_image_constrainLayout);
            layout_loading = getLayoutInflater().inflate(R.layout.layout_loading, constraintLayout, false);

            constraintLayout.addView(layout_loading);

            loading_imageView = findViewById(R.id.general_loading_image);

            loading_imageView.setBottom(R.id.linearLayout5);
            loading_imageView.setTop(R.id.linearLayout5);
            loading_imageView.setLeft(R.id.linearLayout5);
            loading_imageView.setRight(R.id.linearLayout5);
        }
        else {

            progressDialog = new ProgressDialog(this);
        }

        searchView = (SearchView) findViewById(R.id.professional_home_search_patient_searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                patientListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        createNewPatientButton = findViewById(R.id.professional_home_create_new_patient_button);
        createNewPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityCreatePatient();
            }
        });

        logoutButton = findViewById(R.id.professional_home_logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutProfessional();
            }
        });

        Button changePasswordButton = findViewById(R.id.professional_home_changePassword_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangePasswordActivity();
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

        professional_uid = mAuth.getUid();
        getPatientList();
        stopLoadingImage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stopLoadingImage();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopLoadingImage();
        hideNavigationAndStatusBar();
    }

    public void updatePatientsList(List<Patient> patientList) {

        stopLoadingImage();

        patientListAdapter.setPatientListData(patientList);
        patientListAdapter.notifyDataSetChanged();
    }

    //TODO Result

    private void getPatientList() {

        showLoadingImage();

        databaseReference.child(db_professional).child(professional_uid).child(db_patients)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    HashMap<String, Object> map = (HashMap<String, Object>) task.getResult().getValue();
                    Log.w("Here", "Aqui estamos seeee");

                    if (map == null) {
                        map = new HashMap<>();
                    }
                    writeInternalStoragePatientList(map);
                }
            }
        });
    }

    private void writeInternalStoragePatientList(HashMap<String, Object> map) {

        Gson gson = new Gson();
        String data = gson.toJson(map);

        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameProfessionalPatientList, data);
        readInternalStorage();
    }

    private void readInternalStorage() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameProfessionalPatientList);
        createPatientList(map);
    }

    private void createPatientList(HashMap<String, Object> map) {

        patientList = new ArrayList<Patient>();

        if (!map.isEmpty()) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                LinkedTreeMap<String, Object> hashMap = (LinkedTreeMap<String, Object>) entry.getValue();

                Patient patient = new Patient();
                /*patient.setPatient_uid(entry.getKey());
                patient.setName((String) hashMap.get("name"));
                patient.setFirst_lastName((String) hashMap.get("first_lastName"));
                if (hashMap.containsKey("date_last_test")) {
                    patient.setDate_last_test(hashMap.get("date_last_test").toString());
                }*/
                Gson gson = new Gson();
                String data = gson.toJson(hashMap);
                patient = gson.fromJson(data, Patient.class);
                patient.setPatient_uid(entry.getKey());
                //patient.setSecond_lastName((String) hashMap.get("second_lastName"));

                patientList.add(patient);
            }
        }

        updatePatientsList(patientList);

        getProfessionalInfo();
    }

    private void getProfessionalInfo() {

        databaseReference.child(db_professional).child(professional_uid)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    HashMap<String, Object> map = (HashMap<String, Object>) task.getResult().getValue();
                    Log.w("Here", "Aqui estamos seeee");

                    if (map != null) {
                        writeInternalStorageProfessionalInfo(map);
                    }
                }
            }
        });
    }

    private void writeInternalStorageProfessionalInfo(HashMap<String, Object> map) {

        Gson gson = new Gson();
        String data = gson.toJson(map);

        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameProfessionalInfo, data);
    }

    private void setProfessionalNameText() {

        if (mAuth.getCurrentUser() != null) {

            String name = mAuth.getCurrentUser().getDisplayName();

            if (name == null) {
                getProfessionalNameFromDB();
            } else if (!name.equals("")) {
                professional_name_text.setText(mAuth.getCurrentUser().getDisplayName());
            } else {
                getProfessionalNameFromDB();
            }
        }
        else {
            getProfessionalNameFromDB();
        }
    }

    private void getProfessionalNameFromDB() {

        databaseReference.child("Professional").child(mAuth.getCurrentUser().getUid()).child("name")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    Log.w("firebase", value);
                    professional_name_text.setText(value);
                }
            }
        });
    }

    private void startActivityCreatePatient() {
        Intent new_patient_activity = new Intent(this, ProfessionalCreateNewPatient.class);
        startActivity(new_patient_activity);
    }

    private void logOutProfessional() {

        mAuth.signOut();

        Intent mainActivity = new Intent(this, IdentificationActivity.class);
        startActivity(mainActivity);
    }

    private void goToChangePasswordActivity() {

        //Intent changePasswordIntent = new Intent(this, ChangePassword.class);
        Intent changePasswordIntent = new Intent(this, ProfessionalEditProfile.class);
        startActivity(changePasswordIntent);
    }

    private void showLoadingImage() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) { //API 19
            loading_imageView.setVisibility(View.VISIBLE);
            loading_imageView.setAnimation(R.raw.loading_rainbow);
            loading_imageView.playAnimation();
            loading_imageView.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    loading_imageView.playAnimation();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        else {

            //TODO for spanish
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
        }
    }

    private void stopLoadingImage() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) { //API 19
            loading_imageView.cancelAnimation();
            loading_imageView.setVisibility(View.INVISIBLE);
            loading_imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
        else {
            progressDialog.dismiss();
        }
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
