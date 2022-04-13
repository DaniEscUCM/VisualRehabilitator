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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.exercises.ResultInfo;
import com.macularehab.exercises.ShowResultActivity;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SecondExerciseActivity extends AppCompatActivity {

    //Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private final String filenameCurrentUser = "CurrentPatient.json";
    private HashMap<String, Object> patientHashMap;
    private final String NUM_CORRECT = "numCorrect";
    private final String NUM_FAILED = "numFailed";

    protected int counter, counterCorrect,counterFailed, num_miliseconds;
    protected final int exercise_id = 1, total = 10;
    protected boolean triangle;
    protected CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Database
        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        ReadInternalStorage readIS = new ReadInternalStorage();
        patientHashMap = readIS.read(getApplicationContext(), filenameCurrentUser);
        //End database

        counterCorrect = counterFailed = 0; counter = -1;
        triangle = false;
        timer = null;

        num_miliseconds = SecondExerciseDescriptionActivity.getNumSeconds() * 1000;
        boolean focus_on = (boolean) patientHashMap.get("focusIsOn");
        ImageButton button_dot = findViewById(R.id.dot_button);
        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm
        button_dot.getLayoutParams().width = metric_unit*6;//3cm diametro de las figuras
        button_dot.getLayoutParams().height = metric_unit*6;

        ImageView foco = findViewById(R.id.foco);
        if(focus_on){
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
                else {++counterFailed;} //they clicked when they shouldn't have
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

    private void startTimerFoco(ImageButton button_dot) {     //Timer para que aparezca el foco solo 5s
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
        //10s (10000 mili segundos) para hacer click en el circulo
        //Lo pongo a 3-6s para hacer pruebas
        timer = new CountDownTimer(num_miliseconds, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                if(triangle) {++counterFailed;} //they didn't touch when they should have.
                else{++counterCorrect;}
                move();
            }
        };
        timer.start();
    }

    private void cancelTimer() {
        timer.cancel();
        if(timer!=null) {
            timer.cancel();
        }
    }

    private void move(){
        if(++counter == total) {
            //counterCorrect = total - counterFailed + counterCorrect; //this way, if the patient hasn't touched the screen when
            //they shouldn't, we count it also as correct
            //Correct is: touching when they should and not touching when they shouldn't.
            //Incorrect is: touching when they shouldn't and not touching when they should.
            //In this exercise: correct is touching the triangles and not touching the other shapes,
            //incorrect is not touching the triangle and touching other shapes.
            writeResultInDataBase(counterCorrect, counterFailed);
            System.out.println("counter: "+ counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
            String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            ImageButton button_dot = (ImageButton) findViewById(R.id.dot_button);
            System.out.println("counter: " + counter);
            startTimer();
            if (counter == 0 || counter == 5 || counter == 7 || counter == 9) {
                button_dot.setImageResource(R.drawable.circle_black);
                triangle = false;
                System.out.println("counter: " + counter + ". circulo");
            } else if (counter == 1 || counter == 3 || counter == 6) {
                button_dot.setImageResource(R.drawable.triangle);
                triangle = true;
                System.out.println("counter: " + counter + ". triang");
            } else { //2,4,8
                button_dot.setImageResource(R.drawable.star);
                triangle = false;
                System.out.println("counter: " + counter + ". estrella");
            }
        }
    }

    public void Close(View view){
        counter = total + 1;
        finish();
    }

    public void Settings(View view) {
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

        databaseReference.child("Pruebas").child("SecondExercise").child("counterCorrect").setValue(counterCorrect);
        databaseReference.child("Pruebas").child("SecondExercise").child("counterFailed").setValue(counterFailed);
        ExerciseWriteDB exerciseWriteDB = new ExerciseWriteDB(exercise_id);
        exerciseWriteDB.writeResultInDataBase(getApplicationContext(), correct, failed, 0);

        Intent resultIntent = new Intent(this, ShowResultActivity.class);
        resultIntent.putExtra(NUM_CORRECT, correct);
        resultIntent.putExtra(NUM_FAILED, failed);
        startActivity(resultIntent);

        this.finish();
    }
}