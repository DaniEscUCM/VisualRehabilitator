package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;


public class FirstExerciseActivity extends AppCompatActivity {


    private CountDownTimer timer;

    private final String filenameCurrentUser = "CurrentPatient.json";
    private long time_left = 3000;
    private ImageButton button_dot;
    private final String isFocus = "focusIsOn";
    private boolean isOn;
    private int size_focus;
    private ImageView focus;
    private int metric_unit, size;
    private ArrayList<Pair<Float, Float>> coor_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ReadInternalStorage readIS = new ReadInternalStorage();
        HashMap<String, Object> map = readIS.read(getApplicationContext(), filenameCurrentUser);

        button_dot = findViewById(R.id.dot_button);
        button_dot.setOnClickListener(v -> results());

        ImageButton button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(v -> close());

        ImageButton button_pause = findViewById(R.id.pause_button);
        button_pause.setOnClickListener(v -> pause_menu());

        ImageButton button_resume = findViewById(R.id.return_button);
        button_resume.setOnClickListener(v->resume());

        ImageButton settingsButton = findViewById(R.id.settingButton);
        settingsButton.setOnClickListener(v -> gotToSettings());

        //Calculate based on screen size
        Display display_measure = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display_measure.getSize(point);
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        size = metric_unit*20;//10cm
        if(size>point.y){
            metric_unit = (int) Math.floor(point.y/(double) 20);
            size= metric_unit*20;
        }
        focus = findViewById(R.id.focus_point);

        focus.getLayoutParams().width = size;
        focus.getLayoutParams().height = size;
        focus.requestLayout();

        LinkedTreeMap tree= (LinkedTreeMap)map.get("focus");
        coor_result = new ArrayList<>();
        coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));
        size_focus =  (int) Math.round(metric_unit * (double) map.get("focus_size"));

        drawFocusDot();

        button_dot.getLayoutParams().width = metric_unit*6;
        button_dot.getLayoutParams().height = metric_unit*6;
        focus.requestLayout();

         start();

        setUiListener();
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
    private void pause_menu(){
        timer.cancel();
        button_dot.setClickable(false);
        ImageView gone_image = findViewById(R.id.pause);
        gone_image.setVisibility(View.GONE);
        TextView txt_gone=findViewById(R.id.textoPause);
        txt_gone.setVisibility(View.GONE);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);
        ImageView inst = findViewById(R.id.volver);
        inst.setVisibility(View.VISIBLE);
        TextView txt=findViewById(R.id.textoVolver);
        txt.setVisibility(View.VISIBLE);
    }

    private void resume(){
        ReadInternalStorage readIS = new ReadInternalStorage();
        HashMap<String, Object> map = readIS.read(getApplicationContext(), filenameCurrentUser);
        isOn=(Boolean) map.get(isFocus);
        button_dot.setClickable(true);
        if(isOn){
            size_focus =  (int) Math.round(metric_unit * (double) map.get("focus_size"));
            drawFocusDot();
        }
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.GONE);
        ImageView inst = findViewById(R.id.volver);
        inst.setVisibility(View.GONE);
        TextView txt=findViewById(R.id.textoVolver);
        txt.setVisibility(View.GONE);
        start();
    }

    public void start() {
        timer = new CountDownTimer(time_left, 1000) {

            public void onTick(long millisUntilFinished) {
                time_left=millisUntilFinished;
            }

            public void onFinish() {
                button_dot.setVisibility(View.VISIBLE);
            }

        }.start();

    }

    private void results(){
        Intent i = new Intent( this, ResultsFirstExerciseActivity.class );
        startActivity(i);
        finish();
    }

    public void close(){
        Intent i = new Intent( this, FirstExerciseDescriptionActivity.class );
        startActivity(i);
        finish();
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