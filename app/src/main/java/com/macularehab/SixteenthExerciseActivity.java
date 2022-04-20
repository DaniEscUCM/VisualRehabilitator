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
import android.widget.Button;
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

public class SixteenthExerciseActivity extends AppCompatActivity {

    private final int exercise_id = 15, maxCorrect = 21;
    private int counterCorrect, counterFailed, num_miliseconds, total=0;
    private CountDownTimer timer_1 = null;
    private HashMap<String, Object> patientHashMap;
    private boolean n_correct1,n_correct2,n_correct3,n_correct4,n_correct5,n_correct6,n_correct7;
    private boolean o_correct1,o_correct2,o_correct3,o_correct4,o_correct5,o_correct6;
    private boolean v_correct1,v_correct2,v_correct3,v_correct4,v_correct5,v_correct6,v_correct7,v_correct9,v_correct10;

    private Button button_n_correct1, button_n_correct2, button_n_correct3, button_n_correct4, button_n_correct5,
            button_n_correct6, button_n_correct7,
            button_o_correct1, button_o_correct2, button_o_correct3, button_o_correct4, button_o_correct5, button_o_correct6,
            button_v_correct1, button_v_correct2, button_v_correct3, button_v_correct4, button_v_correct5, button_v_correct6,
            button_v_correct7, button_v_correct9, button_v_correct10;

    private boolean dot=false;
    private long time_left=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixteenth_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        String filenameCurrentUser = "CurrentPatient.json";
        ReadInternalStorage readIS = new ReadInternalStorage();
        patientHashMap = readIS.read(getApplicationContext(), filenameCurrentUser);

        ImageButton button_pause = findViewById(R.id.pause_button);
        button_pause.setOnClickListener(v -> pause_menu());

        ImageButton button_resume = findViewById(R.id.return_button);
        button_resume.setOnClickListener(v->resume());

        ImageButton button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(v -> Close(v));

        counterCorrect = counterFailed = 0;
        num_miliseconds = SixteenthExerciseDescriptionActivity.getNumSeconds() * 1000;
        //SEQUENCE N:
        button_n_correct1 = findViewById(R.id.button_n_correct1);
        button_n_correct2 = findViewById(R.id.button_n_correct2);
        button_n_correct3 = findViewById(R.id.button_n_correct3);
        button_n_correct4 = findViewById(R.id.button_n_correct4);
        button_n_correct5 = findViewById(R.id.button_n_correct5);
        button_n_correct6 = findViewById(R.id.button_n_correct6);
        button_n_correct7 = findViewById(R.id.button_n_correct7);
        Button button_n_incorrect1 = findViewById(R.id.button_n_incorrect1);
        Button button_n_incorrect2 = findViewById(R.id.button_n_incorrect2);
        Button button_n_incorrect3 = findViewById(R.id.button_n_incorrect3);
        Button button_n_incorrect4 = findViewById(R.id.button_n_incorrect4);
        Button button_n_incorrect5 = findViewById(R.id.button_n_incorrect5);

        //SEQUENCE O:
        button_o_correct1 = findViewById(R.id.button_o_correct1);
        button_o_correct2 = findViewById(R.id.button_o_correct2);
        button_o_correct3 = findViewById(R.id.button_o_correct3);
        button_o_correct4 = findViewById(R.id.button_o_correct4);
        button_o_correct5 = findViewById(R.id.button_o_correct5);
        button_o_correct6 = findViewById(R.id.button_o_correct6);
        Button button_o_incorrect1 = findViewById(R.id.button_o_incorrect1);
        Button button_o_incorrect2 = findViewById(R.id.button_o_incorrect2);
        Button button_o_incorrect3 = findViewById(R.id.button_o_incorrect3);
        Button button_o_incorrect4 = findViewById(R.id.button_o_incorrect4);
        Button button_o_incorrect5 = findViewById(R.id.button_o_incorrect5);
        Button button_o_incorrect6 = findViewById(R.id.button_o_incorrect6);
        Button button_o_incorrect7 = findViewById(R.id.button_o_incorrect7);
        Button button_o_incorrect8 = findViewById(R.id.button_o_incorrect8);
        Button button_o_incorrect9 = findViewById(R.id.button_o_incorrect9);
        Button button_o_incorrect10 = findViewById(R.id.button_o_incorrect10);

