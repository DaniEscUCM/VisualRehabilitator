package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.HashMap;

public class FirstExerciseDescriptionActivity extends AppCompatActivity {

    private final String filenameCurrentUser = "CurrentPatient.json";
    private final String isFocus = "focusIsOn";
    private boolean isOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_first_exercise_description);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_exerc1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });

        ImageButton button_play = (ImageButton) findViewById(R.id.button_play_ex1);
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
    }


    private void play_exercise(View v) {
        saveInfo();
        Intent i = new Intent( this, FirstExerciseActivity.class );
        startActivity(i);
        finish();
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
