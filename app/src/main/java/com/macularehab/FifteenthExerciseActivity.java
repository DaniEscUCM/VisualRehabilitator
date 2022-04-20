package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.exercises.SaveFocusInfo;
import com.macularehab.exercises.ShowResultActivity;
import com.macularehab.internalStorage.ReadInternalStorage;
import java.util.HashMap;

public class FifteenthExerciseActivity extends AppCompatActivity {

    private final int exercise_id = 14, maxCorrect = 6;
    private int counterCorrect, counterFailed = 0, num_miliseconds, total=0;
    private boolean focus_on;
    private CountDownTimer timer_1 = null;
    private HashMap<String, Object> patientHashMap;
    private boolean circle_1,circle_2,circle_3,circle_4,circle_5,circle_6;
    private String filenameCurrentUser = "CurrentPatient.json";

    private final String isFocus = "focusIsOn";
    private ImageButton button_circle_1, button_circle_2, button_circle_3, button_circle_4, button_circle_5, button_circle_6;
    private boolean dot=false;
    private long time_left=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifteenth_exercise);
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
        num_miliseconds = FifteenthExerciseDescriptionActivity.getNumSeconds() * 1000;
        time_left=num_miliseconds;

        button_circle_1 = findViewById(R.id.circle_1);
        button_circle_2 = findViewById(R.id.circle_2);
        button_circle_3 = findViewById(R.id.circle_3);
        button_circle_4 = findViewById(R.id.circle_4);
        button_circle_5 = findViewById(R.id.circle_5);
        button_circle_6 = findViewById(R.id.circle_6);

        circle_1=circle_2=circle_3=circle_4=circle_5=circle_6 = true;

        startTimer();
        button_circle_1.setOnClickListener(v -> {
            if (circle_1) {
                circle_1 = false;
                ++counterCorrect;
                ++total;
                button_circle_1.setColorFilter(Color.GREEN);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_circle_2.setOnClickListener(v -> {
            if (circle_2) {
                circle_2 = false;
                ++counterCorrect;
                ++total;
                button_circle_2.setColorFilter(Color.GREEN);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_circle_3.setOnClickListener(v -> {
            if (circle_3) {
                circle_3 = false;
                ++counterCorrect;
                ++total;
                button_circle_3.setColorFilter(Color.GREEN);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_circle_4.setOnClickListener(v -> {
            if (circle_4) {
                circle_4 = false;
                ++counterCorrect;
                ++total;
                button_circle_4.setColorFilter(Color.GREEN);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_circle_5.setOnClickListener(v -> {
            if (circle_5) {
                circle_5 = false;
                ++counterCorrect;
                ++total;
                button_circle_5.setColorFilter(Color.GREEN);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
        button_circle_6.setOnClickListener(v -> {
            if (circle_6) {
                circle_6 = false;
                ++counterCorrect;
                ++total;
                button_circle_6.setColorFilter(Color.GREEN);
                if(total==maxCorrect){
                    finish_exercise();
                }
            }
        });
    }

    private void finish_exercise(){
        timer_1.cancel();
        counterFailed = maxCorrect - counterCorrect;
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

    private void pause_menu(){
        timer_1.cancel();
        block_buttons();
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);
    }


    private void block_buttons(){
        button_circle_1.setClickable(false);
        button_circle_2.setClickable(false);
        button_circle_3.setClickable(false);
        button_circle_4.setClickable(false);
        button_circle_5.setClickable(false);
        button_circle_6.setClickable(false);
    }

    private void unblock_buttons(){
        button_circle_1.setClickable(true);
        button_circle_2.setClickable(true);
        button_circle_3.setClickable(true);
        button_circle_4.setClickable(true);
        button_circle_5.setClickable(true);
        button_circle_6.setClickable(true);
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
        counterCorrect = 0;
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
        Close();
    }
}