        //SEQUENCE V:
        button_v_correct1 = findViewById(R.id.button_v_correct1);
        button_v_correct2 = findViewById(R.id.button_v_correct2);
        button_v_correct3 = findViewById(R.id.button_v_correct3);
        button_v_correct4 = findViewById(R.id.button_v_correct4);
        button_v_correct5 = findViewById(R.id.button_v_correct5);
        button_v_correct6 = findViewById(R.id.button_v_correct6);
        button_v_correct7 = findViewById(R.id.button_v_correct7);
        button_v_correct9 = findViewById(R.id.button_v_correct9);
        button_v_correct10 = findViewById(R.id.button_v_correct10);
        Button button_v_incorrect1 = findViewById(R.id.button_v_incorrect1);
        Button button_v_incorrect2 = findViewById(R.id.button_v_incorrect2);
        Button button_v_incorrect3 = findViewById(R.id.button_v_incorrect3);
        Button button_v_incorrect4 = findViewById(R.id.button_v_incorrect4);
        Button button_v_incorrect5 = findViewById(R.id.button_v_incorrect5);
        Button button_v_incorrect6 = findViewById(R.id.button_v_incorrect6);


        //start exercise
        time_left=num_miliseconds;
        startTimer();


        n_correct1 = n_correct2 = n_correct3 = n_correct4 = n_correct5 = true;
        n_correct6 = n_correct7 = true;
        o_correct1 = o_correct2 = o_correct3 = o_correct4 =o_correct5=o_correct6 = true;
        v_correct1 = v_correct2 = v_correct3 = v_correct4 = v_correct5 = true;
        v_correct6 = v_correct7 = v_correct9 = v_correct10 = true;

