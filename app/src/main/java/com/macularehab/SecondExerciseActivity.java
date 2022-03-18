package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SecondExerciseActivity extends AppCompatActivity {
    protected int counter, counterCorrect,counterFailed, num_miliseconds;
    protected final int total = 10;
    protected boolean triangle, written;
    protected CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        counterCorrect = counterFailed = 0; counter = -1;
        triangle = written = false;
        timer = null;
        //Aqui primero tendria que aparecer el foco
        //No seria image button, solo image, o image button sin que se pueda hacer click
        //Poner un temporizador de 5s antes de que aparezca la 1a figura
        //El foco se "Multiplica" por la posicion que toque y las figuras se dejan
        //donde estan, en el centro.
        int w = 50, h = 50;
        ImageView foco = findViewById(R.id.foco);
        foco.getLayoutParams().width = w;
        foco.getLayoutParams().height = h;
        //falta meter tamaño del foco

        ImageButton button_dot = findViewById(R.id.dot_button);
        button_dot.setVisibility(View.INVISIBLE);
        startTimerFoco(button_dot); //5s antes de que aparezca nada más

        num_miliseconds = SecondExerciseDescriptionActivity.getNumSeconds() * 1000;

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
        //10s (10000 mili segundos) para hacer click en el circulo
        //Lo pongo a 3-6s para hacer pruebas
        timer = new CountDownTimer(num_miliseconds, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                move();
                timer.cancel();
            }
        };
        timer.start();
    }

    private void cancelTimer() {
        timer.cancel();
        if(timer!=null) {
            timer.cancel();
        }
            //cTimer.onFinish();
    }

    private void move(){
        cancelTimer();
        timer = null;
        if(++counter == total) {
            System.out.println("counter==total");
            if(!written) {
                written = true;
                System.out.println("counter: "+ counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
                String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
                Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            }
            finish();
        }
        else {
            ImageButton button_dot = (ImageButton) findViewById(R.id.dot_button);
            System.out.println("counter: " + counter);
            startTimer();
            if (counter == 0 || counter == 5 || counter == 7 || counter == 9) {
                button_dot.setImageResource(R.drawable.circle_black);
                triangle = false;
                System.out.println("counter: " + counter + ". circulo");
            } else if (counter == 1 || counter == 3 || counter == 6) {
                button_dot.setImageResource(R.drawable.triangle);
                triangle = true;
                System.out.println("counter: " + counter + ". triang");
            } else { //2,4,8
                button_dot.setImageResource(R.drawable.star);
                triangle = false;
                System.out.println("counter: " + counter + ". estrella");
            }
        }
    }

    public void Close(View view){
        finish();
    }

    public void Settings(View view){
        finish(); //para que termine el ejercicio y no siga funcionando mientras esta en settings
        Intent i = new Intent( this, SettingsActivity.class );
        startActivity(i);
    }
}