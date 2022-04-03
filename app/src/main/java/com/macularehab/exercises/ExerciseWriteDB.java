package com.macularehab.exercises;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class ExerciseWriteDB {

    private final String filenameCurrentUser = "CurrentPatient.json";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private int exercise_id;

    public ExerciseWriteDB(int exercise_id) {

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();

        this.exercise_id = exercise_id;
    }

    public void writeResultInDataBase(Context context, int correct, int failed, int notClicked) {

        ReadInternalStorage readIS = new ReadInternalStorage();
        HashMap<String, Object> patientHashMap = readIS.read(context, filenameCurrentUser);

        if (patientHashMap.containsKey("exercise")) {
            LinkedTreeMap<String, Object> exercise = (LinkedTreeMap<String, Object>) patientHashMap.get("exercise");
            ArrayList<LinkedTreeMap<String, Object>> exercisesList = (ArrayList<LinkedTreeMap<String, Object>>) exercise.get("exerciseInfoList");
            LinkedTreeMap<String, Object> exerciseTwo = exercisesList.get(exercise_id);
            ArrayList<ResultInfo> resultsList = new ArrayList<ResultInfo>();
            if (exerciseTwo.containsKey("resultsList")) {
                resultsList = (ArrayList<ResultInfo>) exerciseTwo.get("resultsList");
            }

            resultsList.add(new ResultInfo(correct, failed, notClicked));

            Double times_completed = (Double) exerciseTwo.get("times_completed");
            exerciseTwo.put("times_completed", times_completed + 1);

            exerciseTwo.put("resultsList", resultsList);
            exercisesList.set(exercise_id, exerciseTwo);
            exercise.put("exerciseInfoList", exercisesList);
            patientHashMap.put("exercise", exercise);
        }

        Gson gson = new Gson();
        String data = gson.toJson(patientHashMap);
        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(context, filenameCurrentUser, data);

        String professional_uid = String.valueOf(patientHashMap.get("professional_uid"));
        String patient_uid = String.valueOf(patientHashMap.get("patient_numeric_code"));

        databaseReference.child("Professional").child(professional_uid).child("Patients").child(patient_uid).setValue(patientHashMap);
    }
}
