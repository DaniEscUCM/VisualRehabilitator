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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class TestsResultsActivity extends AppCompatActivity {

    private final String filenameCurrentPatient = "CurrentPatient.json";
    private HashMap<String, Object> map;

    private HashSet<Pair<Float,Float>> dots = new LinkedHashSet<>();
    private ArrayList<Pair<Float,Float>> coor_resul = new ArrayList<>();
    private String patient_num_cod="";
    private String date="";
    private Float x= (float) 0,y= (float) 0;
    private int cont = 0;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_results);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        patient_num_cod = getIntent().getExtras().getString("patient_id");
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy HH:mm:ss");
        Date date_aux = new Date();
        date=formatter.format(date_aux);


        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close(v));

        Button next=findViewById(R.id.next_view);
        next.setOnClickListener(v -> next(v));

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference.child("Professional").child(firebaseAuth.getUid()).child("Patients").child(patient_num_cod).child("date_last_test").setValue(date);

        File file  = new File(getApplicationContext().getFilesDir(), filenameCurrentPatient);
        if(file.exists()) {
            ReadInternalStorage readInternalStorage = new ReadInternalStorage();
            map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

            if (map != null) {

                if (map.get("patient_numeric_code").equals(patient_num_cod) && map.containsKey("Tests")) {
                    ((LinkedTreeMap) map.get("Tests")).put(date, new LinkedTreeMap<>());
                    map.put("date_last_test", date);
                } else {
                    map = null;
                }
            }
        }

        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm

        Display display_measure = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display_measure.getSize(point);

        if(size>point.y){
            metric_unit = (int) Math.floor(point.y/(double) 20);
            size= metric_unit*20;
        }

        //Manual Grid Left
        ImageView grid_manual_left=findViewById(R.id.grid_manual_left);
        ImageView draw_dots_left=findViewById(R.id.draw_dots_manual_left);
        String value = getIntent().getExtras().getString("manual_left");
        draw(grid_manual_left,size,draw_dots_left,value,metric_unit, "manual_left");

        //Manual Grid Right
        ImageView grid_manual_right=findViewById(R.id.grid_manual_right);
        ImageView draw_dots_right=findViewById(R.id.draw_dots_manual_right);
        String value1 = getIntent().getExtras().getString("manual_right");
        draw(grid_manual_right,size,draw_dots_right,value1,metric_unit, "manual_right");

        //Manual Grid Both
        ImageView grid_manual_both=findViewById(R.id.grid_manual_both);
        ImageView draw_dots_both=findViewById(R.id.draw_dots_manual_both);
        String value2 = getIntent().getExtras().getString("manual_both");
        draw(grid_manual_both,size,draw_dots_both,value2,metric_unit, "manual_both");

        //First Test Left
        ImageView grid_first_left=findViewById(R.id.grid_first_left);
        ImageView draw_dots_first_left=findViewById(R.id.draw_dots_first_left);
        String value3 = getIntent().getExtras().getString("map_left");
        draw(grid_first_left,size,draw_dots_first_left,value3,metric_unit, "map_left");

        //First Test Right
        ImageView grid_first_right=findViewById(R.id.grid_first_right);
        ImageView draw_dots_first_right=findViewById(R.id.draw_dots_first_right);
        String value4 = getIntent().getExtras().getString("map_right");
        draw(grid_first_right,size,draw_dots_first_right,value4,metric_unit, "map_right");

        //First Test Right
        ImageView grid_first_both=findViewById(R.id.grid_first_both);
        ImageView draw_dots_first_both=findViewById(R.id.draw_dots_first_both);
        String value5 = getIntent().getExtras().getString("map_both");
        draw(grid_first_both,size,draw_dots_first_both,value5,metric_unit, "map_both");

        //Second Test Left
        ImageView grid_second_left=findViewById(R.id.grid_second_left);
        ImageView draw_dots_second_left=findViewById(R.id.draw_dots_second_left);
        String value6 = getIntent().getExtras().getString("grid_left");
        draw(grid_second_left,size,draw_dots_second_left,value6,metric_unit, "grid_left");

        //Second Test Right
        ImageView grid_second_right=findViewById(R.id.grid_second_right);
        ImageView draw_dots_second_right=findViewById(R.id.draw_dots_second_right);
        String value7 = getIntent().getExtras().getString("grid_right");
        draw(grid_second_right,size,draw_dots_second_right,value7,metric_unit, "grid_right");

        //Second Test Left
        ImageView grid_second_both=findViewById(R.id.grid_second_both);
        ImageView draw_dots_second_both=findViewById(R.id.draw_dots_second_both);
        String value8 = getIntent().getExtras().getString("grid_both");
        draw(grid_second_both,size,draw_dots_second_both,value8,metric_unit, "grid_both");

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

    void draw(ImageView grid, int size, ImageView draw_dots, String value, int metric_unit, String name_grid){
        grid.getLayoutParams().width = size;
        grid.getLayoutParams().height = size;
        grid.requestLayout();

        draw_dots.getLayoutParams().width = size;
        draw_dots.getLayoutParams().height = size;
        draw_dots.requestLayout();

        List<Pair<Float, Float>> coor_result = new ArrayList<>();

        Gson gson = new Gson();
        List<LinkedTreeMap> aux = gson.fromJson(value,coor_result.getClass());
        Pair<Double,Double> pair = new Pair<>((double) 0, (double) 0);
        if(aux!=null) {
            for (LinkedTreeMap coor:aux) {
                pair = gson.fromJson(gson.toJson(coor), pair.getClass());
                Pair<Float,Float> pair2 = new Pair<>(pair.first.floatValue(),pair.second.floatValue());
                coor_result.add(pair2);
                x+= pair2.first;
                y+= pair2.second;
                cont++;
                if(!dots.contains(pair2)){
                    dots.add(pair2);
                    coor_resul.add(pair2);
                }
            }

            Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(btm_manual_left);
            DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
            all_dots.draw(canvas);
            draw_dots.setImageBitmap(btm_manual_left);
            draw_dots.setVisibility(View.VISIBLE);


        }
        databaseReference.child("Professional").child(firebaseAuth.getUid()).child("Patients").child(patient_num_cod).
                child("Tests").child(date).child(name_grid).setValue(coor_result);

        if(map != null) {
            ((LinkedTreeMap)((LinkedTreeMap)map.get("Tests")).get(date)).put(name_grid,coor_result);
        }
    }
    private void next(View v) {
        Intent i = new Intent( this, CalculateFocusActivity.class );
        databaseReference.child("Professional").child(firebaseAuth.getUid()).child("Patients").child(patient_num_cod).
                child("Tests").child(date).child("resume_stain").setValue(coor_resul);
        Gson gson = new Gson();
        String value= gson.toJson(coor_resul);
        x = x/cont;
        y = y/cont;

        String focus= "["+ x +", "+ y +"]";
        i.putExtra("focus",focus);
        i.putExtra("resume_stain",value);
        i.putExtra("date",date);
        i.putExtra("patient_id",patient_num_cod);

        if(map!=null) {
            ((LinkedTreeMap)((LinkedTreeMap)map.get("Tests")).get(date)).put("resume_stain",coor_resul);


            String data = gson.toJson(map);
            WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
            writeInternalStorage.write(getApplicationContext(),filenameCurrentPatient,data);
        }
        startActivity(i);
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