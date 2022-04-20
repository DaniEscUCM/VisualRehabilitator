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
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.exercises.SaveFocusInfo;
import com.macularehab.exercises.ShowResultActivity;
import com.macularehab.internalStorage.ReadInternalStorage;
import java.util.ArrayList;
import java.util.HashMap;

public class FourteenthExerciseActivity extends AppCompatActivity {

    private final int exercise_id = 13, maxCorrect = 24;
    private int counterCorrect, counterFailed, num_miliseconds, metric_unit, size, total=0;
    private boolean focus_on;
    private CountDownTimer timer_1 = null;
    private HashMap<String, Object> patientHashMap;
    private boolean cross1_1,cross1_2,cross1_3,cross1_4,cross2_1,cross2_2,cross2_3,cross2_4;
    private boolean cross3_1,cross3_2,cross3_3,cross3_4,cross4_1,cross4_2,cross4_3,cross4_4;
    private boolean cross5_1,cross5_2,cross5_3,cross5_4,cross6_1,cross6_2,cross6_3,cross6_4;
    private String filenameCurrentUser = "CurrentPatient.json";

    private final String isFocus = "focusIsOn";
    private ImageButton button_cross1_1, button_cross1_2, button_cross1_3, button_cross1_4,
            button_cross2_1, button_cross2_2, button_cross2_3, button_cross2_4,
            button_cross3_1, button_cross3_2, button_cross3_3, button_cross3_4,
            button_cross4_1, button_cross4_2, button_cross4_3, button_cross4_4,
            button_cross5_1, button_cross5_2, button_cross5_3, button_cross5_4,
            button_cross6_1, button_cross6_2, button_cross6_3, button_cross6_4;
    private boolean dot=false;
    private long time_left=5000;
    private ImageView focus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourteenth_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ReadInternalStorage readIS = new ReadInternalStorage();
        patientHashMap = readIS.read(getApplicationContext(), filenameCurrentUser);

        ImageButton button_pause = findViewById(R.id.pause_button);
        button_pause.setOnClickListener(v -> pause_menu());

        ImageButton button_resume = findViewById(R.id.return_button);
        button_resume.setOnClickListener(v->resume());

