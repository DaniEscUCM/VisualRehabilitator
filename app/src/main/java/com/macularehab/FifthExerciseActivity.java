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


public class FifthExerciseActivity extends AppCompatActivity {

    protected final int exercise_id = 4, total = 13;
    private int counter, counterCorrect, counterFailed,num_miliseconds;
    private boolean is_letter_E;
    private CountDownTimer timer = null,timer_focus = null;
    private long time_left=3000,time_left_focus=5000;
    private String filenameCurrentUser = "CurrentPatient.json";

    private final String isFocus = "focusIsOn";
    private boolean isOn;
    private ImageView focus;
    private Button button_dot;
    private boolean hiden=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_exercise);
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
            isOn=!isOn;
        });

        counter = -1;
        counterCorrect = counterFailed = 0;
        is_letter_E = false;
        num_miliseconds = FifthExerciseDescriptionActivity.getNumSeconds() * 1000;
        time_left=num_miliseconds;
        button_dot = findViewById(R.id.button);
        //Calculate based on screen size
        Display display_measure = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display_measure.getSize(point);

        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit = (int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit * 20;//10cm
        if(size>point.y){
            size= (int) Math.round(point.y);
            metric_unit = Math.round(size/(float)20);
        }
        focus = findViewById(R.id.foco);

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
                if(is_letter_E) {++counterCorrect;}
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


    private void resume(){
        if(isOn){
            if(hiden){
                startTimerFoco(button_dot);
            }
            else{
                focus.setVisibility(View.VISIBLE);
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


    private void saveFocusOn(){

        new SaveFocusInfo(getApplicationContext(), isOn);
    }

    private void startTimerFoco(Button button_dot) {     //Timer para que aparezca el foco solo 5s
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
        timer = new CountDownTimer(time_left, 1000) {
            public void onTick(long millisUntilFinished) { time_left=millisUntilFinished;}
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

            //Toast Message
            Resources resources = this.getResources();
            String correctsString = resources.getString(R.string.exercises_results_toast_message_correctText);
            String incorrectsString = resources.getString(R.string.exercises_results_toast_message_incorrectText);
            String ofTotalString = resources.getString(R.string.exercises_results_toast_message_ofTotalText);

            String message_correct = correctsString + " " + counterCorrect + " " + incorrectsString + " " + counterFailed + " " + ofTotalString + " " + total;
            Toast.makeText(this, message_correct, Toast.LENGTH_LONG).show();

            saveFocusOn();
            finish();
        }
        else {
            Button button_dot = findViewById(R.id.button);
            System.out.println("counter: " + counter);
            is_letter_E = false;
            time_left=num_miliseconds;
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
}
