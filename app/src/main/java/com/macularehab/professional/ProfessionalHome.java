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
import com.macularehab.R;
import com.macularehab.patient.Patient;
import com.macularehab.professional.patientList.PatientListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessionalHome extends AppCompatActivity {

    private Button createNewPatientButton;

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

                    if (map != null) {
                        createPatientList(map);
                    }
                }
            }
        });
    }

    private void createPatientList(HashMap<String, Object> map) {

        patientList = new ArrayList<Patient>();

        if (!map.isEmpty()) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                HashMap<String, Object> hashMap = (HashMap<String, Object>) entry.getValue();

                Patient patient = new Patient();
                patient.setPatient_uid(entry.getKey());
                patient.setName((String) hashMap.get("name"));
                patient.setFirst_lastName((String) hashMap.get("first_lastName"));
                //patient.setSecond_lastName((String) hashMap.get("second_lastName"));

                patientList.add(patient);
            }
        }

        updatePatientsList(patientList);
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

}
