package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.exercises.SaveFocusInfo;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.HashMap;

public class EighthExerciseDescriptionActivity extends AppCompatActivity {
    private static int num_seconds;
    private final String filenameCurrentUser = "CurrentPatient.json";
    private final String isFocus = "focusIsOn";
    private boolean isOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_eighth_exercise_description);

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

    }


    private void play_exercise(View v) {

        saveInfo();
        EditText seconds = (EditText) findViewById(R.id.seconds);
        String se = seconds.getText().toString();
        num_seconds = 10;
        if (!se.equals("")) {
            num_seconds = Integer.parseInt(se);
        }

        finish();

        Intent i = new Intent( this, EighthExerciseActivity.class );
        startActivity(i);
    }

    public static int getNumSeconds(){
        return num_seconds;
    }

    public void Close(View view){
        finish();
    }

    private void saveInfo() {

        new SaveFocusInfo(getApplicationContext(), isOn);
    }
}
