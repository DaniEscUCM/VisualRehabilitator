package com.macularehab.internalStorage;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;

public class DownloadPatientData {

    private final String filename = "CurrentPatient.json";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Context context;
    private String patient_numeric_code;
    private String professional_uid;

    public DownloadPatientData(Context context) {

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();

        this.context = context;

        readPatientData();
    }

    private void readPatientData() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(context, filename);

        if (map != null) {

            if (map.containsKey("patient_numeric_code")) {
                patient_numeric_code = (String) map.get("patient_numeric_code");
            }
            if (map.containsKey("professional_uid")) {
                professional_uid = (String) map.get("professional_uid");
            }
        }

        getDataFromDataBase();
    }

    private void getDataFromDataBase() {

        databaseReference.child("Professional").child(professional_uid).child("Patients").child(patient_numeric_code)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {

                    HashMap<String, Object> map = (HashMap<String, Object>) task.getResult().getValue();

                    if (map != null) {

                        writeDataFromDataBase(map);
                    }
                }
            }
        });
    }

    private void writeDataFromDataBase(HashMap<String, Object> map) {

        Gson gson = new Gson();
        String data = gson.toJson(map);

        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(context, filename, data);
    }
}
