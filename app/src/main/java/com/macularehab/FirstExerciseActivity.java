package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class FirstExerciseActivity extends AppCompatActivity {
    int counter = 0, counterCorrect, total = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        counterCorrect = 0;
        ImageButton button_dot = findViewById(R.id.dot_button);
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { counter = move(counter); }
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

    //Declare timer
    CountDownTimer cTimer = null;

    //Start timer function
    void startTimer() {
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

    //cancel timer
    /*void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }*/

    private int move(int counter){
        startTimer();
        ImageButton button_dot = findViewById(R.id.dot_button);
        Display disp_info = getWindowManager().getDefaultDisplay();
        Point point_info = new Point();
        disp_info.getSize(point_info);
        int x, y;
        if(counter == 0) {   //La primera vez aparece en el centro
            x = button_dot.getWidth() + button_dot.getWidth();
            y = button_dot.getHeight() + button_dot.getHeight();
            button_dot.getPivotX();
            button_dot.setX(x);
            button_dot.setY(y);
            ++counterCorrect;
            //por algun motivo, al primer punto le da igual el temporizador

            //Esto sobraria pero sin esto el primer punto dek else if SIEMPRE sale en el mismo sitio (abajo drch)
            x = (int) (Math.random() * (point_info.x - (2 * button_dot.getWidth()))) + button_dot.getWidth();
            y = (int) (Math.random() * (point_info.y - (2 * button_dot.getHeight()))) + button_dot.getHeight();
            button_dot.getPivotX();
            button_dot.setX(x);
            button_dot.setY(y);
        }
        else if(counter > 0 && counter < total) {
            x = (int) (Math.random() * (point_info.x - (2 * button_dot.getWidth()))) + button_dot.getWidth();
            y = (int) (Math.random() * (point_info.y - (2 * button_dot.getHeight()))) + button_dot.getHeight();
            button_dot.getPivotX();
            button_dot.setX(x);
            button_dot.setY(y);
            ++counterCorrect;
        }
        else {
            finish();
        }
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