package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class FifthExerciseActivity extends AppCompatActivity {
    private int counter, counterCorrect, counterFailed;
    protected final int total = 13;
    private boolean is_letter_E;
    protected CountDownTimer timer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        counter = -1;
        counterCorrect = counterFailed = 0;
        is_letter_E = false;
        Button button_dot = findViewById(R.id.button);
        move();
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_letter_E) {++counterCorrect;}
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

    private void startTimer() {
        //10s (10000 mili segundos) para hacer click en la letra
        timer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() { move(); }
        };
        timer.start();
    }

    private void cancelTimer() {
        if(timer!=null)
            timer.cancel();
    }

    private void move(){
        if(++counter == total) { //13==13, el ultimo es el 12
            System.out.println("counter: "+ counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
            String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            Button button_dot = findViewById(R.id.button);
            System.out.println("counter: " + counter);
            is_letter_E = false;
            startTimer();
            if (counter == 2 || counter == 6 || counter == 11) {
                button_dot.setText("T");
                System.out.println("counter: " + counter + ". T");
            } else if (counter == 0 || counter == 10) {
                button_dot.setText("M");
                System.out.println("counter: " + counter + ". M");
            } else if (counter == 1 || counter == 4 || counter == 12) {
                is_letter_E = true;
                button_dot.setText("E");
                System.out.println("counter: " + counter + ". E");
            } else if (counter == 5 || counter == 8) {
                button_dot.setText("L");
                System.out.println("counter: " + counter + ". L");
            } else { //3,7,12
                button_dot.setText("F");
                System.out.println("counter: " + counter + ". F");
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
