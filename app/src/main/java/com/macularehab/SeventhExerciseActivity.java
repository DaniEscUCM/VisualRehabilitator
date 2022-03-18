package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class SeventhExerciseActivity extends AppCompatActivity {
    protected int counter, counterCorrect, counterFailed,previous_1,previous_2;
    protected final int total = 10;
    protected boolean triangle_1, triangle_2;
    protected CountDownTimer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        counterCorrect = counterFailed = 0;
        counter = -1;
        triangle_1 =  triangle_2 = false;
        ImageButton button_1 = findViewById(R.id.button_1);
        ImageButton button_2 = findViewById(R.id.button_2);
        previous_1 = new Random().nextInt(3); //da un valor del 0 al 2
        previous_2 = new Random().nextInt(3);
        move();
        move_button2();
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (triangle_1) {
                    ++counterCorrect;
                } else {
                    ++counterFailed;
                }
                cancelTimer();
                move();
            }
        });
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (triangle_2) {
                    ++counterCorrect;
                } else {
                    ++counterFailed;
                }
                cancelTimer();
                move_button2();
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
        timer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) { }

            public void onFinish() {
                move();
            }
        };
        timer.start();
    }

    private void startTimer_button2() {
        timer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) { }

            public void onFinish() {
                move_button2();
            }
        };
        timer.start();
    }

    private void cancelTimer() {
        if (timer != null)
            timer.cancel();
    }

    private void move() {
        if (++counter == total) {
            System.out.println("counter: " + counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
            String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            finish();
        } else {
            System.out.println("counter: " + counter);
            System.out.println("previous_1: " + previous_1 + ". previous_2" + previous_2);

            /*while (rand1 != previous_1) {
                rand1 = new Random().nextInt(3); //da un valor del 0 al 2
                System.out.println("Hola r1: " + rand1);
            }
            do {
                rand2 = new Random().nextInt(3); //da un valor del 0 al 2
                System.out.println("Hola r2: " + rand2);
            } while (rand2 != previous_2);*/
            boolean end=false;
            while(!end) {
                int rand1 = new Random().nextInt(3);
                if(previous_1!=rand1) {
                    previous_1 = rand1; //previous_2 = rand2;
                    end=true;
                }
                System.out.println("r1: " + rand1 + " counter: " + counter);
            }
            System.out.println("previous_1: " + previous_1 + ". previous_2: " + previous_2);

            ImageButton button_1 = findViewById(R.id.button_1);
            startTimer();
            if(previous_1 == 0) {
                button_1.setImageResource(R.drawable.circle_black);
                triangle_1 = false;
                System.out.println("counter: " + counter + ". circulo1");
            } else if (previous_1 == 1) {
                button_1.setImageResource(R.drawable.triangle);
                triangle_1 = true;
                System.out.println("counter: " + counter + ". triang1");
            } else {
                button_1.setImageResource(R.drawable.star);
                triangle_1 = false;
                System.out.println("counter: " + counter + ". estrella1");
            }
        }
    }

    private void move_button2() {
        if (++counter == total) {
            System.out.println("counter: " + counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
            String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            finish();
        } else {
            System.out.println("counter: " + counter);
            System.out.println("previous_1: " + previous_1 + ". previous_2" + previous_2);
            boolean end=false;
            while(!end){
                int rand2 = new Random().nextInt(3);
                rand2 = new Random().nextInt(3);
                if(rand2!=previous_2) {previous_2 = rand2;end=true;}
                System.out.println(". r2: " + rand2 + " counter: " + counter);
            }
            System.out.println("previous_1: " + previous_1 + ". previous_2: " + previous_2);

            ImageButton button_2 = findViewById(R.id.button_2);
            startTimer_button2();

            if (previous_2 == 0) {
                button_2.setImageResource(R.drawable.circle_black);
                triangle_2 = false;
                System.out.println("counter: " + counter + ". circulo2");
            } else if (previous_2 == 1) {
                button_2.setImageResource(R.drawable.triangle);
                triangle_2 = true;
                System.out.println("counter: " + counter + ". triang2");
            } else {
                button_2.setImageResource(R.drawable.star);
                triangle_2 = false;
                System.out.println("counter: " + counter + ". estrella2");
            }
        }
    }

    public void Close(View view) {
        System.out.println("counter: " + counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
        String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
        Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
        finish();
    }

    public void Settings(View view) {
        finish(); //para que termine el ejercicio y no siga funcionando mientras esta en settings
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
}
