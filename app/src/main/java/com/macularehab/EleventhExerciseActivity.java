package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
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
import com.macularehab.exercises.SaveFocusInfo;
import com.macularehab.exercises.ShowResultActivity;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class EleventhExerciseActivity extends AppCompatActivity {

    private final int exercise_id = 10, total = 10, num_shapes = 4;
    private int counter, counterCorrect, counterFailed, num_miliseconds, current, metric_unit, size;
    private CountDownTimer timer=null,timer_focus = null;
    private long time_left=3000,time_left_focus=5000;
    private HashMap<String, Object> patientHashMap;
    private final String filenameCurrentUser = "CurrentPatient.json";

    private final String isFocus = "focusIsOn";
    private boolean isOn;
    private ImageView focus;
    private ImageButton button_button_left_eye;
    private ImageButton button_right_eye;
    private ImageButton button_mouth;
    private ImageButton button_nose;
    private boolean hiden=false;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleventh_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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
        num_miliseconds = EleventhExerciseDescriptionActivity.getNumSeconds() * 1000;
        time_left=num_miliseconds;

        button_button_left_eye = findViewById(R.id.dot_button_left_eye);
        button_right_eye = findViewById(R.id.dot_button_right_eye);
        button_mouth = findViewById(R.id.dot_button_mouth);
        button_nose = findViewById(R.id.dot_button_nose);
        ImageView photo = findViewById(R.id.image_background);

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

    private void startTimerFoco() {
        hiden=true;
        timer_focus = new CountDownTimer(time_left_focus, 1000) {
            public void onTick(long millisUntilFinished) { time_left_focus=millisUntilFinished;}
            public void onFinish() {
                hiden=false;
                cancelTimer_1();
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
            //focus = findViewById(R.id.focus_right_eye);
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
            //focus = findViewById(R.id.focus_nose);
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
            //focus = findViewById(R.id.focus_mouth);
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
                timer.cancel();
            }
        };
        timer.start();
    }

    private void cancelTimer_1() {
        if (timer != null) {
            timer.cancel();
        }
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

        new SaveFocusInfo(getApplicationContext(), isOn);
    }

    private void move() {
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
            Resources res = EleventhExerciseActivity.this.getResources();
            stopPlayer();
            if(current == 0) {
                text.setText(res.getString(R.string.eleventh_exercise_find_left_eye));
                playAudio("");
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

    private void playAudio(String filename) {

        if (mediaPlayer != null) {
            stopAndPlay(filename);
        }
        else {

            mediaPlayer = MediaPlayer.create(this, R.raw.encuentra_ojo_izquierdo);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });

            mediaPlayer.start();
        }
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void stopAndPlay(String filename) {
        stopPlayer();
        playAudio(filename);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}
