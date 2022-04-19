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

import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.exercises.SaveFocusInfo;
import com.macularehab.exercises.ShowResultActivity;
import com.macularehab.internalStorage.ReadInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class FifteenthExerciseActivity  extends AppCompatActivity {

    private final int exercise_id = 14, maxCorrect = 27;
    private int counterCorrect, counterFailed, num_miliseconds, metric_unit, size;
    private boolean focus_on;
    private CountDownTimer timer_1 = null;
    private HashMap<String, Object> patientHashMap;
    private boolean n_correct1,n_correct2,n_correct3,n_correct4,n_correct5,n_correct6,n_correct7,n_correct8,n_correct9,n_correct10;
    private boolean o_correct1,o_correct2,o_correct3,o_correct4,o_correct5,o_correct6,o_correct7;
    private boolean v_correct1,v_correct2,v_correct3,v_correct4,v_correct5,v_correct6,v_correct7,v_correct8,v_correct9,v_correct10;
    private String filenameCurrentUser = "CurrentPatient.json";

    private final String isFocus = "focusIsOn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifteenth_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        String filenameCurrentUser = "CurrentPatient.json";

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

        counterCorrect = counterFailed = 0;
        num_miliseconds = FifteenthExerciseDescriptionActivity.getNumSeconds() * 1000;
        focus_on = (boolean) patientHashMap.get("focusIsOn");
        //SEQUENCE N:
        Button button_n_correct1 = (Button)findViewById(R.id.button_n_correct1);
        Button button_n_correct2 = (Button)findViewById(R.id.button_n_correct2);
        Button button_n_correct3 = (Button)findViewById(R.id.button_n_correct3);
        Button button_n_correct4 = (Button)findViewById(R.id.button_n_correct4);
        Button button_n_correct5 = (Button)findViewById(R.id.button_n_correct5);
        Button button_n_correct6 = (Button)findViewById(R.id.button_n_correct6);
        Button button_n_correct7 = (Button)findViewById(R.id.button_n_correct7);
        Button button_n_correct8 = (Button)findViewById(R.id.button_n_correct8);
        Button button_n_correct9 = (Button)findViewById(R.id.button_n_correct9);
        Button button_n_correct10 = (Button)findViewById(R.id.button_n_correct10);
        Button button_n_incorrect1 = (Button)findViewById(R.id.button_n_incorrect1);
        Button button_n_incorrect2 = (Button)findViewById(R.id.button_n_incorrect2);
        Button button_n_incorrect3 = (Button)findViewById(R.id.button_n_incorrect3);
        Button button_n_incorrect4 = (Button)findViewById(R.id.button_n_incorrect4);
        Button button_n_incorrect5 = (Button)findViewById(R.id.button_n_incorrect5);
        Button button_n_incorrect6 = (Button)findViewById(R.id.button_n_incorrect6);

        //SEQUENCE O:
        Button button_o_correct1 = (Button)findViewById(R.id.button_o_correct1);
        Button button_o_correct2 = (Button)findViewById(R.id.button_o_correct2);
        Button button_o_correct3 = (Button)findViewById(R.id.button_o_correct3);
        Button button_o_correct5 = (Button)findViewById(R.id.button_o_correct5);
        Button button_o_correct7 = (Button)findViewById(R.id.button_o_correct7);
        Button button_o_incorrect1 = (Button)findViewById(R.id.button_o_incorrect1);
        Button button_o_incorrect2 = (Button)findViewById(R.id.button_o_incorrect2);
        Button button_o_incorrect3 = (Button)findViewById(R.id.button_o_incorrect3);
        Button button_o_incorrect4 = (Button)findViewById(R.id.button_o_incorrect4);
        Button button_o_incorrect5 = (Button)findViewById(R.id.button_o_incorrect5);
        Button button_o_incorrect6 = (Button)findViewById(R.id.button_o_incorrect6);
        Button button_o_incorrect7 = (Button)findViewById(R.id.button_o_incorrect7);
        Button button_o_incorrect8 = (Button)findViewById(R.id.button_o_incorrect8);
        Button button_o_incorrect9 = (Button)findViewById(R.id.button_o_incorrect9);
        Button button_o_incorrect10 = (Button)findViewById(R.id.button_o_incorrect10);

        //SEQUENCE V:

        Button button_v_correct1 = (Button)findViewById(R.id.button_v_correct1);
        Button button_v_correct2 = (Button)findViewById(R.id.button_v_correct2);
        Button button_v_correct3 = (Button)findViewById(R.id.button_v_correct3);
        Button button_v_correct4 = (Button)findViewById(R.id.button_v_correct4);
        Button button_v_correct5 = (Button)findViewById(R.id.button_v_correct5);
        Button button_v_correct6 = (Button)findViewById(R.id.button_v_correct6);
        Button button_v_correct7 = (Button)findViewById(R.id.button_v_correct7);
        Button button_v_correct9 = (Button)findViewById(R.id.button_v_correct9);
        Button button_v_correct10 = (Button)findViewById(R.id.button_v_correct10);
        Button button_v_incorrect1 = (Button)findViewById(R.id.button_v_incorrect1);
        Button button_v_incorrect2 = (Button)findViewById(R.id.button_v_incorrect2);
        Button button_v_incorrect3 = (Button)findViewById(R.id.button_v_incorrect3);
        Button button_v_incorrect4 = (Button)findViewById(R.id.button_v_incorrect4);
        Button button_v_incorrect5 = (Button)findViewById(R.id.button_v_incorrect5);
        Button button_v_incorrect6 = (Button)findViewById(R.id.button_v_incorrect6);


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
        ImageView focus_o = findViewById(R.id.focus_o);
        focus_o.getLayoutParams().width = size;
        focus_o.getLayoutParams().height = size;
        focus_o.requestLayout();
        focus_o.setImageBitmap(btm_manual_left);

        ImageView focus_v = findViewById(R.id.focus_v);
        focus_v.getLayoutParams().width = size;
        focus_v.getLayoutParams().height = size;
        focus_v.requestLayout();
        focus_v.setImageBitmap(btm_manual_left);

        if (focus_on) {
            focus_function();
        } else {
            focus.setVisibility(View.INVISIBLE);
            focus_o.setVisibility(View.INVISIBLE);
            focus_v.setVisibility(View.INVISIBLE);
            startTimer();
        }

        n_correct1 = n_correct2 = n_correct3 = n_correct4 = n_correct5 = true;
        n_correct6 = n_correct7 = n_correct8 = n_correct9 = n_correct10 = true;
        o_correct1 = o_correct2 = o_correct3 = o_correct5 = o_correct7 = true;
        v_correct1 = v_correct2 = v_correct3 = v_correct4 = v_correct5 = true;
        v_correct6 = v_correct7 = v_correct9 = v_correct10 = true;

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

        //SEQUENCE O:
        button_o_correct1.setOnClickListener(v -> {
            if (o_correct1) {
                o_correct1 = false;
                ++counterCorrect;
            }
        });
        button_o_correct2.setOnClickListener(v -> {
            if (o_correct2) {
                o_correct2 = false;
                ++counterCorrect;
            }
        });
        button_o_correct3.setOnClickListener(v -> {
            if (o_correct3) {
                o_correct3 = false;
                ++counterCorrect;
            }
        });
        button_o_correct5.setOnClickListener(v -> {
            if (o_correct5) {
                o_correct5 = false;
                ++counterCorrect;
            }
        });
        button_o_correct7.setOnClickListener(v -> {
            if (o_correct7) {
                o_correct7 = false;
                ++counterCorrect;
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
            }
        });
        button_v_correct2.setOnClickListener(v -> {
            if (v_correct2) {
                v_correct2 = false;
                ++counterCorrect;
            }
        });
        button_v_correct3.setOnClickListener(v -> {
            if (v_correct3) {
                v_correct3 = false;
                ++counterCorrect;
            }
        });
        button_v_correct4.setOnClickListener(v -> {
            if (v_correct4) {
                v_correct4 = false;
                ++counterCorrect;
            }
        });
        button_v_correct5.setOnClickListener(v -> {
            if (v_correct5) {
                v_correct5 = false;
                ++counterCorrect;
            }
        });
        button_v_correct6.setOnClickListener(v -> {
            if (v_correct6) {
                v_correct6 = false;
                ++counterCorrect;
            }
        });
        button_v_correct7.setOnClickListener(v -> {
            if (v_correct7) {
                v_correct7 = false;
                ++counterCorrect;
            }
        });
        button_v_correct9.setOnClickListener(v -> {
            if (v_correct9) {
                v_correct9 = false;
                ++counterCorrect;
            }
        });
        button_v_correct10.setOnClickListener(v -> {
            if (v_correct10) {
                v_correct10 = false;
                ++counterCorrect;
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
        /*if(isOn){
            if(hiden){
                startTimerFoco(button_1,button_2);
            }
            else{
                focus_1.setVisibility(View.VISIBLE);
                focus_2.setVisibility(View.VISIBLE);
                startTimer();
                startTimer_button2();
            }
        }
        else{
            focus_1.setVisibility(View.INVISIBLE);
            focus_2.setVisibility(View.INVISIBLE);
            if(hiden){
                hiden=false;
                button_1.setVisibility(View.VISIBLE);
                button_2.setVisibility(View.VISIBLE);
                move_button_1();
                move_button_2();
            }
            startTimer();
            startTimer_button2();
        }
        button_1.setClickable(true);
        button_2.setClickable(true);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.GONE);*/
    }

    private void pause_menu(){
        /*if(hiden){
            timer_focus.cancel();
        }else{
            timer_1.cancel();
            timer_2.cancel();
        }
        button_1.setClickable(false);
        button_2.setClickable(false);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);*/
    }


    private void saveFocusOn(){

        new SaveFocusInfo(getApplicationContext(), focus_on);
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
        ImageView focus_o = findViewById(R.id.focus_o);
        ImageView focus_v = findViewById(R.id.focus_v);
        focus.setVisibility(View.VISIBLE);
        focus_o.setVisibility(View.VISIBLE);
        focus_v.setVisibility(View.VISIBLE);
        startTimerFoco();
    }

    private void startTimer() {
        timer_1 = new CountDownTimer(num_miliseconds, 1000) {
            public void onTick(long millisUntilFinished) { }
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
