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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SecondExerciseDescriptionActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_second_exercise_description);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_exerc2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });

        ImageButton button_play = (ImageButton) findViewById(R.id.button_play_ex2);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_exercise(v);
            }
        });

        ImageButton button_settings = (ImageButton) findViewById(R.id.conf_second_exercise_button);
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting(v);
            }
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

    private void setting(View v) {
        Intent i = new Intent( this, SettingsActivity.class );
        startActivity(i);
    }

    private void play_exercise(View v) {
        //
        EditText seconds = (EditText) findViewById(R.id.seconds);
        String se = seconds.getText().toString();
        int num_seconds = 10;
        if (se.equals("")) {
            System.out.println("Hola, quieres estos segundos: " + se);

        } else {
            num_seconds = Integer.parseInt(se);
            System.out.println("Hola, quieres estos segundos: " + se + "en int: " + num_seconds);
        }

        Intent i = new Intent( this, SecondExerciseActivity.class );
        startActivity(i);
    }

    public void Close(View view){
        finish();
    }

}
