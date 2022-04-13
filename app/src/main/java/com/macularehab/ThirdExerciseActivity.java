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
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class ThirdExerciseActivity extends AppCompatActivity {

    private final int exercise_id = 2, total = 12;
    private int counter, counterCorrect, counterFailed, num_miliseconds;
    private boolean triangle;
    private CountDownTimer timer = null;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private final String filenameCurrentUser = "CurrentPatient.json";
    private HashMap<String, Object> patientHashMap;
    private long time_left=3000;

    private final String isFocus = "focusIsOn";
    private boolean isOn;
    private ImageView foco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

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
            ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
            HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentUser);
            isOn=!(Boolean)mapS.get(isFocus);
        });

        counter = -1; counterCorrect = counterFailed = 0;
        triangle = false;
        num_miliseconds = ThirdExerciseDescriptionActivity.getNumSeconds() * 1000;
        time_left=num_miliseconds;
        boolean focus_on = (boolean) patientHashMap.get("focusIsOn");
        ImageButton button_dot = findViewById(R.id.dot_button);
        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm
        button_dot.getLayoutParams().width = metric_unit*3;//1.5cm diametro de las figuras
        button_dot.getLayoutParams().height = metric_unit*3;

        foco = findViewById(R.id.foco);
        if(focus_on) {
            button_dot.setVisibility(View.INVISIBLE);
            ArrayList<Pair<Float, Float>> coor_result;
            LinkedTreeMap tree= (LinkedTreeMap)patientHashMap.get("focus");
            coor_result = new ArrayList<>();
            coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));

            foco.getLayoutParams().width = size;
            foco.getLayoutParams().height = size;
            foco.requestLayout();
            Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(btm_manual_left);
            DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
            all_dots.draw(canvas);
            foco.setImageBitmap(btm_manual_left);

            startTimerFoco(button_dot); //Durante 5s solo se ve el foco
        }
        else{
            foco.setVisibility(View.INVISIBLE);
            move();
        }


        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(triangle) {++counterCorrect;}
                else {++counterFailed;}
                cancelTimer();
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
    private void startTimerFoco(ImageButton button_dot) {  //Timer para que aparezca el foco solo 5s
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
        //10s (10000 mili segundos) para hacer click en la figura
        timer = new CountDownTimer(time_left, 1000) {
            public void onTick(long millisUntilFinished) {time_left=millisUntilFinished; }
            public void onFinish() {
                if(triangle) {++counterFailed;} //they didn't touch when they should have.
                else{++counterCorrect;}
                move();
            }
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
            ImageButton button_dot = findViewById(R.id.dot_button);
            System.out.println("counter: " + counter);
            time_left=num_miliseconds;
            startTimer();
            triangle = false;
            if (counter == 0 || counter == 6 || counter == 10) {
                button_dot.setImageResource(R.drawable.circle_line);
                System.out.println("counter: " + counter + ". circulo");
            } else if (counter == 2 || counter == 5 || counter == 9) {
                button_dot.setImageResource(R.drawable.triangle_line);
                triangle = true;
                System.out.println("counter: " + counter + ". triang");
            } else if (counter == 4 || counter == 7 || counter == 11) {
                button_dot.setImageResource(R.drawable.star_line);
                System.out.println("counter: " + counter + ". estrella");
            } else { //1,3,8
                button_dot.setImageResource(R.drawable.semi_square_line);
                System.out.println("counter: " + counter + ". squareL");
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
    private void resume(){
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.GONE);
        startTimer();
        if(isOn){
            foco.setVisibility(View.VISIBLE);
        }
        else{
            foco.setVisibility(View.INVISIBLE);
        }
        saveFocusOn();
    }

    private void pause_menu(){
        timer.cancel();
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
