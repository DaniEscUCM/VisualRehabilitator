package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class SecondExerciseActivity extends AppCompatActivity {
    int counter = 0, counterCorrect, total = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        counterCorrect = 0;
        ImageButton button_dot = findViewById(R.id.dot_button);
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = move(counter);
            }
        });

        ImageButton button_setting = findViewById(R.id.first_exercise_settings);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings(v);
            }
        });

        ImageButton button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });
    }

    CountDownTimer cTimer = null;     //Declare timer

    void startTimer() {     //Start timer function
        //10s (10000 mili segundos) para hacer click en el circulo
        //Lo pongo a 3s para hacer pruebas
        cTimer = new CountDownTimer(3000, 10) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                move(++counter);
            }
        };
        cTimer.start();
    }

    private int move(int counter){
        startTimer();
        ImageButton button_dot = (ImageButton) findViewById(R.id.dot_button);
        Display disp_info = getWindowManager().getDefaultDisplay();
        Point point_info = new Point();
        disp_info.getSize(point_info);
        int x = (int) (Math.random() * (point_info.x - (2*button_dot.getWidth()))) + button_dot.getWidth() ;
        int y = (int) (Math.random() * (point_info.y - (2*button_dot.getHeight()))) + button_dot.getHeight();
        button_dot.getPivotX();
        button_dot.setX(x);
        button_dot.setY(y);
        ++counterCorrect;
        if(++counter == total) {
            finish();
        }
        return counter;
    }

    public void Close(View view){
        finish();
    }

    public void Settings(View view){
        Intent i = new Intent( this, SettingsActivity.class );
        startActivity(i);
    }
}