        //SEQUENCE N:
        //corrects:
        button_n_correct1.setOnClickListener(v -> {
            if (n_correct1) {
                n_correct1 = false;
                ++counterCorrect;
                total++;
                button_n_correct1.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_n_correct2.setOnClickListener(v -> {
            if (n_correct2) {
                n_correct2 = false;
                ++counterCorrect;
                total++;
                button_n_correct2.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_n_correct3.setOnClickListener(v -> {
            if (n_correct3) {
                n_correct3 = false;
                ++counterCorrect;
                total++;
                button_n_correct3.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_n_correct4.setOnClickListener(v -> {
            if (n_correct4) {
                n_correct4 = false;
                ++counterCorrect;
                total++;
                button_n_correct4.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_n_correct5.setOnClickListener(v -> {
            if (n_correct5) {
                n_correct5 = false;
                ++counterCorrect;
                total++;
                button_n_correct5.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_n_correct6.setOnClickListener(v -> {
            if (n_correct6) {
                n_correct6 = false;
                ++counterCorrect;
                total++;
                button_n_correct6.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_n_correct7.setOnClickListener(v -> {
            if (n_correct7) {
                n_correct7 = false;
                ++counterCorrect;
                total++;
                button_n_correct7.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
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

        //SEQUENCE O:
        button_o_correct1.setOnClickListener(v -> {
            if (o_correct1) {
                o_correct1 = false;
                ++counterCorrect;
                total++;
                button_o_correct1.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_o_correct2.setOnClickListener(v -> {
            if (o_correct2) {
                o_correct2 = false;
                ++counterCorrect;
                total++;
                button_o_correct2.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_o_correct3.setOnClickListener(v -> {
            if (o_correct3) {
                o_correct3 = false;
                ++counterCorrect;
                total++;
                button_o_correct3.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_o_correct4.setOnClickListener(v -> {
            if (o_correct4) {
                o_correct4 = false;
                ++counterCorrect;
                total++;
                button_o_correct4.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_o_correct5.setOnClickListener(v -> {
            if (o_correct5) {
                o_correct5 = false;
                ++counterCorrect;
                total++;
                button_o_correct5.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_o_correct6.setOnClickListener(v -> {
            if (o_correct6) {
                o_correct6 = false;
                ++counterCorrect;
                total++;
                button_o_correct6.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });

        //Incorrect:
        button_o_incorrect1.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_o_incorrect2.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_o_incorrect3.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_o_incorrect4.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_o_incorrect5.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_o_incorrect6.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_o_incorrect7.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_o_incorrect8.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_o_incorrect9.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_o_incorrect10.setOnClickListener(v -> {
            ++counterFailed;
        });

        //SEQUENCE V:
        //corrects:
        button_v_correct1.setOnClickListener(v -> {
            if (v_correct1) {
                v_correct1 = false;
                ++counterCorrect;
                total++;
                button_v_correct1.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_v_correct2.setOnClickListener(v -> {
            if (v_correct2) {
                v_correct2 = false;
                ++counterCorrect;
                total++;
                button_v_correct2.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_v_correct3.setOnClickListener(v -> {
            if (v_correct3) {
                v_correct3 = false;
                ++counterCorrect;
                total++;
                button_v_correct3.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_v_correct4.setOnClickListener(v -> {
            if (v_correct4) {
                v_correct4 = false;
                ++counterCorrect;
                total++;
                button_v_correct4.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_v_correct5.setOnClickListener(v -> {
            if (v_correct5) {
                v_correct5 = false;
                ++counterCorrect;
                total++;
                button_v_correct5.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_v_correct6.setOnClickListener(v -> {
            if (v_correct6) {
                v_correct6 = false;
                ++counterCorrect;
                total++;
                button_v_correct6.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_v_correct7.setOnClickListener(v -> {
            if (v_correct7) {
                v_correct7 = false;
                ++counterCorrect;
                total++;
                button_v_correct7.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
       
        button_v_correct9.setOnClickListener(v -> {
            if (v_correct9) {
                v_correct9 = false;
                ++counterCorrect;
                total++;
                button_v_correct9.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });
        button_v_correct10.setOnClickListener(v -> {
            if (v_correct10) {
                v_correct10 = false;
                ++counterCorrect;
                total++;
                button_v_correct10.setTextColor(Color.GREEN);
                if(total==21){
                    timer_1.cancel();
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
                    writeResultInDataBase(counterCorrect, counterFailed);
                    showResults(counterCorrect, counterFailed);
                    finish();
                }
            }
        });

        //Incorrect:
        button_v_incorrect1.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_v_incorrect2.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_v_incorrect3.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_v_incorrect4.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_v_incorrect5.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_v_incorrect6.setOnClickListener(v -> {
            ++counterFailed;
        });
    }

    private void resume(){
        startTimer();
        unblock_buttons();
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.GONE);
    }

    private void unblock_buttons() {
        button_n_correct1.setClickable(true);
        button_n_correct2.setClickable(true);
        button_n_correct3.setClickable(true);
        button_n_correct4.setClickable(true);
        button_n_correct5.setClickable(true);
        button_n_correct6.setClickable(true);
        button_n_correct7.setClickable(true);
        button_o_correct1.setClickable(true);
        button_o_correct2.setClickable(true);
        button_o_correct3.setClickable(true);
        button_o_correct4.setClickable(true);
        button_o_correct5.setClickable(true);
        button_o_correct6.setClickable(true);
        button_v_correct1.setClickable(true);
        button_v_correct2.setClickable(true);
        button_v_correct3.setClickable(true);
        button_v_correct4.setClickable(true);
        button_v_correct5.setClickable(true);
        button_v_correct6.setClickable(true);
        button_v_correct7.setClickable(true);
        button_v_correct9.setClickable(true);
        button_v_correct10.setClickable(true);
    }

    private void pause_menu(){
        timer_1.cancel();
        block_buttons();
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);
    }

    private void block_buttons() {
        button_n_correct1.setClickable(false);
        button_n_correct2.setClickable(false);
        button_n_correct3.setClickable(false);
        button_n_correct4.setClickable(false);
        button_n_correct5.setClickable(false);
        button_n_correct6.setClickable(false);
        button_n_correct7.setClickable(false);
        button_o_correct1.setClickable(false);
        button_o_correct2.setClickable(false);
        button_o_correct3.setClickable(false);
        button_o_correct4.setClickable(false);
        button_o_correct5.setClickable(false);
        button_o_correct6.setClickable(false);
        button_v_correct1.setClickable(false);
        button_v_correct2.setClickable(false);
        button_v_correct3.setClickable(false);
        button_v_correct4.setClickable(false);
        button_v_correct5.setClickable(false);
        button_v_correct6.setClickable(false);
        button_v_correct7.setClickable(false);
        button_v_correct9.setClickable(false);
        button_v_correct10.setClickable(false);
    }

    private void startTimer() {
        timer_1 = new CountDownTimer(time_left, 1000) {
            public void onTick(long millisUntilFinished) { time_left=millisUntilFinished;}
            public void onFinish() {
                if(counterCorrect != 0 || counterFailed != 0) {
                    counterFailed = counterFailed + maxCorrect - counterCorrect;
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

    public void Close(View view) {
        timer_1.cancel();
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
