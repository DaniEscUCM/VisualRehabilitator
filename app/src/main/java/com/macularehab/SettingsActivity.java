package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.SaveFocusInfo;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private int size, metric_unit;
    private double db_size;
    private final String filenameCurrentPatient = "CurrentPatient.json";
    private final String isFocus = "focusIsOn";
    private final String focusSize = "focus_size";
    private boolean isOn, can_plus, can_minus;
    private ImageView dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_settings);

        ImageButton button = findViewById(R.id.imageButton_back_set);
        button.setOnClickListener(v -> Close(v));

        ImageButton plus = findViewById(R.id.plus_button);
        ImageButton minus = findViewById(R.id.minus_button);
        minus.setOnClickListener(v -> {
            if(can_minus) {
                db_size -= 0.25;
                size = (int) Math.round(metric_unit * db_size);
                can_minus = db_size != 1;//minimum the metric unit
                if (!can_minus) {
                    minus.setClickable(false);
                }
                plus.setClickable(true);
                can_plus = true;
                draw();
            }
        });

        plus.setOnClickListener(v -> {
            if(can_plus) {
                db_size += 0.25;
                size = (int) Math.round(metric_unit * db_size);
                can_plus = db_size != 2;//maximum the double of metric unit
                if(!can_plus){
                    plus.setClickable(false);
                }
                minus.setClickable(true);
                can_minus=true;
                draw();
            }
        });

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map= readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);
        Switch focus_switch = findViewById(R.id.focus_switch);
        focus_switch.setChecked((Boolean) map.get(isFocus));
        isOn=(Boolean) map.get(isFocus);
        focus_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isOn=!isOn;
        });
        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm

        Display display_measure = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display_measure.getSize(point);

        if(size>point.y){
            metric_unit = (int) Math.floor(point.y/(double) 20);
        }
        db_size = (double) map.get("focus_size");
        size = (int) Math.round(metric_unit * db_size);

        can_minus = db_size != 1;//minimum the metric unit
        if(!can_minus){
            minus.setClickable(false);
        }

        can_plus = db_size != 2;//maximum the double of metric unit
        if(!can_plus){
            plus.setClickable(false);
        }
        //draw dot
        dot = findViewById(R.id.dotImage);
        draw();

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

    public void Close(View view){
        new SaveFocusInfo(getApplicationContext(), isOn);
        saveFocusSize();
        finish();
    }

    public void saveFocusSize(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map= readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        map.put(focusSize, db_size);

        Gson gson = new Gson();
        String data = gson.toJson(map);


        String professional_uid = String.valueOf(map.get("professional_uid"));
        String patient_code = String.valueOf(map.get("patient_numeric_code"));

        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameCurrentPatient, data);
        databaseReference.child("Professional").child(professional_uid).
                child("Patients").child(patient_code).child(focusSize).setValue(db_size);
    }

    private void draw(){
        Bitmap btm = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btm);

        List<Pair<Float, Float>> result_coor = new ArrayList<>();
        result_coor.add(new Pair<Float, Float>((float) 0, (float) 0));
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, result_coor, size / (float) 2, metric_unit, Color.RED);
        all_dots.draw(canvas);
        dot.setImageBitmap(btm);
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