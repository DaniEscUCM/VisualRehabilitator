package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.macularehab.exercises.ExerciseWriteDB;
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

        ImageButton settingsButton = findViewById(R.id.settingButton);
        settingsButton.setOnClickListener(v -> gotToSettings());

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

        setUiListener();
    }

    private void gotToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    private void setUiListener() {

        View decorView = getWindow().getDecorView();

        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 2000ms
                                    hideNavigationAndStatusBar();
                                }
                            }, 2000);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideNavigationAndStatusBar();
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

        //Toast Message
        Resources resources = this.getResources();
        String correctsString = resources.getString(R.string.exercises_results_toast_message_correctText);
        String incorrectsString = resources.getString(R.string.exercises_results_toast_message_incorrectText);
        String ofTotalString = resources.getString(R.string.exercises_results_toast_message_ofTotalText);

        String message_correct = correctsString + " " + counterCorrect + " " + incorrectsString + " " + counterFailed + " " + ofTotalString + " " + total;
        Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();

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

    private void hideNavigationAndStatusBar() {

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        decorView.setSystemUiVisibility(uiOptions);
    }
}
