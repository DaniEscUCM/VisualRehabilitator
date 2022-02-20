package com.macularehab;

import static java.util.Collections.shuffle;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class SecondExerciseActivity extends AppCompatActivity {
    protected int counter = 0, counterCorrect, counterUpL = 0, counterUpR = 0,counterDownL = 0,counterDownR = 0;
    protected final int total = 7;
    List<Double> list = Arrays.asList(0.05, 2.75, 4.25);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        counterCorrect = 0;
        //shuffle(list);
        //Collections.shuffle(list);
        ImageButton button_dot = findViewById(R.id.dot_button);
        move(counter, list.get(1));
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = move(counter, list.get(1)); //list.get(counter%list.size())
            }
        });

        ImageButton button_setting = findViewById(R.id.second_exercise_settings);
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
                move(++counter,list.get(0));
            }
        };
        cTimer.start();
    }

    void startTimerInit() {     //Start timer function
        cTimer = new CountDownTimer(1000, 1) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                move(++counter,list.get(0));
            }
        };
        cTimer.start();
    }

    private int init(int counter){
        startTimerInit();
        ImageButton button_dot = (ImageButton) findViewById(R.id.dot_button);
        Display disp_info = getWindowManager().getDefaultDisplay();
        Point point_info = new Point();
        disp_info.getSize(point_info);
        int x = (int) (Math.random() * (point_info.x - (2*button_dot.getWidth()))) + button_dot.getWidth() ;
        int y = (int) (Math.random() * (point_info.y - (2*button_dot.getHeight()))) + button_dot.getHeight();
        button_dot.getPivotX();
        button_dot.setX(x);
        button_dot.setY(y);
        return counter;
    }

    private int move(int counter, Double position){
        startTimer();
        ImageButton button_dot = (ImageButton) findViewById(R.id.dot_button);
        Display disp_info = getWindowManager().getDefaultDisplay();
        Point point_info = new Point();
        disp_info.getSize(point_info);
        /*int x = (int) (position * (point_info.x - (2*button_dot.getWidth()))) + button_dot.getWidth() ;
        int y = (int) (position * (point_info.y - (2*button_dot.getHeight()))) + button_dot.getHeight();*/
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