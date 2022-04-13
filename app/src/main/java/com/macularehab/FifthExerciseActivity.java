package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.widget.Button;
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


public class FifthExerciseActivity extends AppCompatActivity {

    protected final int exercise_id = 4, total = 13;
    private int counter, counterCorrect, counterFailed,num_miliseconds;
    private boolean is_letter_E;
    private CountDownTimer timer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        String filenameCurrentUser = "CurrentPatient.json";
        ReadInternalStorage readIS = new ReadInternalStorage();
        HashMap<String, Object> patientHashMap = readIS.read(getApplicationContext(), filenameCurrentUser);
        counter = -1;
        counterCorrect = counterFailed = 0;
        is_letter_E = false;
        num_miliseconds = FifthExerciseDescriptionActivity.getNumSeconds() * 1000;
        boolean focus_on = (boolean) patientHashMap.get("focusIsOn");
        Button button_dot = findViewById(R.id.button);
        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm
        ImageView focus = findViewById(R.id.foco);

        if(focus_on){
            button_dot.setVisibility(View.INVISIBLE);
            ArrayList<Pair<Float, Float>> coor_result;
            LinkedTreeMap tree= (LinkedTreeMap)patientHashMap.get("focus");
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
        }
        else{
            focus.setVisibility(View.INVISIBLE);
            move();
        }
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_letter_E) {++counterCorrect;}
                else {++counterFailed;}
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

    private void startTimerFoco(Button button_dot) {     //Timer para que aparezca el foco solo 5s
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
                if(is_letter_E) {++counterFailed;} //they didn't touch when they should have.
                else {++counterCorrect;}
                move(); }
        };
        timer.start();
    }

    private void cancelTimer() {
        if(timer!=null)
            timer.cancel();
    }

    private void move(){
        if(++counter == total) {
            writeResultInDataBase(counterCorrect, counterFailed);
            System.out.println("counter: "+ counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
            String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            Button button_dot = findViewById(R.id.button);
            System.out.println("counter: " + counter);
            is_letter_E = false;
            startTimer();
            if (counter == 2 || counter == 6 || counter == 11) {
                button_dot.setText("T");
                System.out.println("counter: " + counter + ". T");
            } else if (counter == 0 || counter == 10) {
                button_dot.setText("M");
                System.out.println("counter: " + counter + ". M");
            } else if (counter == 1 || counter == 4 || counter == 12) {
                is_letter_E = true;
                button_dot.setText("E");
                System.out.println("counter: " + counter + ". E");
            } else if (counter == 5 || counter == 8) {
                button_dot.setText("L");
                System.out.println("counter: " + counter + ". L");
            } else { //3,7,12
                button_dot.setText("F");
                System.out.println("counter: " + counter + ". F");
            }
        }
    }

    public void Close(View view){
        counter = total + 1;
        System.out.println("counter: "+ counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
        String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
        Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
        finish();
    }

    public void Settings(View view){
        counter = total + 1;
        finish(); //para que termine el ejercicio y no siga funcionando mientras esta en settings
        Intent i = new Intent( this, SettingsActivity.class );
        startActivity(i);
    }

    public int getNumCorrect(){
        return counterCorrect;
    }
    public int getNumFailed(){ return counterFailed; }

    //Database
    private void writeResultInDataBase(int correct, int failed) {

        ExerciseWriteDB exerciseWriteDB = new ExerciseWriteDB(exercise_id);
        exerciseWriteDB.writeResultInDataBase(getApplicationContext(), correct, failed, 0);
    }
}
