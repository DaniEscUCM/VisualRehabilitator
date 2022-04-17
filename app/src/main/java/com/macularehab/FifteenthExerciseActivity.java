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

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.exercises.ShowResultActivity;
import com.macularehab.internalStorage.ReadInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class FifteenthExerciseActivity  extends AppCompatActivity {

    private final int exercise_id = 14, maxCorrect = 24;
    private int counterCorrect, counterFailed, num_miliseconds, metric_unit, size;
    private boolean focus_on;
    private CountDownTimer timer_1 = null;
    private HashMap<String, Object> patientHashMap;
    private boolean n_correct1,n_correct2,n_correct3,n_correct4,n_correct5,n_correct6,n_correct7,n_correct8,n_correct9,n_correct10;
    private boolean cross5_1,cross5_2,cross5_3,cross5_4,cross6_1,cross6_2,cross6_3,cross6_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifteenth_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        String filenameCurrentUser = "CurrentPatient.json";
        ReadInternalStorage readIS = new ReadInternalStorage();
        patientHashMap = readIS.read(getApplicationContext(), filenameCurrentUser);
        counterCorrect = counterFailed = 0;
        num_miliseconds = FifteenthExerciseDescriptionActivity.getNumSeconds() * 1000;
        focus_on = (boolean) patientHashMap.get("focusIsOn");
        //SEQUENCE N:
        ImageButton button_n_correct1 = findViewById(R.id.button_n_correct1);
        ImageButton button_n_correct2 = findViewById(R.id.button_n_correct2);
        ImageButton button_n_correct3 = findViewById(R.id.button_n_correct3);
        ImageButton button_n_correct4 = findViewById(R.id.button_n_correct4);
        ImageButton button_n_correct5 = findViewById(R.id.button_n_correct5);
        ImageButton button_n_correct6 = findViewById(R.id.button_n_correct6);
        ImageButton button_n_correct7 = findViewById(R.id.button_n_correct7);
        ImageButton button_n_correct8 = findViewById(R.id.button_n_correct8);
        ImageButton button_n_correct9 = findViewById(R.id.button_n_correct9);
        ImageButton button_n_correct10 = findViewById(R.id.button_n_correct10);
        ImageButton button_n_incorrect1 = findViewById(R.id.button_n_incorrect1);
        ImageButton button_n_incorrect2 = findViewById(R.id.button_n_incorrect2);
        ImageButton button_n_incorrect3 = findViewById(R.id.button_n_incorrect3);
        ImageButton button_n_incorrect4 = findViewById(R.id.button_n_incorrect4);
        ImageButton button_n_incorrect5 = findViewById(R.id.button_n_incorrect5);
        ImageButton button_n_incorrect6 = findViewById(R.id.button_n_incorrect6);
        ImageButton button_n_incorrect7 = findViewById(R.id.button_n_incorrect7);

        //SEQUENCE O:

        //SEQUENCE V:


        ArrayList<Pair<Float, Float>> coor_result;
        LinkedTreeMap tree = (LinkedTreeMap) patientHashMap.get("focus");
        coor_result = new ArrayList<>();
        coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit = (int) Math.round(display.xdpi * 0.19685); //0.5cm
        size = metric_unit * 20;//10cm
        ImageView focus = findViewById(R.id.focus_n);
        focus.getLayoutParams().width = size;
        focus.getLayoutParams().height = size;
        focus.requestLayout();
        Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btm_manual_left);
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
        all_dots.draw(canvas);
        focus.setImageBitmap(btm_manual_left);

        if (focus_on) {
            System.out.println("on");
            focus_function();
        } else {
            System.out.println("off");
            focus.setVisibility(View.INVISIBLE);
            startTimer();
        }

        n_correct1 = n_correct2 = n_correct3 = n_correct4 = n_correct5 = true;
        n_correct6 = n_correct7 = n_correct8 = n_correct9 = true;

        //SEQUENCE N:
        //corrects:
        button_n_correct1.setOnClickListener(v -> {
            if (n_correct1) {
                n_correct1 = false;
                ++counterCorrect;
            }
        });
        button_n_correct2.setOnClickListener(v -> {
            if (n_correct2) {
                n_correct2 = false;
                ++counterCorrect;
            }
        });
        button_n_correct3.setOnClickListener(v -> {
            if (n_correct3) {
                n_correct3 = false;
                ++counterCorrect;
            }
        });
        button_n_correct4.setOnClickListener(v -> {
            if (n_correct4) {
                n_correct4 = false;
                ++counterCorrect;
            }
        });
        button_n_correct5.setOnClickListener(v -> {
            if (n_correct5) {
                n_correct5 = false;
                ++counterCorrect;
            }
        });
        button_n_correct6.setOnClickListener(v -> {
            if (n_correct6) {
                n_correct6 = false;
                ++counterCorrect;
            }
        });
        button_n_correct7.setOnClickListener(v -> {
            if (n_correct7) {
                n_correct7 = false;
                ++counterCorrect;
            }
        });
        button_n_correct8.setOnClickListener(v -> {
            if (n_correct8) {
                n_correct8 = false;
                ++counterCorrect;
            }
        });

        button_n_correct9.setOnClickListener(v -> {
            if (n_correct9) {
                n_correct9 = false;
                ++counterCorrect;
            }
        });
        button_n_correct10.setOnClickListener(v -> {
            if (n_correct10) {
                n_correct10 = false;
                ++counterCorrect;
            }
        });
        //Incorrect:
        button_n_incorrect1.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_n_incorrect2.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_n_incorrect3.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_n_incorrect4.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_n_incorrect5.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_n_incorrect6.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_n_incorrect7.setOnClickListener(v -> {
            ++counterFailed;
        });

        //SEQUENCE O:
    }

    private void startTimerFoco() {
        timer_1 = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                startTimer();
            }
        };
        timer_1.start();
    }

    private void focus_function () {
        ImageView focus = findViewById(R.id.focus_n);
        focus.setVisibility(View.VISIBLE);
        startTimerFoco();
    }

    private void startTimer() {
        timer_1 = new CountDownTimer(num_miliseconds, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                System.out.println("Hola");
                System.out.println("counterCorrect: " + counterCorrect + ", counterFailed: "+counterFailed);
                if(counterCorrect != 0 || counterFailed != 0) {
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        };
        timer_1.start();
    }

    private void cancelTimer_1() {
        if (timer_1 != null)
            timer_1.cancel();
    }

    public void Close(View view) {
        finish();
    }

    //Database
    private void writeResultInDataBase(int correct, int failed) {
        ExerciseWriteDB exerciseWriteDB = new ExerciseWriteDB(exercise_id);
        exerciseWriteDB.writeResultInDataBase(getApplicationContext(), correct, failed, 0);
    }

    private void showResults(int correct, int failed) {
        Intent resultIntent = new Intent(this, ShowResultActivity.class);
        resultIntent.putExtra("numCorrect", correct);
        resultIntent.putExtra("numFailed", failed);
        startActivity(resultIntent);
    }
}
