package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TwelfthExerciseActivity extends AppCompatActivity {

    private final int exercise_id = 11, total = 10, num_shapes = 4;
    private int counter, counterCorrect, counterFailed, num_miliseconds, current, metric_unit, size;
    private CountDownTimer timer=null,timer_focus = null;
    private long time_left=3000,time_left_focus=5000;
    private HashMap<String, Object> patientHashMap;
    private String filenameCurrentUser = "CurrentPatient.json";

    private final String isFocus = "focusIsOn";
    private boolean isOn;
    private ImageView focus;
    private ImageButton button_button_left_eye;
    private ImageButton button_right_eye;
    private ImageButton button_mouth;
    private ImageButton button_nose;
    private boolean hiden=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twelfth_exercise);
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
        isOn=(Boolean) patientHashMap.get(isFocus);
        focus_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isOn=!isOn;
        });

        counterCorrect = counterFailed = 0;
        counter = current = -1;
        num_miliseconds = TwelfthExerciseDescriptionActivity.getNumSeconds() * 1000;
        time_left=num_miliseconds;

        button_button_left_eye = findViewById(R.id.dot_button_left_eye);
        button_right_eye = findViewById(R.id.dot_button_right_eye);
        button_mouth = findViewById(R.id.dot_button_mouth);
        button_nose = findViewById(R.id.dot_button_nose);
        ImageView photo = findViewById(R.id.image_background);

        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit = (int) Math.round(display.xdpi * 0.19685); //0.5cm
        size = metric_unit * 20;//10cm
        move();

        /*
         * 0 left eye
         * 1 right eye
         * 2 nose
         * 3 mouth
         *
         * */

        button_button_left_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 0) {
                    ++counterCorrect;
                } else {
                    ++counterFailed;
                }
                cancelTimer_1();
                move();
            }
        });
        button_right_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 1) {
                    ++counterCorrect;
                } else {
                    ++counterFailed;
                }
                cancelTimer_1();
                move();
            }
        });
        button_nose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 2) {
                    ++counterCorrect;
                } else {
                    ++counterFailed;
                }
                cancelTimer_1();
                move();
            }
        });
        button_mouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 3) {
                    ++counterCorrect;
                } else {
                    ++counterFailed;
                }
                cancelTimer_1();
                move();
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

    private void startTimerFoco() {
        hiden=true;
        timer_focus = new CountDownTimer(time_left_focus, 1000) {
            public void onTick(long millisUntilFinished) { time_left_focus=millisUntilFinished;}
            public void onFinish() {
                hiden=false;
                startTimer();
            }
        };
        timer_focus.start();
    }

    private void focus_function () {
        ArrayList<Pair<Float, Float>> coor_result;
        LinkedTreeMap tree = (LinkedTreeMap) patientHashMap.get("focus");
        coor_result = new ArrayList<>();
        coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));

        if (current == 0) {
            focus = findViewById(R.id.focus_left_eye);
            focus.getLayoutParams().width = size;
            focus.getLayoutParams().height = size;
            focus.requestLayout();
            Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(btm_manual_left);
            DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
            all_dots.draw(canvas);
            focus.setImageBitmap(btm_manual_left);
            focus.setVisibility(View.VISIBLE);
            startTimerFoco();
        }
        else if (current == 1) {
            focus = findViewById(R.id.focus_right_eye);
            focus.getLayoutParams().width = size;
            focus.getLayoutParams().height = size;
            focus.requestLayout();
            Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(btm_manual_left);
            DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
            all_dots.draw(canvas);
            focus.setImageBitmap(btm_manual_left);
            focus.setVisibility(View.VISIBLE);
            startTimerFoco();
        }
        else if (current == 2) {
            focus = findViewById(R.id.focus_nose);
            focus.getLayoutParams().width = size;
            focus.getLayoutParams().height = size;
            focus.requestLayout();
            Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(btm_manual_left);
            DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
            all_dots.draw(canvas);
            focus.setImageBitmap(btm_manual_left);
            focus.setVisibility(View.VISIBLE);
            startTimerFoco();
        }
        else{
            focus = findViewById(R.id.focus_mouth);
            focus.getLayoutParams().width = size;
            focus.getLayoutParams().height = size;
            focus.requestLayout();
            Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(btm_manual_left);
            DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
            all_dots.draw(canvas);
            focus.setImageBitmap(btm_manual_left);
            focus.setVisibility(View.VISIBLE);
            startTimerFoco();
        }

    }

    private void startTimer() {
        timer = new CountDownTimer(time_left, 1000) {
            public void onTick(long millisUntilFinished) { time_left=millisUntilFinished;}
            public void onFinish() {
                ++counterFailed; //they didn't touch when they should have.
                move();
            }
        };
        timer.start();
    }

    private void cancelTimer_1() {
        if (timer != null)
            timer.cancel();
    }

    private void resume(){
        if(isOn){
            if(hiden){
                startTimerFoco();
            }
            else{
                focus_function();
            }
        }
        else{
            if(focus!=null) {
                focus.setVisibility(View.INVISIBLE);
                if (hiden) {
                    hiden = false;
                }
            }
            startTimer();
        }
        button_button_left_eye.setClickable(true);
        button_right_eye.setClickable(true);
        button_mouth.setClickable(true);
        button_nose.setClickable(true);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.GONE);
    }

    private void pause_menu(){
        if(hiden){
            timer_focus.cancel();
        }else {
            timer.cancel();
        }
        button_button_left_eye.setClickable(false);
        button_right_eye.setClickable(false);
        button_mouth.setClickable(false);
        button_nose.setClickable(false);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);
    }

    private void saveFocusOn(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
        HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentUser);

        mapS.put(isFocus, isOn);

        Gson gson = new Gson();
        String data = gson.toJson(mapS);
        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameCurrentUser, data);
        databaseReference.child("Professional").child((String) mapS.get("professional_uid")).
                child("Patients").child((String) mapS.get("patient_numeric_code")).child(isFocus).setValue(isOn);
    }
    private void move() {
        if (++counter == total) {
            writeResultInDataBase(counterCorrect, counterFailed);
            System.out.println("counter: " + counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
            String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            saveFocusOn();
            finish();
        } else {
            ImageView focus_left_eye = findViewById(R.id.focus_left_eye);
            ImageView focus_right_eye = findViewById(R.id.focus_right_eye);
            ImageView focus_mouth = findViewById(R.id.focus_mouth);
            ImageView focus_nose = findViewById(R.id.focus_nose);
            focus_left_eye.setVisibility(View.INVISIBLE);
            focus_right_eye.setVisibility(View.INVISIBLE);
            focus_nose.setVisibility(View.INVISIBLE);
            focus_mouth.setVisibility(View.INVISIBLE);
            time_left=num_miliseconds;
            time_left_focus=5000;
            int rand1;
            do {
                rand1 = new Random().nextInt(num_shapes);

            } while (current==rand1);
            current = rand1;
            TextView text = findViewById(R.id.text_findX);
            Resources res = TwelfthExerciseActivity.this.getResources();
            if(current == 0) {
                text.setText(res.getString(R.string.eleventh_exercise_find_left_eye));
            }
            else if(current == 1) {
                text.setText(res.getString(R.string.eleventh_exercise_find_right_eye));
            }
            else if(current == 2) {
                text.setText(res.getString(R.string.eleventh_exercise_find_nose));
            }
            else {
                text.setText(res.getString(R.string.eleventh_exercise_find_mouth));
            }
            if(isOn) {
                focus_function();
            }
            else {
                startTimer();
            }
        }
    }

    public void Close(View view) {
        counter = total + 1;
        finish();
    }



    //Database
    private void writeResultInDataBase(int correct, int failed) {
        ExerciseWriteDB exerciseWriteDB = new ExerciseWriteDB(exercise_id);
        exerciseWriteDB.writeResultInDataBase(getApplicationContext(), correct, failed, 0);
    }
}
