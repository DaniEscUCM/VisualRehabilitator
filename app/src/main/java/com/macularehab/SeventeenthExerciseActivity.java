package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.exercises.ShowResultActivity;

public class SeventeenthExerciseActivity  extends AppCompatActivity {

    private final int exercise_id = 16, maxCorrect = 27;
    private int counterCorrect, counterFailed, num_miliseconds,total=0;
    private CountDownTimer timer_1 = null;
    private boolean n_correct1,n_correct2,n_correct3,n_correct4,n_correct5,n_correct6,n_correct7,n_correct8,n_correct9,n_correct10;
    private boolean o_correct1,o_correct2,o_correct3,o_correct4,o_correct5,o_correct6,o_correct7;
    private boolean v_correct1,v_correct2,v_correct3,v_correct4,v_correct5,v_correct6,v_correct7,v_correct8,v_correct9,v_correct10;

    private ImageButton button_n_correct1,button_n_correct2,button_n_correct3,button_n_correct4,button_n_correct5,button_n_correct6,button_n_correct7,button_n_correct8,button_n_correct9,button_n_correct10;
    private ImageButton button_o_correct1,button_o_correct2,button_o_correct3,button_o_correct4,button_o_correct5,button_o_correct6,button_o_correct7;
    private ImageButton button_v_correct1,button_v_correct2,button_v_correct3,button_v_correct4,button_v_correct5,button_v_correct6,button_v_correct7,button_v_correct8,button_v_correct9,button_v_correct10;

