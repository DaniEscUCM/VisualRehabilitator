package com.macularehab.professional;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.IdentificationActivity;
import com.macularehab.R;
import com.macularehab.patient.Patient;
import com.macularehab.professional.patientList.PatientListAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        getPatientList();

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
    }

    public void updatePatientsList(List<Patient> patientList) {

        patientListAdapter.setPatientListData(patientList);
        patientListAdapter.notifyDataSetChanged();
    }

    //TODO Result

    private void getPatientList() {

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
                    //if (map != null) {
                        writeInternalStoragePatientList(map);
                        //createPatientList(map);
                    //}
                }
            }
        });
    }

    private void writeInternalStoragePatientList(HashMap<String, Object> map) {

        Gson gson = new Gson();
        String data = gson.toJson(map);
        try {
            File file = new File(getApplicationContext().getFilesDir(), filenameProfessionalPatientList);
            Toast.makeText(getApplicationContext(), getApplicationContext().getFilesDir().toString(), Toast.LENGTH_LONG).show();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            readInternalStorage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readInternalStorage() {

        try {
            File file = new File(getApplicationContext().getFilesDir(), filenameProfessionalPatientList);
            FileInputStream fileInputStream = new FileInputStream(file);
                    //openFileInput(filenameProfessionalPatientList);
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fileInputStream.read()) != -1) {
                temp.append((char) a);
            }

            String fin = temp.toString();
            fileInputStream.close();

            Gson gson = new Gson();
            HashMap<String, Object> map = gson.fromJson(fin, HashMap.class);
            createPatientList(map);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPatientList(HashMap<String, Object> map) {

        patientList = new ArrayList<Patient>();

        if (!map.isEmpty()) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                //HashMap<String, Object> hashMap = (HashMap<String, Object>) entry.getValue();
                LinkedTreeMap<String, Object> hashMap = (LinkedTreeMap<String, Object>) entry.getValue();

                Patient patient = new Patient();
                patient.setPatient_uid(entry.getKey());
                patient.setName((String) hashMap.get("name"));
                patient.setFirst_lastName((String) hashMap.get("first_lastName"));
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
                        //createPatientList(map);
                    }
                }
            }
        });
    }

    private void writeInternalStorageProfessionalInfo(HashMap<String, Object> map) {

        Gson gson = new Gson();
        String data = gson.toJson(map);
        try {
            File file = new File(getApplicationContext().getFilesDir(), filenameProfessionalInfo);
            //Toast.makeText(getApplicationContext(), getApplicationContext().getFilesDir().toString(), Toast.LENGTH_LONG).show();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            readInternalStorage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setProfessionalNameText() {

        String name = mAuth.getCurrentUser().getDisplayName();
        Log.w("Professional Name", name);
        if (!name.equals("")) {
            professional_name_text.setText(mAuth.getCurrentUser().getDisplayName());
        }
        else {
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

}