        Switch focus_switch = findViewById(R.id.focus_switch1);
        focus_switch.setChecked((Boolean) patientHashMap.get(isFocus));
        focus_on=(Boolean) patientHashMap.get(isFocus);
        focus_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            focus_on=!focus_on;
        });

        ImageButton button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(v -> Close());

        counterCorrect = counterFailed = 0;
        num_miliseconds = FourteenthExerciseDescriptionActivity.getNumSeconds() * 1000;
        button_cross1_1 = findViewById(R.id.cross1_1);
        button_cross1_2 = findViewById(R.id.cross1_2);
        button_cross1_3 = findViewById(R.id.cross1_3);
        button_cross1_4 = findViewById(R.id.cross1_4);
        button_cross2_1 = findViewById(R.id.cross2_1);
        button_cross2_2 = findViewById(R.id.cross2_2);
        button_cross2_3 = findViewById(R.id.cross2_3);
        button_cross2_4 = findViewById(R.id.cross2_4);

        button_cross3_1 = findViewById(R.id.cross3_1);
        button_cross3_2 = findViewById(R.id.cross3_2);
        button_cross3_3 = findViewById(R.id.cross3_3);
        button_cross3_4 = findViewById(R.id.cross3_4);
        button_cross4_1 = findViewById(R.id.cross4_1);
        button_cross4_2 = findViewById(R.id.cross4_2);
        button_cross4_3 = findViewById(R.id.cross4_3);
        button_cross4_4 = findViewById(R.id.cross4_4);

        button_cross5_1 = findViewById(R.id.cross5_1);
        button_cross5_2 = findViewById(R.id.cross5_2);
        button_cross5_3 = findViewById(R.id.cross5_3);
        button_cross5_4 = findViewById(R.id.cross5_4);
        button_cross6_1 = findViewById(R.id.cross6_1);
        button_cross6_2 = findViewById(R.id.cross6_2);
        button_cross6_3 = findViewById(R.id.cross6_3);
        button_cross6_4 = findViewById(R.id.cross6_4);

        ArrayList<Pair<Float, Float>> coor_result;
        LinkedTreeMap tree = (LinkedTreeMap) patientHashMap.get("focus");
        coor_result = new ArrayList<>();
        coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit = (int) Math.round(display.xdpi * 0.19685); //0.5cm
        size = metric_unit * 20;//10cm
        focus = findViewById(R.id.foco);
        focus.getLayoutParams().width = size;
        focus.getLayoutParams().height = size;
        focus.requestLayout();
        Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btm_manual_left);
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
        all_dots.draw(canvas);
        focus.setImageBitmap(btm_manual_left);

        if(focus_on) {
            focus_function();
        }
        else {
            time_left=num_miliseconds;
            focus.setVisibility(View.INVISIBLE);
            startTimer();
        }

        cross1_1 = cross1_2 = cross1_3 = cross1_4 = true;
        cross2_1 = cross2_2 = cross2_3 = cross2_4 = true;
        cross3_1 = cross3_2 = cross3_3 = cross3_4 = true;
        cross4_1 = cross4_2 = cross4_3 = cross4_4 = true;
        cross5_1 = cross5_2 = cross5_3 = cross5_4 = true;
        cross6_1 = cross6_2 = cross6_3 = cross6_4 = true;

        //circle 1:
        button_cross1_1.setOnClickListener(v -> {
            if (cross1_1) {
                cross1_1 = false;
                ++counterCorrect;
                button_cross1_1.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross1_2.setOnClickListener(v -> {
            if (cross1_2) {
                cross1_2 = false;
                ++counterCorrect;
                button_cross1_2.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross1_3.setOnClickListener(v -> {
            if (cross1_3) {
                cross1_3 = false;
                ++counterCorrect;
                button_cross1_3.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross1_4.setOnClickListener(v -> {
            if (cross1_4) {
                cross1_4 = false;
                ++counterCorrect;
                button_cross1_4.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });

        //circle 2:
        button_cross2_1.setOnClickListener(v -> {
            if (cross2_1) {
                cross2_1 = false;
                ++counterCorrect;
                button_cross2_1.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross2_2.setOnClickListener(v -> {
            if (cross2_2) {
                cross2_2 = false;
                ++counterCorrect;
                button_cross2_2.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross2_3.setOnClickListener(v -> {
            if (cross2_3) {
                cross2_3 = false;
                ++counterCorrect;
                button_cross2_3.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross2_4.setOnClickListener(v -> {
            if (cross2_4) {
                cross2_4 = false;
                ++counterCorrect;
                button_cross2_4.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });

        //circle 3:
        button_cross3_1.setOnClickListener(v -> {
            if (cross3_1) {
                cross3_1 = false;
                ++counterCorrect;
                button_cross3_1.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross3_2.setOnClickListener(v -> {
            if (cross3_2) {
                cross3_2 = false;
                ++counterCorrect;
                button_cross3_2.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross3_3.setOnClickListener(v -> {
            if (cross3_3) {
                cross3_3 = false;
                ++counterCorrect;
                button_cross3_3.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross3_4.setOnClickListener(v -> {
            if (cross3_4) {
                cross3_4 = false;
                ++counterCorrect;
                button_cross3_4.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });

        //circle 4:
        button_cross4_1.setOnClickListener(v -> {
            if (cross4_1) {
                cross4_1 = false;
                ++counterCorrect;
                button_cross4_1.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross4_2.setOnClickListener(v -> {
            if (cross4_2) {
                cross4_2 = false;
                ++counterCorrect;
                button_cross4_2.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross4_3.setOnClickListener(v -> {
            if (cross4_3) {
                cross4_3 = false;
                ++counterCorrect;
                button_cross4_3.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross4_4.setOnClickListener(v -> {
            if (cross4_4) {
                cross4_4 = false;
                ++counterCorrect;
                button_cross4_4.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });

        //circle 5:
        button_cross5_1.setOnClickListener(v -> {
            if (cross5_1) {
                cross5_1 = false;
                ++counterCorrect;
                button_cross5_1.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross5_2.setOnClickListener(v -> {
            if (cross5_2) {
                cross5_2 = false;
                ++counterCorrect;
                button_cross5_2.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross5_3.setOnClickListener(v -> {
            if (cross5_3) {
                cross5_3 = false;
                ++counterCorrect;
                button_cross5_3.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross5_4.setOnClickListener(v -> {
            if (cross5_4) {
                cross5_4 = false;
                ++counterCorrect;
                button_cross5_4.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });

        //circle 6:
        button_cross6_1.setOnClickListener(v -> {
            if (cross6_1) {
                cross6_1 = false;
                ++counterCorrect;
                button_cross6_1.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross6_2.setOnClickListener(v -> {
            if (cross6_2) {
                cross6_2 = false;
                ++counterCorrect;
                button_cross6_2.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross6_3.setOnClickListener(v -> {
            if (cross6_3) {
                cross6_3 = false;
                ++counterCorrect;
                button_cross6_3.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });
        button_cross6_4.setOnClickListener(v -> {
            if (cross6_4) {
                cross6_4 = false;
                ++counterCorrect;
                button_cross6_4.setColorFilter(Color.GREEN);
                total++;
                if(total==24){
                    timer_1.cancel();
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
            }
        });


    }
    private void resume(){
        if(focus_on){
            if(dot){
                startTimerFoco();
            }
            else{
                focus.setVisibility(View.VISIBLE);
                startTimer();
            }
        }
        else{
            focus.setVisibility(View.INVISIBLE);
            if(dot){
                dot=false;
            }
            startTimer();
        }
        unblock_buttons();
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.GONE);
    }

    private void pause_menu(){
        timer_1.cancel();
        block_buttons();
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);
    }


    private void saveFocusOn(){
        new SaveFocusInfo(getApplicationContext(), focus_on);
    }
    private void startTimerFoco() {
        dot=true;
        timer_1 = new CountDownTimer(time_left, 1000) {
            public void onTick(long millisUntilFinished) {time_left=millisUntilFinished; }
            public void onFinish() {
                dot=false;
                time_left=num_miliseconds;
                startTimer();
            }
        };
        timer_1.start();
    }

    private void focus_function () {
        ImageView focus = findViewById(R.id.foco);
        focus.setVisibility(View.VISIBLE);
        startTimerFoco();
    }

    private void block_buttons(){
        button_cross1_1.setClickable(false);
        button_cross1_2.setClickable(false);
        button_cross1_3.setClickable(false);
        button_cross1_4.setClickable(false);
        button_cross2_1.setClickable(false);
        button_cross2_2.setClickable(false);
        button_cross2_3.setClickable(false);
        button_cross2_4.setClickable(false);
        button_cross3_1.setClickable(false);
        button_cross3_2.setClickable(false);
        button_cross3_3.setClickable(false);
        button_cross3_4.setClickable(false);
        button_cross4_1.setClickable(false);
        button_cross4_2.setClickable(false);
        button_cross4_3.setClickable(false);
        button_cross4_4.setClickable(false);
        button_cross5_1.setClickable(false);
        button_cross5_2.setClickable(false);
        button_cross5_3.setClickable(false);
        button_cross5_4.setClickable(false);
        button_cross6_1.setClickable(false);
        button_cross6_2.setClickable(false);
        button_cross6_3.setClickable(false);
        button_cross6_4.setClickable(false);
    }

    private void unblock_buttons(){
        button_cross1_1.setClickable(true);
        button_cross1_2.setClickable(true);
        button_cross1_3.setClickable(true);
        button_cross1_4.setClickable(true);
        button_cross2_1.setClickable(true);
        button_cross2_2.setClickable(true);
        button_cross2_3.setClickable(true);
        button_cross2_4.setClickable(true);
        button_cross3_1.setClickable(true);
        button_cross3_2.setClickable(true);
        button_cross3_3.setClickable(true);
        button_cross3_4.setClickable(true);
        button_cross4_1.setClickable(true);
        button_cross4_2.setClickable(true);
        button_cross4_3.setClickable(true);
        button_cross4_4.setClickable(true);
        button_cross5_1.setClickable(true);
        button_cross5_2.setClickable(true);
        button_cross5_3.setClickable(true);
        button_cross5_4.setClickable(true);
        button_cross6_1.setClickable(true);
        button_cross6_2.setClickable(true);
        button_cross6_3.setClickable(true);
        button_cross6_4.setClickable(true);
    }

    private void startTimer() {
        timer_1 = new CountDownTimer(time_left, 1000) {
            public void onTick(long millisUntilFinished) {time_left=millisUntilFinished; }
            public void onFinish() {
                if(counterCorrect != 0 ) {
                    counterFailed = maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                }
                finish();
            }
        };
        timer_1.start();
    }

    private void cancelTimer_1() {
        if (timer_1 != null)
            timer_1.cancel();
    }

    public void Close() {
        timer_1.cancel();
        finish();
    }

    //Database
    private void writeResultInDataBase(int correct, int failed) {
        ExerciseWriteDB exerciseWriteDB = new ExerciseWriteDB(exercise_id);
        exerciseWriteDB.writeResultInDataBase(getApplicationContext(), correct, failed, 0);
    }

    private void showResults(int correct, int failed) {
        saveFocusOn();
        Intent resultIntent = new Intent(this, ShowResultActivity.class);
        resultIntent.putExtra("numCorrect", correct);
        resultIntent.putExtra("numFailed", failed);
        startActivity(resultIntent);
        Close();
    }
}