    private long time_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventeenth_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);;

        ImageButton button_pause = findViewById(R.id.pause_button);
        button_pause.setOnClickListener(v -> pause_menu());

        ImageButton button_resume = findViewById(R.id.return_button);
        button_resume.setOnClickListener(v->resume());

        ImageButton button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(v -> Close(v));

        counterCorrect = counterFailed = 0;
        num_miliseconds = SeventeenthExerciseDescriptionActivity.getNumSeconds() * 1000;
        //SEQUENCE N:
         button_n_correct1 = findViewById(R.id.button_n_correct1);
         button_n_correct2 = findViewById(R.id.button_n_correct2);
         button_n_correct3 = findViewById(R.id.button_n_correct3);
         button_n_correct4 = findViewById(R.id.button_n_correct4);
         button_n_correct5 = findViewById(R.id.button_n_correct5);
         button_n_correct6 = findViewById(R.id.button_n_correct6);
         button_n_correct7 = findViewById(R.id.button_n_correct7);
         button_n_correct8 = findViewById(R.id.button_n_correct8);
         button_n_correct9 = findViewById(R.id.button_n_correct9);
         button_n_correct10 = findViewById(R.id.button_n_correct10);
        ImageButton button_n_incorrect1 = findViewById(R.id.button_n_incorrect1);
        ImageButton button_n_incorrect2 = findViewById(R.id.button_n_incorrect2);
        ImageButton button_n_incorrect3 = findViewById(R.id.button_n_incorrect3);
        ImageButton button_n_incorrect4 = findViewById(R.id.button_n_incorrect4);
        ImageButton button_n_incorrect5 = findViewById(R.id.button_n_incorrect5);
        ImageButton button_n_incorrect6 = findViewById(R.id.button_n_incorrect6);
        ImageButton button_n_incorrect7 = findViewById(R.id.button_n_incorrect7);

        //SEQUENCE O:
         button_o_correct1 = findViewById(R.id.button_o_correct1);
         button_o_correct2 = findViewById(R.id.button_o_correct2);
         button_o_correct3 = findViewById(R.id.button_o_correct3);
         button_o_correct4 = findViewById(R.id.button_o_correct4);
         button_o_correct5 = findViewById(R.id.button_o_correct5);
         button_o_correct6 = findViewById(R.id.button_o_correct6);
         button_o_correct7 = findViewById(R.id.button_o_correct7);
        ImageButton button_o_incorrect1 = findViewById(R.id.button_o_incorrect1);
        ImageButton button_o_incorrect2 = findViewById(R.id.button_o_incorrect2);
        ImageButton button_o_incorrect3 = findViewById(R.id.button_o_incorrect3);
        ImageButton button_o_incorrect4 = findViewById(R.id.button_o_incorrect4);
        ImageButton button_o_incorrect5 = findViewById(R.id.button_o_incorrect5);
        ImageButton button_o_incorrect6 = findViewById(R.id.button_o_incorrect6);

        //SEQUENCE V:
         button_v_correct1 = findViewById(R.id.button_v_correct1);
         button_v_correct2 = findViewById(R.id.button_v_correct2);
         button_v_correct3 = findViewById(R.id.button_v_correct3);
         button_v_correct4 = findViewById(R.id.button_v_correct4);
         button_v_correct5 = findViewById(R.id.button_v_correct5);
         button_v_correct6 = findViewById(R.id.button_v_correct6);
         button_v_correct7 = findViewById(R.id.button_v_correct7);
         button_v_correct8 = findViewById(R.id.button_v_correct8);
         button_v_correct9 = findViewById(R.id.button_v_correct9);
         button_v_correct10 = findViewById(R.id.button_v_correct10);
        ImageButton button_v_incorrect1 = findViewById(R.id.button_v_incorrect1);
        ImageButton button_v_incorrect2 = findViewById(R.id.button_v_incorrect2);
        ImageButton button_v_incorrect3 = findViewById(R.id.button_v_incorrect3);
        ImageButton button_v_incorrect4 = findViewById(R.id.button_v_incorrect4);
        ImageButton button_v_incorrect5 = findViewById(R.id.button_v_incorrect5);
        ImageButton button_v_incorrect6 = findViewById(R.id.button_v_incorrect6);
        ImageButton button_v_incorrect7 = findViewById(R.id.button_v_incorrect7);

        n_correct1 = n_correct2 = n_correct3 = n_correct4 = n_correct5 = true;
        n_correct6 = n_correct7 = n_correct8 = n_correct9 = n_correct10 = true;
        o_correct1 = o_correct2 = o_correct3 = o_correct4 = o_correct5 = o_correct6 = o_correct7 = true;
        v_correct1 = v_correct2 = v_correct3 = v_correct4 = v_correct5 = true;
        v_correct6 = v_correct7 = v_correct8 = v_correct9 = v_correct10 = true;

        time_left=num_miliseconds;
        startTimer();

        //SEQUENCE N:
        //corrects:
        button_n_correct1.setOnClickListener(v -> {
            if (n_correct1) {
                n_correct1 = false;
                ++counterCorrect;
                ++total;
                button_n_correct1.getBackground().setAlpha(200);
                button_n_correct1.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_n_correct2.setOnClickListener(v -> {
            if (n_correct2) {
                n_correct2 = false;
                ++counterCorrect;
                ++total;
                button_n_correct2.getBackground().setAlpha(200);
                button_n_correct2.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_n_correct3.setOnClickListener(v -> {
            if (n_correct3) {
                n_correct3 = false;
                ++counterCorrect;
                ++total;
                button_n_correct3.getBackground().setAlpha(200);
                button_n_correct3.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_n_correct4.setOnClickListener(v -> {
            if (n_correct4) {
                n_correct4 = false;
                ++counterCorrect;
                ++total;
                button_n_correct4.getBackground().setAlpha(200);
                button_n_correct4.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_n_correct5.setOnClickListener(v -> {
            if (n_correct5) {
                n_correct5 = false;
                ++counterCorrect;
                ++total;
                button_n_correct5.getBackground().setAlpha(200);
                button_n_correct5.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_n_correct6.setOnClickListener(v -> {
            if (n_correct6) {
                n_correct6 = false;
                ++counterCorrect;
                ++total;
                button_n_correct6.getBackground().setAlpha(200);
                button_n_correct6.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_n_correct7.setOnClickListener(v -> {
            if (n_correct7) {
                n_correct7 = false;
                ++counterCorrect;
                ++total;
                button_n_correct7.getBackground().setAlpha(200);
                button_n_correct7.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_n_correct8.setOnClickListener(v -> {
            if (n_correct8) {
                n_correct8 = false;
                ++counterCorrect;
                ++total;
                button_n_correct8.getBackground().setAlpha(200);
                button_n_correct8.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_n_correct9.setOnClickListener(v -> {
            if (n_correct9) {
                n_correct9 = false;
                ++counterCorrect;
                ++total;
                button_n_correct9.getBackground().setAlpha(200);
                button_n_correct9.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_n_correct10.setOnClickListener(v -> {
            if (n_correct10) {
                n_correct10 = false;
                ++counterCorrect;
                ++total;
                button_n_correct10.getBackground().setAlpha(200);
                button_n_correct10.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
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
        button_n_incorrect6.setOnClickListener(v -> {
            ++counterFailed;
        });
        button_n_incorrect7.setOnClickListener(v -> {
            ++counterFailed;
        });

        //SEQUENCE O:
        button_o_correct1.setOnClickListener(v -> {
            if (o_correct1) {
                o_correct1 = false;
                ++counterCorrect;
                ++total;
                button_o_correct1.getBackground().setAlpha(200);
                button_o_correct1.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_o_correct2.setOnClickListener(v -> {
            if (o_correct2) {
                o_correct2 = false;
                ++counterCorrect;
                ++total;
                button_o_correct2.getBackground().setAlpha(200);
                button_o_correct2.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_o_correct3.setOnClickListener(v -> {
            if (o_correct3) {
                o_correct3 = false;
                ++counterCorrect;
                ++total;
                button_o_correct3.getBackground().setAlpha(200);
                button_o_correct3.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_o_correct4.setOnClickListener(v -> {
            if (o_correct4) {
                o_correct4 = false;
                ++counterCorrect;
                ++total;
                button_o_correct4.getBackground().setAlpha(200);
                button_o_correct4.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_o_correct5.setOnClickListener(v -> {
            if (o_correct5) {
                o_correct5 = false;
                ++counterCorrect;
                ++total;
                button_o_correct5.getBackground().setAlpha(200);
                button_o_correct5.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_o_correct6.setOnClickListener(v -> {
            if (o_correct6) {
                o_correct6 = false;
                ++counterCorrect;
                ++total;
                button_o_correct6.getBackground().setAlpha(200);
                button_o_correct6.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_o_correct7.setOnClickListener(v -> {
            if (o_correct7) {
                o_correct7 = false;
                ++counterCorrect;
                ++total;
                button_o_correct7.getBackground().setAlpha(200);
                button_o_correct7.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
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


        //SEQUENCE V:
        //corrects:
        button_v_correct1.setOnClickListener(v -> {
            if (v_correct1) {
                v_correct1 = false;
                ++counterCorrect;
                ++total;
                button_v_correct1.getBackground().setAlpha(200);
                button_v_correct1.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_v_correct2.setOnClickListener(v -> {
            if (v_correct2) {
                v_correct2 = false;
                ++counterCorrect;
                ++total;
                button_v_correct2.getBackground().setAlpha(200);
                button_v_correct2.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_v_correct3.setOnClickListener(v -> {
            if (v_correct3) {
                v_correct3 = false;
                ++counterCorrect;
                ++total;
                button_v_correct3.getBackground().setAlpha(200);
                button_v_correct3.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_v_correct4.setOnClickListener(v -> {
            if (v_correct4) {
                v_correct4 = false;
                ++counterCorrect;
                ++total;
                button_v_correct4.getBackground().setAlpha(200);
                button_v_correct4.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_v_correct5.setOnClickListener(v -> {
            if (v_correct5) {
                v_correct5 = false;
                ++counterCorrect;
                ++total;
                button_v_correct5.getBackground().setAlpha(200);
                button_v_correct5.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_v_correct6.setOnClickListener(v -> {
            if (v_correct6) {
                v_correct6 = false;
                ++counterCorrect;
                ++total;
                button_v_correct6.getBackground().setAlpha(200);
                button_v_correct6.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_v_correct7.setOnClickListener(v -> {
            if (v_correct7) {
                v_correct7 = false;
                ++counterCorrect;
                ++total;
                button_v_correct7.getBackground().setAlpha(200);
                button_v_correct7.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_v_correct8.setOnClickListener(v -> {
            if (v_correct8) {
                v_correct8 = false;
                ++counterCorrect;
                ++total;
                button_v_correct8.getBackground().setAlpha(200);
                button_v_correct8.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_v_correct9.setOnClickListener(v -> {
            if (v_correct9) {
                v_correct9 = false;
                ++counterCorrect;
                ++total;
                button_v_correct9.getBackground().setAlpha(200);
                button_v_correct9.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_v_correct10.setOnClickListener(v -> {
            if (v_correct10) {
                v_correct10 = false;
                ++counterCorrect;
                ++total;
                button_v_correct10.getBackground().setAlpha(200);
                button_v_correct10.setAlpha((float)1.0);
                if(total==maxCorrect){
                    finish_exercise();
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
        button_v_incorrect7.setOnClickListener(v -> {
            ++counterFailed;
        });
    }

    private void finish_exercise(){
        timer_1.cancel();
        counterFailed = counterFailed + maxCorrect - counterCorrect;
        writeResultInDataBase(counterCorrect, counterFailed);
        showResults(counterCorrect, counterFailed);
        finish();
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
        button_n_correct8.setClickable(true);
        button_n_correct9.setClickable(true);
        button_n_correct10.setClickable(true);
        button_o_correct1.setClickable(true);
        button_o_correct2.setClickable(true);
        button_o_correct3.setClickable(true);
        button_o_correct4.setClickable(true);
        button_o_correct5.setClickable(true);
        button_o_correct6.setClickable(true);
        button_o_correct7.setClickable(true);
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
        button_n_correct8.setClickable(false);
        button_n_correct9.setClickable(false);
        button_n_correct10.setClickable(false);
        button_o_correct1.setClickable(false);
        button_o_correct2.setClickable(false);
        button_o_correct3.setClickable(false);
        button_o_correct4.setClickable(false);
        button_o_correct5.setClickable(false);
        button_o_correct6.setClickable(false);
        button_o_correct7.setClickable(false);
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
            public void onTick(long millisUntilFinished) {time_left=millisUntilFinished; }
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
