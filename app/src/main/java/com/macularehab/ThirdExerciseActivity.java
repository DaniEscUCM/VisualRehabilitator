package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ThirdExerciseActivity extends AppCompatActivity {
    private int counter = 0, counterCorrect;
    private final int total = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        counterCorrect = 0;
        /*List<Number> numbers = null;
        for(int i = 1; i < 4;i++) {
            numbers.add(i);
        }
        //arrayOrder.shuffle();
        Collections.shuffle(numbers);*/
        ImageButton button_dot = findViewById(R.id.dot_button);
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                counter = move2(counter);
                //counter2 = move2(counter2);
            }
        });


        ImageButton button_setting = findViewById(R.id.third_exercise_settings);
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

    //Declare timer
    CountDownTimer cTimer = null;

    //Start timer function
    void startTimer() {
        //10s (10000 mili segundos) para hacer click en el circulo
        //Lo pongo a 3s para hacer pruebas
        cTimer = new CountDownTimer(3000, 10) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                move2(++counter);
            }
        };
        cTimer.start();
    }

    //cancel timer
    /*void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }*/


/*
    private int move(int counter){
        startTimer();
        ImageButton button_dot = findViewById(R.id.dot_button);
        Display disp_info = getWindowManager().getDefaultDisplay();
        Point point_info = new Point();
        disp_info.getSize(point_info);
        int x, y;
        if(counter == 0) {   //La primera vez aparece en el centro*/
            /* x = point_info.x - (2 * button_dot.getWidth()) + button_dot.getWidth();
            y = point_info.y - (2 * button_dot.getHeight()) + button_dot.getHeight();*/
            /*x = button_dot.getWidth() + button_dot.getWidth();
            y = button_dot.getHeight() + button_dot.getHeight();
            button_dot.getPivotX();
            button_dot.setX(x);
            button_dot.setY(y);
            ++counterCorrect;
            //por algun motivo, al primer punto le da igual el temporizador
        }
        else if(counter > 0 && counter < total) {
            x = (int) (0.025 * (point_info.x - (button_dot.getWidth()))) + button_dot.getWidth();
            y = (int) (0.025 * (point_info.y - (button_dot.getHeight()))) + button_dot.getHeight();
            button_dot.getPivotX();
            button_dot.setX(x);
            button_dot.setY(y);
            ++counterCorrect;
        }
        else {
            finish();
        }
        return ++counter;
    }*/

    private int move2(int counter){
        if(counter < total){
            startTimer();
            ImageButton find_dot = findViewById(R.id.dot_button); //no estaba asi
            Display disp_info = getWindowManager().getDefaultDisplay();
            Point point_info = new Point();
            disp_info.getSize(point_info);
            int x = (int) (Math.random() * (point_info.x - (2 * find_dot.getWidth()))) + find_dot.getWidth();
            int y = (int) (Math.random() * (point_info.y - (2 * find_dot.getHeight()))) + find_dot.getHeight();

            find_dot.getPivotX();
            find_dot.setX(x);
            find_dot.setY(y);
        }
        else{finish();}
        return ++counter;
    }

    public void Close(View view){
        if (counterCorrect*2 >= total) { //si ha acertado el doble o mas del total
            String message_correct = "Well done, you hit " + counterCorrect + "out of" + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();

            //se desbloquea el siguiente ejercicio
        }
        else {
            String message_failed = "Sorry, try again";
            Toast.makeText(this, message_failed, Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public void Settings(View view){
        Intent i = new Intent( this, SettingsActivity.class );
        startActivity(i);
    }
}