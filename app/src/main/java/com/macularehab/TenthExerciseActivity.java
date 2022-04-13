package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.internalStorage.ReadInternalStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TenthExerciseActivity extends AppCompatActivity {

    private final int exercise_id = 9, total = 12, num_shapes = 4;
    private int counter, counterCorrect, counterFailed, num_miliseconds, previous;
    private boolean coffee;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenth_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        String filenameCurrentUser = "CurrentPatient.json";
        ReadInternalStorage readIS = new ReadInternalStorage();
        HashMap<String, Object> patientHashMap = readIS.read(getApplicationContext(), filenameCurrentUser);
        counterCorrect = counterFailed = 0;
        counter = previous = -1;
        coffee = false;
        timer = null;
        num_miliseconds = TenthExerciseDescriptionActivity.getNumSeconds() * 1000;
        boolean focus_on = (boolean) patientHashMap.get("focusIsOn");
        ImageButton button_dot = findViewById(R.id.dot_button);
        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit = (int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit * 20;//10cm
        button_dot.getLayoutParams().width = metric_unit * 6;//3cm diametro de las figuras
        button_dot.getLayoutParams().height = metric_unit * 6;

        ImageView focus = findViewById(R.id.focus);
        if (focus_on) {
            button_dot.setVisibility(View.INVISIBLE);
            ArrayList<Pair<Float, Float>> coor_result;
            LinkedTreeMap tree = (LinkedTreeMap) patientHashMap.get("focus");
            coor_result = new ArrayList<>();
            coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));

            focus.getLayoutParams().width = size;
            focus.getLayoutParams().height = size;
            focus.requestLayout();
            Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(btm_manual_left);
            DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
            all_dots.draw(canvas);
            focus.setImageBitmap(btm_manual_left);

            startTimerFoco(button_dot); //Durante 5s solo se ve el foco
        } else {
            focus.setVisibility(View.INVISIBLE);
            move();
        }

        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coffee) {
                    ++counterCorrect;
                } else {
                    ++counterFailed;
                } //they clicked when they shouldn't have
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
        timer = new CountDownTimer(num_miliseconds, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                if (coffee) {
                    ++counterFailed;
                } //they didn't touch when they should have.
                else {
                    ++counterCorrect;
                }
                move();
            }
        };
        timer.start();
    }

    private void cancelTimer() {
        timer.cancel();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void move() {
        if (++counter == total) {
            //Correct is: touching when they should and not touching when they shouldn't.
            //Incorrect is: touching when they shouldn't and not touching when they should.
            //In this exercise: correct is touching the coffee cups and not touching the other images,
            //incorrect is not touching the coffee cups and touching other images.

            //TO DO
            writeResultInDataBase(counterCorrect, counterFailed);
            System.out.println("counter: " + counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
            String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            finish();
        } else {
            int rand;
            do {
                rand = new Random().nextInt(num_shapes);
            } while (previous==rand);
            previous = rand;

            ImageButton button_dot = (ImageButton) findViewById(R.id.dot_button);
            System.out.println("counter: " + counter);
            startTimer();
            if (previous == 0) {
                button_dot.setImageResource(R.drawable.juice);
                coffee = false;
                System.out.println("counter: " + counter + ". juice");
            } else if (previous == 1) {
                button_dot.setImageResource(R.drawable.coffee);
                coffee = true;
                System.out.println("counter: " + counter + ". coffee");
            } else if (previous == 2) {
                button_dot.setImageResource(R.drawable.cola);
                coffee = false;
                System.out.println("counter: " + counter + ". cola");

            } else {
                button_dot.setImageResource(R.drawable.water);
                coffee = false;
                System.out.println("counter: " + counter + ". water");

            }
        }
    }

    public void Close(View view) {
        counter = total + 1;
        finish();
    }

    public void Settings(View view) {
        counter = total + 1;
        finish();
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public int getNumCorrect() {
        return counterCorrect;
    }

    public int getNumFailed() {
        return counterFailed;
    }

    //Database
    private void writeResultInDataBase(int correct, int failed) {
       ExerciseWriteDB exerciseWriteDB = new ExerciseWriteDB(exercise_id);
       exerciseWriteDB.writeResultInDataBase(getApplicationContext(), correct, failed, 0);
    }
}
