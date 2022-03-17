package com.macularehab.professional.patientList;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.patient.Patient;

import java.util.List;
import java.util.Map;

public class PatientListLoader extends AsyncTaskLoader<List<Patient>> {

    private Context context;
    private String professional_uid;
    private final String db_professional = "Professional";
    private final String db_patients = "Patients";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private boolean done;

    public PatientListLoader(@NonNull Context context, String professional_uid) {
        super(context);
        this.context = context;
        this.professional_uid = professional_uid;

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        done = false;
    }

    @Nullable
    @Override
    public List<Patient> loadInBackground() {
        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    private List<Patient> getPatientList() {

        databaseReference.child(db_professional).child(professional_uid).child(db_patients)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    Map<String, Object> mapa = (Map<String, Object>) task.getResult().getValue();

                }
            }
        });

        while(!done);
        done = false;c
    }
}
