package com.macularehab;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.exercises.SaveFocusInfo;
import com.macularehab.exercises.ShowResultActivity;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class NinthExerciseActivity extends AppCompatActivity {
    //Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private HashMap<String, Object> patientHashMap;
    private final int exercise_id = 8, total = 22, num_shapes = 5;
    private int counter, counterCorrect, counterFailed, num_miliseconds, previous_1,previous_2;
    private boolean letter_M_1, letter_M_2;
    private CountDownTimer timer_1 = null, timer_2 = null, timer_focus=null;
    private long time_left_1=3000;
    private long time_left_2=3000;
    private long time_left_focus=5000;
    private String filenameCurrentUser = "CurrentPatient.json";

    private final String isFocus = "focusIsOn";
    private boolean isOn;
    private ImageView focus_1;
    private ImageView focus_2;
    private Button button_1;
    private Button button_2;
    private boolean hiden=false;

    private int size_focus;
    private int metric_unit, size;
    private ArrayList<Pair<Float, Float>> coor_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninth_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();

        ReadInternalStorage readIS = new ReadInternalStorage();
        HashMap<String, Object> patientHashMap = readIS.read(getApplicationContext(), filenameCurrentUser);

        ImageButton button_pause = findViewById(R.id.pause_button);
        button_pause.setOnClickListener(v -> pause_menu());

        ImageButton button_resume = findViewById(R.id.return_button);
        button_resume.setOnClickListener(v->resume());

        ImageButton settingsButton = findViewById(R.id.settingButton);
        settingsButton.setOnClickListener(v -> gotToSettings());

        counterCorrect = counterFailed = 0;
        counter = previous_1 = previous_2 = -1;
        letter_M_1 =  letter_M_2 = false;
        num_miliseconds = NinthExerciseDescriptionActivity.getNumSeconds() * 1000;
        time_left_1=num_miliseconds;
        time_left_2=num_miliseconds;

        button_1 = findViewById(R.id.button_1);
        button_2 = findViewById(R.id.button_2);
        Display display_measure = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display_measure.getSize(point);

        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit = (int) Math.round(display.xdpi * 0.19685); //0.5cm
        size = metric_unit * 20;//10cm
        if(size>point.y){
            metric_unit = (int) Math.floor(point.y/(double) 20);
            size= metric_unit*20;
        }
        focus_1 = findViewById(R.id.focus_1);
        focus_1.getLayoutParams().width = size;
        focus_1.getLayoutParams().height = size;
        focus_1.requestLayout();

        HashMap<String, Object> map = readIS.read(getApplicationContext(), filenameCurrentUser);
        isOn=(Boolean) map.get(isFocus);
        LinkedTreeMap tree = (LinkedTreeMap) map.get("focus");
        coor_result = new ArrayList<>();
        coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));
        size_focus = (int) Math.round(metric_unit * (double) map.get("focus_size"));

        drawFocusDot1();

        focus_2 = findViewById(R.id.focus_2);
        focus_2.getLayoutParams().width = size;
        focus_2.getLayoutParams().height = size;
        focus_2.requestLayout();

        size_focus = (int) Math.round(metric_unit * (double) map.get("focus_size"));

        drawFocusDot2();

        if(isOn){
            button_1.setVisibility(View.INVISIBLE);
            button_2.setVisibility(View.INVISIBLE);
            startTimerFoco(button_1, button_2); //Durante 5s solo se ve el foco
        }
        else{
            focus_1.setVisibility(View.INVISIBLE);
            focus_2.setVisibility(View.INVISIBLE);
            move_button_1();
            move_button_2();
        }

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (letter_M_1) {
                    ++counterCorrect;
                } else {
                    ++counterFailed; //they clicked when they shouldn't have
                }
                cancelTimer_1();
                move_button_1();
            }
        });

        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (letter_M_2) {
                    ++counterCorrect;
                } else {
                    ++counterFailed; //they clicked when they shouldn't have
                }
                cancelTimer_2();
                move_button_2();
            }
        });


        ImageButton button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });

        setUiListener();
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

    private void gotToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void drawFocusDot1(){
        Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btm_manual_left);
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, size_focus / (float) 2, metric_unit, Color.RED);
        all_dots.draw(canvas);
        focus_1.setImageBitmap(btm_manual_left);
        focus_1.setVisibility(View.VISIBLE);
    }
    private void drawFocusDot2(){
        Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btm_manual_left);
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, size_focus / (float) 2, metric_unit, Color.RED);
        all_dots.draw(canvas);
        focus_2.setImageBitmap(btm_manual_left);
        focus_2.setVisibility(View.VISIBLE);
    }
    private void resume(){
        ReadInternalStorage readIS = new ReadInternalStorage();
        HashMap<String, Object> map = readIS.read(getApplicationContext(), filenameCurrentUser);
        isOn=(Boolean) map.get(isFocus);

        if(isOn){
            size_focus =  (int) Math.round(metric_unit * (double) map.get("focus_size"));
            drawFocusDot1();
            drawFocusDot2();
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
        menu.setVisibility(View.GONE);
    }

    private void pause_menu(){
        if(hiden){
            timer_focus.cancel();
        }else{
            timer_1.cancel();
            timer_2.cancel();
        }
        button_1.setClickable(false);
        button_2.setClickable(false);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);
    }


    private void startTimerFoco(Button button_1, Button button_2) {
        hiden=true;
        timer_focus = new CountDownTimer(time_left_focus, 1000) {
            public void onTick(long millisUntilFinished) {time_left_focus=millisUntilFinished; }
            public void onFinish() {
                hiden=false;
                button_1.setVisibility(View.VISIBLE);
                button_2.setVisibility(View.VISIBLE);
                move_button_1();
                move_button_2();
            }
        };
        timer_focus.start();
    }

    private void startTimer() { //counter of shape 1
        timer_1 = new CountDownTimer(time_left_1, 1000) {
            public void onTick(long millisUntilFinished) {time_left_1=millisUntilFinished;  }
            public void onFinish() {
                if(letter_M_1) {++counterFailed;} //they didn't touch when they should have.
                else{++counterCorrect;}
                move_button_1();
            }
        };
        timer_1.start();
    }

    private void startTimer_button2() { // counter of shape 2
        timer_2 = new CountDownTimer(time_left_2, 1000) {
            public void onTick(long millisUntilFinished) {time_left_2=millisUntilFinished; }
            public void onFinish() {
                if(letter_M_2) {++counterFailed;} //they didn't touch when they should have.
                else{++counterCorrect;}
                move_button_2();
            }
        };
        timer_2.start();
    }

    private void cancelTimer_1() {
        if (timer_1 != null)
            timer_1.cancel();
    }

    private void cancelTimer_2() {
        if (timer_2 != null)
            timer_2.cancel();
    }

    private void move_button_1() {
        if (++counter == total) {
            writeResultInDataBase(counterCorrect, counterFailed);
            System.out.println("counter: " + counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);

            //Toast Message
            Resources resources = this.getResources();
            String correctsString = resources.getString(R.string.exercises_results_toast_message_correctText);
            String incorrectsString = resources.getString(R.string.exercises_results_toast_message_incorrectText);
            String ofTotalString = resources.getString(R.string.exercises_results_toast_message_ofTotalText);

            String message_correct = correctsString + " " + counterCorrect + " " + incorrectsString + " " + counterFailed + " " + ofTotalString + " " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();

            finish();
        } else {
            System.out.println("counter: " + counter);
            System.out.println("previous_1: " + previous_1 + ". previous_2: " + previous_2);
            int rand1;
            do {
                System.out.println("previous_1: " + previous_1);
                rand1 = new Random().nextInt(num_shapes);
                System.out.println("rand1: " + rand1);

            } while (previous_1==rand1);
            previous_1 = rand1;
            System.out.println("previous_1: " + previous_1 + ". rand1: " + rand1);

            Button button_1 = findViewById(R.id.button_1);
            time_left_1=num_miliseconds;
            startTimer();
            if(previous_1 == 0) {
                button_1.setText("T");
                letter_M_1 = false;
            } else if (previous_1 == 1) {
                button_1.setText("M");
                letter_M_1 = true;
            } else if(previous_1 == 2){
                button_1.setText("E");
                letter_M_1 = false;
            } else if(previous_1 == 3){
                button_1.setText("L");
                letter_M_1 = false;
            } else {
                button_1.setText("A");
                letter_M_1 = false;
            }
        }
    }

    private void move_button_2() {
        if (++counter == total) {
            writeResultInDataBase(counterCorrect, counterFailed);
            System.out.println("counter: " + counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);

            //Toast Message
            Resources resources = this.getResources();
            String correctsString = resources.getString(R.string.exercises_results_toast_message_correctText);
            String incorrectsString = resources.getString(R.string.exercises_results_toast_message_incorrectText);
            String ofTotalString = resources.getString(R.string.exercises_results_toast_message_ofTotalText);

            String message_correct = correctsString + " " + counterCorrect + " " + incorrectsString + " " + counterFailed + " " + ofTotalString + " " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();

            finish();
        } else {
            System.out.println("counter: " + counter + ". previous_2: " + previous_2);
            int rand2;
            do {
                System.out.println("previous_2: " + previous_2);
                rand2 = new Random().nextInt(num_shapes);
                System.out.println("rand2: " + rand2);
            } while (previous_2==rand2);
            previous_2 = rand2;
            System.out.println("previous_2: " + previous_2);

            Button button_2 = findViewById(R.id.button_2);
            time_left_2=num_miliseconds;
            startTimer_button2();
            if(previous_2 == 0) {
                button_2.setText("T");
                letter_M_2 = false;
            } else if (previous_2 == 1) {
                button_2.setText("M");
                letter_M_2 = true;
            } else if(previous_2 == 2){
                button_2.setText("E");
                letter_M_2 = false;
            } else if(previous_2 == 3){
                button_2.setText("L");
                letter_M_2 = false;
            } else {
                button_2.setText("A");
                letter_M_2 = false;
            }
        }
    }

    public void Close(View view) {
        counter = total + 1;
        finish();
    }

    //Database
    /**
     * [EN] Writes the result of the exercise in the database and in internal storage
     * [ES] Escribe el resultado del ejercicio en la base de datos y en el almacenamiento interno
     *
     * @param correct
     *          [En] Number of corrects
     *          [ES] Número de aciertos
     * @param failed
     *          [En] Number of failures
     *          [ES] Número de fallos
     */
    private void writeResultInDataBase(int correct, int failed) {

        ExerciseWriteDB exerciseWriteDB = new ExerciseWriteDB(exercise_id);
        exerciseWriteDB.writeResultInDataBase(getApplicationContext(), correct, failed, 0);

        showResults(correct, failed);
    }

    /**
     * [EN] Starts ShowResultActivity,
     *      and passes two intents, the number of corrects and number of failures
     * [ES] Comienza nueva actividad,
     *      y pasa dos intents, el número de aciertos y el número de fallos
     *
     * @param correct
     *          [En] Number of corrects
     *          [ES] Número de aciertos
     * @param failed
     *          [En] Number of failures
     *          [ES] Número de fallos
     */
    private void showResults(int correct, int failed) {

        Intent resultIntent = new Intent(this, ShowResultActivity.class);
        resultIntent.putExtra("numCorrect", correct);
        resultIntent.putExtra("numFailed", failed);
        startActivity(resultIntent);
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