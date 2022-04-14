package com.macularehab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.HashMap;

public class SecondExerciseDescriptionActivity extends AppCompatActivity {
    private static int num_seconds;
    private final String filenameCurrentUser = "CurrentPatient.json";
    private final String isFocus = "focusIsOn";
    private boolean isOn;
    //@SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_second_exercise_description);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_exerc);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });

        ImageButton button_play = (ImageButton) findViewById(R.id.button_play_ex);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_exercise(v);
            }
        });

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map= readInternalStorage.read(getApplicationContext(), filenameCurrentUser);

        Switch focus_switch = findViewById(R.id.focus_switch);
        focus_switch.setChecked((Boolean) map.get(isFocus));
        isOn=(Boolean) map.get(isFocus);
        focus_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
            HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentUser);
            isOn=!(Boolean)mapS.get(isFocus);
        });

        EditText seconds = (EditText) findViewById(R.id.seconds);
        /*seconds.setText("10"); //asi se pone a 10
        seconds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*//*
        seconds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                seconds.setText("10"); //asi se pone a 10
                System.out.println("Hola1");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Hola2");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String se = seconds.getText().toString();
                System.out.println("segundos: " + se);
                if (se.equals("seconds")) {
                    System.out.println("Hola, quieres estos segundos: " + se);

                } else {
                    int num_seconds = Integer.parseInt(se);
                    System.out.println("Hola, quieres estos segundos: " + se + "en int: " + num_seconds);
                }
            }
        });*/
        /*String num_seconds = seconds.getText().toString();
        System.out.println(num_seconds);
        int n = Integer.parseInt(num_seconds);
        System.out.println(n);*/
        //int num_seconds = Integer.parseInt(seconds.getText().toString().trim()); //.trim por si acaso hay espacios
        //System.out.println(num_seconds);
    }

    private void play_exercise(View v) {

        saveInfo();
        //
        EditText seconds = (EditText) findViewById(R.id.seconds);
        String se = seconds.getText().toString();
        num_seconds = 10;
        if (se.equals("")) {
            System.out.println("Hola, quieres estos segundos: " + se);

        } else {
            num_seconds = Integer.parseInt(se);
            System.out.println("Hola, quieres estos segundos: " + se + "en int: " + num_seconds);
        }

        finish();

        Intent i = new Intent( this, SecondExerciseActivity.class );
        startActivity(i);
    }

    public static int getNumSeconds(){
        return num_seconds;
    }

    public void Close(View view){
        saveInfo();
        finish();
    }

    private void saveInfo(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
        HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentUser);

        mapS.put(isFocus, isOn);

        Gson gson = new Gson();
        String data = gson.toJson(mapS);
        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameCurrentUser, data);
        databaseReference.child("Professional").child((String) mapS.get("professional_uid")).
                child("Patients").child((String) mapS.get("patient_numeric_code")).child(isFocus).setValue(isOn);
    }
}
