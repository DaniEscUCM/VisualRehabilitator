package com.macularehab.internalStorage;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class UploadPatientData {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public UploadPatientData() {

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
    }

    public void upload(Context context, String filename) {

        HashMap<String, Object> map = new HashMap<>();

        try {

            File file = new File(context.getFilesDir(), filename);
            FileInputStream fileInputStream = new FileInputStream(file);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) { //API 19
                inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1);
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);

            int character;
            StringBuilder stringBuilder = new StringBuilder();
            while ((character = reader.read()) != -1) {
                stringBuilder.append((char) character);
            }

            String fin = stringBuilder.toString();
            fileInputStream.close();

            Gson gson = new Gson();
            map = gson.fromJson(fin, HashMap.class);

            String patient_numeric_code = "";
            String professional_uid = "";
            if (map.containsKey("patient_numeric_code")) {
                patient_numeric_code = (String) map.get("patient_numeric_code");
            }
            if (map.containsKey("professional_uid")) {
                professional_uid = (String) map.get("professional_uid");
            }

            if (!professional_uid.equals("") && !patient_numeric_code.equals("")) {
                databaseReference.child("Professional").child(professional_uid).child("Patients").child(patient_numeric_code).setValue(map);
            }

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
