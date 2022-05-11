package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.exercises.ShowResultActivity;
import com.macularehab.internalStorage.ReadInternalStorage;
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

        ImageButton settingsButton = findViewById(R.id.settingButton);
        settingsButton.setOnClickListener(v -> gotToSettings());

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

        time_left=num_miliseconds;
        startTimer();

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
