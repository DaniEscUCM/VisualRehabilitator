package com.macularehab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
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

public class SecondExerciseDescriptionActivity extends AppCompatActivity {
    private static int num_seconds;

    //@SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_second_exercise_description);

        ImageButton button = findViewById(R.id.imageButton_back_exerc);
        button.setOnClickListener(v -> Close(v));

        ImageButton button_play =  findViewById(R.id.button_play_ex);
        button_play.setOnClickListener(v -> play_exercise(v));

        ImageButton settingsButton = findViewById(R.id.settingButton);
        settingsButton.setOnClickListener(v -> gotToSettings());

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

        setUiListener();
    }

    private void setUiListener() {

        View decorView = getWindow().getDecorView();

        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 2000ms
                                    hideNavigationAndStatusBar();
                                }
                            }, 2000);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideNavigationAndStatusBar();
    }

    private void gotToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void play_exercise(View v) {
        EditText seconds = (EditText) findViewById(R.id.seconds);
        String se = seconds.getText().toString();
        Resources res = getResources();
        String default_time = res.getString(R.string.num_seconds);
        num_seconds = Integer.parseInt(default_time);
        if (!se.equals("")) {
            num_seconds = Integer.parseInt(se);
        }

        finish();

        Intent i = new Intent( this, SecondExerciseActivity.class );
        startActivity(i);
    }

    public static int getNumSeconds(){
        return num_seconds;
    }

    public void Close(View view){
        finish();
    }


    private void hideNavigationAndStatusBar() {

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        decorView.setSystemUiVisibility(uiOptions);
    }
}
