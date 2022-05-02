package com.macularehab.exercises;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.HashMap;

public class SaveFocusInfo {

    private final String filenameCurrentUser = "CurrentPatient.json";

    private Context context;
    private final String db_focus = "focusIsOn";
    private boolean isOn;

    public SaveFocusInfo(Context context, boolean isOn) {

        this.context = context;
        this.isOn = isOn;

        safeInfo();
    }

    private void safeInfo() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorageS.read(context, filenameCurrentUser);

        map.put(db_focus, isOn);

        Gson gson = new Gson();
        String data = gson.toJson(map);

        String professional_uid = String.valueOf(map.get("professional_uid"));
        String patient_code = String.valueOf(map.get("patient_numeric_code"));

        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(context, filenameCurrentUser, data);
        /*databaseReference.child("Professional").child(professional_uid).
                child("Patients").child(patient_code).child(db_focus).setValue(isOn);*/
    }
}
