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
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.internalStorage.ReadInternalStorage;

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
        counter = -1; counterCorrect = counterFailed = 0;
        triangle = false;
        num_miliseconds = ThirdExerciseDescriptionActivity.getNumSeconds() * 1000;
        boolean focus_on = (boolean) patientHashMap.get("focusIsOn");
        ImageButton button_dot = findViewById(R.id.dot_button);
        button_dot.setVisibility(View.INVISIBLE);
        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm
        button_dot.getLayoutParams().width = metric_unit*3;//1.5cm diametro de las figuras
        button_dot.getLayoutParams().height = metric_unit*3;

        ImageView foco = findViewById(R.id.foco);
        if(focus_on) {
            //una forma mas facil seria simplemente poniendo el focus en la posicion correcta
            /*int w = 50, h = 50;
            foco.getLayoutParams().width = w;
            foco.getLayoutParams().height = h;*/
            //ReadInternalStorage readIS = new ReadInternalStorage();
            //HashMap<String, Object> map = readIS.read(getApplicationContext(), filenameCurrentUser);

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

        ImageButton button_setting = findViewById(R.id.exercise_settings);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings(v);
                finish();
            }
        });

        ImageButton button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
                finish();
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
        System.out.println("counter: "+ counter + " counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed);
        String message_correct = "counterCorrect: " + counterCorrect + " counterFailed: " + counterFailed + " out of " + total;
        Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();
        finish();
    }

    public void Settings(View view){
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
