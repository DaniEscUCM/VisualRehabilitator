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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ThirdExerciseActivity extends AppCompatActivity {
    private int counter, counterCorrect, counterFailed, num_miliseconds;
    protected final int total = 12;
    private boolean triangle, focus_on;
    private CountDownTimer timer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        counter = -1; counterCorrect = counterFailed = 0;
        triangle = false;
        ImageButton button_dot = findViewById(R.id.dot_button);
        button_dot.setVisibility(View.INVISIBLE);
        num_miliseconds = ThirdExerciseDescriptionActivity.getNumSeconds() * 1000;
        //num_miliseconds = 1000;
        focus_on = true; //leer de la clase anterior como seconds
        ImageView foco = findViewById(R.id.foco);
        if(focus_on) {
            int w = 50, h = 50;
            foco.getLayoutParams().width = w;
            foco.getLayoutParams().height = h;
            //falta meter tamaño del foco
            startTimerFoco(button_dot); //5s antes de que aparezca nada más
        }
        else{
            foco.setVisibility(View.INVISIBLE);
            move();
        }


        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(triangle) {++counterCorrect;}
                else {++counterFailed;}
                cancelTimer();
                move();
            }
        });

        ImageButton button_setting = findViewById(R.id.exercise_settings);
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
    private void startTimerFoco(ImageButton button_dot) {     //Timer para que aparezca el foco solo 5s
        timer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                button_dot.setVisibility(View.VISIBLE);
                move();
            }
        };
        timer.start();
    }

    private void startTimer() {
        //10s (10000 mili segundos) para hacer click en la figura
        timer = new CountDownTimer(num_miliseconds, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                move();
            }
        };
        timer.start();
    }

    private void cancelTimer() {
        if(timer!=null)
            timer.cancel();
            //timer.onFinish();
    }

    private void move(){
        if(++counter == total) {
            System.out.println("counter: "+ counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
            String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            ImageButton button_dot = findViewById(R.id.dot_button);
            System.out.println("counter: " + counter);
            startTimer();
            triangle = false;
            if (counter == 0 || counter == 6 || counter == 10) {
                button_dot.setImageResource(R.drawable.circle_line);
                System.out.println("counter: " + counter + ". circulo");
            } else if (counter == 2 || counter == 5 || counter == 9) {
                button_dot.setImageResource(R.drawable.triangle_line);
                triangle = true;
                System.out.println("counter: " + counter + ". triang");
            } else if (counter == 4 || counter == 7 || counter == 11) {
                button_dot.setImageResource(R.drawable.star_line);
                System.out.println("counter: " + counter + ". estrella");
            } else { //1,3,8
                button_dot.setImageResource(R.drawable.semi_square_line);
                System.out.println("counter: " + counter + ". squareL");
            }
        }
    }

    public void Close(View view){
        System.out.println("counter: "+ counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
        String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
        Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
        finish();
    }

    public void Settings(View view){
        finish(); //para que termine el ejercicio y no siga funcionando mientras esta en settings
        Intent i = new Intent( this, SettingsActivity.class );
        startActivity(i);
    }
}


/*
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
    }*/
