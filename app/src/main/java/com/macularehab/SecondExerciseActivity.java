package com.macularehab;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.exercises.ShowResultActivity;
import com.macularehab.internalStorage.ReadInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;


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
    protected CountDownTimer timer = null,timer_focus = null;
    private long time_left=3000,time_left_focus=5000;

    private boolean isOn;
    private ImageView  focus;
    private ImageButton button_dot;
    private boolean hiden=false;

    private final String isFocus = "focusIsOn";
    private int size_focus;
    private int metric_unit, size;
    private ArrayList<Pair<Float, Float>> coor_result;

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


        ImageButton button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(v -> close());

        ImageButton button_pause = findViewById(R.id.pause_button);
        button_pause.setOnClickListener(v -> pause_menu());

        ImageButton button_resume = findViewById(R.id.return_button);
        button_resume.setOnClickListener(v->resume());

        ImageButton settingsButton = findViewById(R.id.settingButton);
        settingsButton.setOnClickListener(v -> gotToSettings());

        counterCorrect = counterFailed = 0; counter = -1;
        triangle = false;
        timer = null;

        num_miliseconds = SecondExerciseDescriptionActivity.getNumSeconds() * 1000;
        time_left=num_miliseconds;

        button_dot = findViewById(R.id.dot_button);
        //Calculate based on screen size
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
        button_dot.getLayoutParams().width = metric_unit*6;//3cm diametro de las figuras
        button_dot.getLayoutParams().height = metric_unit*6;

         focus = findViewById(R.id.foco);
         focus.getLayoutParams().width = size;
         focus.getLayoutParams().height = size;
         focus.requestLayout();

        HashMap<String, Object> map = readIS.read(getApplicationContext(), filenameCurrentUser);
        isOn=(Boolean) map.get(isFocus);
        LinkedTreeMap tree = (LinkedTreeMap) map.get("focus");
        coor_result = new ArrayList<>();
        coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));
        size_focus = (int) Math.round(metric_unit * (double) map.get("focus_size"));

        drawFocusDot();

        if(isOn){
            button_dot.setVisibility(View.INVISIBLE);
            startTimerFoco(button_dot); //Durante 5s solo se ve el foco
        }
        else{
             focus.setVisibility(View.INVISIBLE);
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

    private void drawFocusDot(){
        Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btm_manual_left);
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, size_focus / (float) 2, metric_unit, Color.RED);
        all_dots.draw(canvas);
         focus.setImageBitmap(btm_manual_left);
         focus.setVisibility(View.VISIBLE);
    }
    private void startTimerFoco(ImageButton button_dot) {     //Timer para que aparezca el foco solo 5s
        hiden=true;
        timer_focus = new CountDownTimer(time_left_focus, 1000) {
            public void onTick(long millisUntilFinished) { time_left_focus=millisUntilFinished; }
            public void onFinish() {
                hiden=false;
                button_dot.setVisibility(View.VISIBLE);
                move();
            }
        };
        timer_focus.start();
    }

    private void startTimer() {
        //10s (10000 mili segundos) para hacer click en el circulo
        //Lo pongo a 3-6s para hacer pruebas
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
        timer.cancel();
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

            //Toast Message
            Resources resources = this.getResources();
            String correctsString = resources.getString(R.string.exercises_results_toast_message_correctText);
            String incorrectsString = resources.getString(R.string.exercises_results_toast_message_incorrectText);
            String ofTotalString = resources.getString(R.string.exercises_results_toast_message_ofTotalText);

            String message_correct = correctsString + " " + counterCorrect + " " + incorrectsString + " " + counterFailed + " " + ofTotalString + " " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();

            finish();
        }
        else {
            ImageButton button_dot = (ImageButton) findViewById(R.id.dot_button);
            System.out.println("counter: " + counter);
            time_left=num_miliseconds;
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

    public void close(){
        counter = total + 1;
        finish();
    }


    public int getNumCorrect(){
        return counterCorrect;
    }
    public int getNumFailed(){ return counterFailed; }


    private void resume(){
        ReadInternalStorage readIS = new ReadInternalStorage();
        HashMap<String, Object> map = readIS.read(getApplicationContext(), filenameCurrentUser);
        isOn=(Boolean) map.get(isFocus);
        if(isOn){
            size_focus =  (int) Math.round(metric_unit * (double) map.get("focus_size"));
            drawFocusDot();
            if(hiden){
                startTimerFoco(button_dot);
            }
            else{
                startTimer();
            }
        }
        else{
            focus.setVisibility(View.INVISIBLE);
            if(hiden){
                hiden=false;
                button_dot.setVisibility(View.VISIBLE);
                move();
            }
            startTimer();
        }

        button_dot.setClickable(true);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.GONE);

    }

    private void pause_menu(){
        if(hiden){
            timer_focus.cancel();
        }else {
            timer.cancel();
        }
        button_dot.setClickable(false);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);
    }



    //Database
    /**
     * [EN] Writes the result of the exercise in the database and in internal storage
     * [ES] Escribe el resultado del ejercicio en la base de datos y en el almacenamiento interno
     *
     * @param correct
     *          [En] Number of corrects
     *          [ES] N??mero de aciertos
     * @param failed
     *          [En] Number of failures
     *          [ES] N??mero de fallos
     */
    private void writeResultInDataBase(int correct, int failed) {

        databaseReference.child("Pruebas").child("SecondExercise").child("counterCorrect").setValue(counterCorrect);
        databaseReference.child("Pruebas").child("SecondExercise").child("counterFailed").setValue(counterFailed);

        ExerciseWriteDB exerciseWriteDB = new ExerciseWriteDB(exercise_id);
        exerciseWriteDB.writeResultInDataBase(getApplicationContext(), correct, failed, 0);

        showResults(correct, failed);
    }

    /**
     * [EN] Starts ShowResultActivity,
     *      and passes two intents, the number of corrects and number of failures
     * [ES] Comienza nueva actividad,
     *      y pasa dos intents, el n??mero de aciertos y el n??mero de fallos
     *
     * @param correct
     *          [En] Number of corrects
     *          [ES] N??mero de aciertos
     * @param failed
     *          [En] Number of failures
     *          [ES] N??mero de fallos
     */
    private void showResults(int correct, int failed) {

        Intent resultIntent = new Intent(this, ShowResultActivity.class);
        resultIntent.putExtra(NUM_CORRECT, correct);
        resultIntent.putExtra(NUM_FAILED, failed);
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