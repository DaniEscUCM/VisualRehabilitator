package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;
import com.macularehab.professional.ProfessionalHome;
import com.macularehab.professional.patient.ProfessionalTestsHistoryActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CalculateFocusActivity extends AppCompatActivity {

    private final String filenameCurrentPatient = "CurrentPatient.json";

    private float centre_x;
    private float centre_y;
    private int metric_unit;
    private int size;
    private final List<Pair<Float,Float>> result_coor = new ArrayList<>();
    private Pair<Float,Float> calculated_focus;
    private ImageView focus;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_focus);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close(v));

        ImageButton repeat = findViewById(R.id.repeatButton);
        repeat.setOnClickListener(v->repeat());

        Button next = findViewById(R.id.buttonEnd);
        next.setOnClickListener(v->end());

        String cal_focus = getIntent().getExtras().getString("focus");
        List<String> list_ =Arrays.asList(cal_focus.substring(1, cal_focus.length() - 1).split(", "));
        calculated_focus = new Pair<>(Float.parseFloat(list_.get(0)),Float.parseFloat(list_.get(1)));
        result_coor.add(calculated_focus);

        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit = (int) Math.round(display.xdpi * 0.19685); //0.5cm
        size = metric_unit * 20;//10cm

        Display display_measure = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display_measure.getSize(point);
        centre_x = point.x / (float) 2;
        centre_y = point.y / (float) 2;

        if(size>point.y){
            size= (int) Math.round(point.y);
            metric_unit =  Math.round(size/(float)20);
        }

        focus = findViewById(R.id.focus);
        focus.getLayoutParams().width = size;
        focus.getLayoutParams().height = size;
        focus.requestLayout();

        ImageView grid = findViewById(R.id.grid);
        ImageView stain = findViewById(R.id.stain);
        String value = getIntent().getExtras().getString("resume_stain");
        draw_stain(grid, size, stain, value, metric_unit);

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

    void draw_stain(ImageView grid, int size, ImageView draw_dots, String value, int metric_unit){
        grid.getLayoutParams().width = size;
        grid.getLayoutParams().height = size;
        grid.requestLayout();

        draw_dots.getLayoutParams().width = size;
        draw_dots.getLayoutParams().height = size;
        draw_dots.requestLayout();

        float x=0, y=0;
        if(value!=null) {
            List<String> list = Arrays.asList(value.substring(1, value.length() - 1).split(", "));
            List<Pair<Float, Float>> coor_result = new ArrayList<>();
            char aux = ' ';
            String accumulate = "";
            float first = 0;
            float second;
            for (String word : list) {
                for (char charac : word.toCharArray()) {
                    if (charac != 'P' && charac != 'a' && charac != 'i' && charac != 'r') {
                        if (charac == '{' || charac == '}') {
                            aux = charac;
                            if (charac == '}') {
                                second = Float.parseFloat(accumulate);
                                Pair<Float,Float> pair =new Pair<>(first, second);
                                coor_result.add(pair);
                                x+=pair.first;
                                y+=pair.second;
                                accumulate = "";
                            }
                        } else if (aux == '{' && charac == ' ') {
                            first = Float.parseFloat(accumulate);
                            accumulate = "";
                        } else if (charac != ' ') {
                            accumulate += charac;
                        }
                    }

                }
            }
            //calculated_focus = new Pair<>(x/coor_result.size(),y/coor_result.size());
            //result_coor.add(calculated_focus);

            Bitmap btm = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(btm);
            DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
            all_dots.draw(canvas);
            draw_dots.setImageBitmap(btm);
            draw_dots.setVisibility(View.VISIBLE);

            draw_focus();
        }
    }

    private Pair<Float,Float> valid_coor(float x, float y){
        Float x_about_centre = -((centre_x - x) / metric_unit);
        Float y_about_centre = -((centre_y - y) / metric_unit);
        if(x_about_centre<0) x_about_centre-=1;
        if(y_about_centre<0) y_about_centre-=1;
        return new Pair<>(x_about_centre,y_about_centre);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            Pair<Float,Float> res = valid_coor(x,y);
            if(res.first*res.first + res.second*res.second <= 100){
                result_coor.clear();
                result_coor.add(res);
                draw_focus();
            }
            return true;
        }
        return false;
    }

    private void repeat(){
        result_coor.clear();
        result_coor.add(calculated_focus);
        draw_focus();
    }

    private void draw_focus(){
        Bitmap btm = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btm);
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, result_coor, metric_unit / (float) 2, metric_unit, Color.BLUE);
        all_dots.draw(canvas);
        focus.setImageBitmap(btm);
        focus.setVisibility(View.VISIBLE);
    }

    public void end(){
        String patient_id = getIntent().getExtras().getString("patient_id");
        databaseReference.child("Professional").child(firebaseAuth.getUid()).child("Patients").child(patient_id).
                child("focus").setValue(result_coor.get(0));
        String date = getIntent().getExtras().getString("date");

        databaseReference.child("Professional").child(firebaseAuth.getUid()).child("Patients").child(patient_id).
                child("Tests").child(date).child("focus").setValue(result_coor.get(0));
        File file = new File(getApplicationContext().getFilesDir(), filenameCurrentPatient);
        if(file.exists()){
            ReadInternalStorage readInternalStorage = new ReadInternalStorage();
            HashMap<String, Object> map= readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

            if (map != null) {

                if (map.get("patient_numeric_code").equals(patient_id)) {
                    map.put("focus", result_coor.get(0));
                    ((LinkedTreeMap) ((LinkedTreeMap) map.get("Tests")).get(date)).put("focus", result_coor.get(0));

                    Gson gson = new Gson();
                    String data = gson.toJson(map);
                    WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
                    writeInternalStorage.write(getApplicationContext(), filenameCurrentPatient, data);

                    Intent i = new Intent(this, ProfessionalTestsHistoryActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(this, ProfessionalHome.class);
                    startActivity(i);
                }
            }
            else{
                Intent i = new Intent( this, ProfessionalHome.class );
                startActivity(i);
            }
        }
        else{
            Intent i = new Intent( this, ProfessionalHome.class );
            startActivity(i);
        }
    }

    public void Close(View view){
        finish();
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