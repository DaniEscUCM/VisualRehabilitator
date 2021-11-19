package com.macularehab;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.macularehab.login.MainLogin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        ImageButton button1 = (ImageButton) findViewById(R.id.imageButton_settings);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings(v);
            }
        });

        Button button2 = (Button) findViewById(R.id.button_exercises);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exercises(v);
            }
        });

        //PATIENT
        Button dataButton = (Button) findViewById(R.id.dataButton);

        askFirstTime();
    }

    public void Settings(View view){
        Intent i = new Intent( this, SettingsActivity.class );
        startActivity(i);
    }

    public void Exercises(View view){
        Intent i = new Intent( this, ExercisesActivity.class );
        startActivity(i);
    }

    //PATIENT
    public void onClickData(View view) {

        /*Intent i = new Intent(this, PatientLogin.class);
        startActivity(i);*/

        Intent i = new Intent(getApplicationContext(), MainLogin.class);
        startActivity(i);
    }

    private void askFirstTime() {

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {

            showFirstStart();
        }
    }

    private void showFirstStart() {

        new AlertDialog.Builder(this)
                .setTitle("First Time Dialog")
                .setMessage("This only be show once")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
}