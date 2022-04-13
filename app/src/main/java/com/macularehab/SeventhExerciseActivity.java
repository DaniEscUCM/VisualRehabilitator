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

public class SeventhExerciseActivity extends AppCompatActivity {

    private final int exercise_id = 6, total = 22, num_shapes = 4;
    private int counter, counterCorrect, counterFailed, num_miliseconds, previous_1,previous_2;
    private boolean circle_1, circle_2;
    private CountDownTimer timer_1 = null, timer_2 = null;
    private long time_left_1=3000;
    private long time_left_2=3000;
    private String filenameCurrentUser = "CurrentPatient.json";

    private final String isFocus = "focusIsOn";
    private boolean isOn;
    private ImageView focus_1;
    private ImageView focus_2;
    private ImageButton button_1;
    private ImageButton button_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        ReadInternalStorage readIS = new ReadInternalStorage();
        HashMap<String, Object> patientHashMap = readIS.read(getApplicationContext(), filenameCurrentUser);

        ImageButton button_pause = findViewById(R.id.pause_button);
        button_pause.setOnClickListener(v -> pause_menu());

        ImageButton button_resume = findViewById(R.id.return_button);
        button_resume.setOnClickListener(v->resume());

        Switch focus_switch = findViewById(R.id.focus_switch1);
        focus_switch.setChecked((Boolean) patientHashMap.get(isFocus));
        isOn=(Boolean) patientHashMap.get(isFocus);
        focus_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
            HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentUser);
            isOn=!(Boolean)mapS.get(isFocus);
        });

        counterCorrect = counterFailed = 0;
        counter = previous_1 = previous_2 = -1;
        circle_1 =  circle_2 = false;
        num_miliseconds = SeventhExerciseDescriptionActivity.getNumSeconds() * 1000;
        time_left_1=num_miliseconds;
        time_left_2=num_miliseconds;
        button_1 = findViewById(R.id.button_1);
        button_2 = findViewById(R.id.button_2);
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm
        button_1.getLayoutParams().width = metric_unit*3;//1.5cm diametro de las figuras
        button_1.getLayoutParams().height = metric_unit*3;
        button_2.getLayoutParams().width = metric_unit*3;
        button_2.getLayoutParams().height = metric_unit*3;

        focus_1 = findViewById(R.id.focus_1);
        focus_2 = findViewById(R.id.focus_2);

        ArrayList<Pair<Float, Float>> coor_result;
        LinkedTreeMap tree= (LinkedTreeMap)patientHashMap.get("focus");
        coor_result = new ArrayList<>();
        coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));

        focus_1.getLayoutParams().width = size;
        focus_1.getLayoutParams().height = size;
        focus_1.requestLayout();
        Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btm_manual_left);
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
        all_dots.draw(canvas);
        focus_1.setImageBitmap(btm_manual_left);

        focus_2.getLayoutParams().width = size;
        focus_2.getLayoutParams().height = size;
        focus_2.requestLayout();
        Bitmap btm_manual_left_2 = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas_2 = new Canvas(btm_manual_left_2);
        DrawDot all_dots_2 = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
        all_dots_2.draw(canvas_2);
        focus_2.setImageBitmap(btm_manual_left);

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
                if (circle_1) {
                    ++counterCorrect;
                } else {
                    ++counterFailed;
                }
                cancelTimer_1();
                move_button_1();
            }
        });
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circle_2) {
                    ++counterCorrect;
                } else {
                    ++counterFailed;
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
    }

    private void resume(){
        button_1.setClickable(true);
        button_2.setClickable(true);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.GONE);
        startTimer();
        startTimer_button2();
        if(isOn){
            focus_1.setVisibility(View.VISIBLE);
            focus_2.setVisibility(View.VISIBLE);
        }
        else{
            focus_1.setVisibility(View.INVISIBLE);
            focus_2.setVisibility(View.INVISIBLE);
        }
    }

    private void pause_menu(){
        button_1.setClickable(false);
        button_2.setClickable(false);
        timer_1.cancel();
        timer_2.cancel();
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

    private void startTimerFoco(ImageButton button_1, ImageButton button_2) {
        timer_1 = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                button_1.setVisibility(View.VISIBLE);
                button_2.setVisibility(View.VISIBLE);
                move_button_1();
                move_button_2();
            }
        };
        timer_1.start();
    }

    private void startTimer() { //counter of shape 1
        timer_1 = new CountDownTimer(time_left_1, 1000) {
            public void onTick(long millisUntilFinished) {time_left_1=millisUntilFinished; }
            public void onFinish() {
                if(circle_1) {++counterFailed;} //they didn't touch when they should have.
                else{++counterCorrect;}
                move_button_1();
            }
        };
        timer_1.start();
    }

    private void startTimer_button2() { // counter of shape 2
        timer_2 = new CountDownTimer(time_left_2, 1000) {
            public void onTick(long millisUntilFinished) { time_left_2=millisUntilFinished;}
            public void onFinish() {
                if(circle_2) {++counterFailed;} //they didn't touch when they should have.
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
            String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            saveFocusOn();
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

            ImageButton button_1 = findViewById(R.id.button_1);
            time_left_1=num_miliseconds;
            startTimer();
            if(previous_1 == 0) {
                button_1.setImageResource(R.drawable.circle_line);
                circle_1 = true;
                System.out.println("counter: " + counter + ". circulo1");
            } else if (previous_1 == 1) {
                button_1.setImageResource(R.drawable.triangle_line);
                circle_1 = false;
                System.out.println("counter: " + counter + ". triang1");
            } else if (previous_1 == 2) {
                button_1.setImageResource(R.drawable.semi_square_line);
                circle_1 = false;
                System.out.println("counter: " + counter + ". semi_square_line1");
            } else{
                button_1.setImageResource(R.drawable.star_line);
                circle_1 = false;
                System.out.println("counter: " + counter + ". estrella1");
            }
        }
    }

    private void move_button_2() {
        if (++counter == total) {
            writeResultInDataBase(counterCorrect, counterFailed);
            System.out.println("counter: " + counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
            String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            saveFocusOn();
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

            ImageButton button_2 = findViewById(R.id.button_2);
            time_left_2=num_miliseconds;
            startTimer_button2();

            if (previous_2 == 0) {
                button_2.setImageResource(R.drawable.circle_line);
                circle_2 = true;
                System.out.println("counter: " + counter + ". circulo2");
            } else if (previous_2 == 1) {
                button_2.setImageResource(R.drawable.triangle_line);
                circle_2 = false;
                System.out.println("counter: " + counter + ". triang2");
            } else if (previous_2 == 2) {
                button_2.setImageResource(R.drawable.semi_square_line);
                circle_2 = false;
                System.out.println("counter: " + counter + ". semi_square_line2");
            } else {
                button_2.setImageResource(R.drawable.star_line);
                circle_2 = false;
                System.out.println("counter: " + counter + ". estrella2");
            }
        }
    }

    public void Close(View view) {
        counter = total + 1;
        saveFocusOn();
        System.out.println("counter: " + counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
        String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
        Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
        finish();
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
