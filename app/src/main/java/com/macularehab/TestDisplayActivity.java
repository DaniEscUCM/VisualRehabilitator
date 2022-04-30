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

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.internalStorage.ReadInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_display);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        String date = getIntent().getExtras().getString("date");

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close(v));

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        String filenameCurrentPatient = "CurrentPatient.json";
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);
        LinkedTreeMap<String,  ArrayList<LinkedTreeMap>> test=(LinkedTreeMap)((LinkedTreeMap) map.get("Tests")).get(date);


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

        //Resume Grid
        ImageView grid_resume_stain=findViewById(R.id.grid_resume_stain);
        ImageView draw_dots_resume_stain=findViewById(R.id.draw_dots_resume_stain);
        ArrayList<Pair<Float, Float>> coor_result;

        if(test.containsKey("resume_stain")) {
            coor_result = prepare_pairs( test.get("resume_stain"));
        }
        else{
            coor_result = new ArrayList<>();
        }
        draw(size, coor_result, metric_unit, draw_dots_resume_stain, grid_resume_stain);


        ImageView draw_focus=findViewById(R.id.focus_display);
        LinkedTreeMap tree= (LinkedTreeMap)((LinkedTreeMap)((LinkedTreeMap) map.get("Tests")).get(date)).get("focus");
        coor_result = new ArrayList<>();
        coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));
        draw_focus.getLayoutParams().width = size;
        draw_focus.getLayoutParams().height = size;
        draw_focus.requestLayout();

        Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(btm_manual_left);
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.BLUE);
        all_dots.draw(canvas);
        draw_focus.setImageBitmap(btm_manual_left);
        draw_focus.setVisibility(View.VISIBLE);



        //Manual Grid Left
        ImageView grid_manual_left=findViewById(R.id.grid_manual_left);
        ImageView draw_dots_left=findViewById(R.id.draw_dots_manual_left);
        if(test.containsKey("manual_left")) {
              coor_result = prepare_pairs( test.get("manual_left"));
        }
        else{
            coor_result = new ArrayList<>();
        }
        draw(size, coor_result, metric_unit, draw_dots_left, grid_manual_left);

        //Manual Grid Right
        ImageView grid_manual_right=findViewById(R.id.grid_manual_right);
        ImageView draw_dots_right=findViewById(R.id.draw_dots_manual_right);
        if(test.containsKey("manual_right")) {
              coor_result =  prepare_pairs( test.get("manual_right")) ;
        }
        else{
            coor_result = new ArrayList<>();
        }
        draw(size, coor_result, metric_unit, draw_dots_right, grid_manual_right);

        //Manual Grid Both
        ImageView grid_manual_both=findViewById(R.id.grid_manual_both);
        ImageView draw_dots_both=findViewById(R.id.draw_dots_manual_both);
        if(test.containsKey("manual_both")) {
              coor_result =  prepare_pairs( test.get("manual_both")) ;
        }
        else{
            coor_result = new ArrayList<>();
        }
        draw(size, coor_result, metric_unit, draw_dots_both, grid_manual_both);

        //First Test Left
        ImageView grid_first_left=findViewById(R.id.grid_first_left);
        ImageView draw_dots_first_left=findViewById(R.id.draw_dots_first_left);
        if(test.containsKey("map_left")) {
              coor_result = prepare_pairs(  test.get("map_left") );
        }
        else{
            coor_result = new ArrayList<>();
        }
        draw(size, coor_result, metric_unit, draw_dots_first_left, grid_first_left);

        //First Test Right
        ImageView grid_first_right=findViewById(R.id.grid_first_right);
        ImageView draw_dots_first_right=findViewById(R.id.draw_dots_first_right);
        if(test.containsKey("map_right")) {
              coor_result =  prepare_pairs( test.get("map_right") );
        }
        else{
            coor_result = new ArrayList<>();
        }
        draw(size, coor_result, metric_unit, draw_dots_first_right, grid_first_right);

        //First Test Right
        ImageView grid_first_both=findViewById(R.id.grid_first_both);
        ImageView draw_dots_first_both=findViewById(R.id.draw_dots_first_both);
        if(test.containsKey("map_both")) {
              coor_result = prepare_pairs(  test.get("map_both") );
        }
        else{
            coor_result = new ArrayList<>();
        }
        draw(size, coor_result, metric_unit, draw_dots_first_both, grid_first_both);

        //Second Test Left
        ImageView grid_second_left=findViewById(R.id.grid_second_left);
        ImageView draw_dots_second_left=findViewById(R.id.draw_dots_second_left);
        if(test.containsKey("grid_left")) {
              coor_result =  prepare_pairs( test.get("grid_left") );
        }
        else{
            coor_result = new ArrayList<>();
        }
        draw(size, coor_result, metric_unit, draw_dots_second_left, grid_second_left);

        //Second Test Right
        ImageView grid_second_right=findViewById(R.id.grid_second_right);
        ImageView draw_dots_second_right=findViewById(R.id.draw_dots_second_right);
        if(test.containsKey("grid_right")) {
              coor_result = prepare_pairs(  test.get("grid_right") );
        }
        else{
            coor_result = new ArrayList<>();
        }
        draw(size, coor_result, metric_unit, draw_dots_second_right, grid_second_right);

        //Second Test Left
        ImageView grid_second_both=findViewById(R.id.grid_second_both);
        ImageView draw_dots_second_both=findViewById(R.id.draw_dots_second_both);
        if(test.containsKey("grid_both")) {
              coor_result =  prepare_pairs( test.get("grid_both") );
        }
        else{
            coor_result = new ArrayList<>();
        }
        draw(size, coor_result, metric_unit, draw_dots_second_both, grid_second_both);

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
        finish();
    }

    private ArrayList<Pair<Float,Float>> prepare_pairs(ArrayList<LinkedTreeMap> ori){
        ArrayList<Pair<Float,Float>> result= new ArrayList<>();
        for (LinkedTreeMap coor:ori) {
            result.add(new Pair<>(Float.parseFloat(coor.get("first").toString()), Float.parseFloat(coor.get("second").toString())));
        }
        return  result;
    }

    private void draw(int size,List<Pair<Float, Float>> coor_result, float metric_unit, ImageView draw_dots, ImageView grid){
        grid.getLayoutParams().width = size;
        grid.getLayoutParams().height = size;
        grid.requestLayout();

        draw_dots.getLayoutParams().width = size;
        draw_dots.getLayoutParams().height = size;
        draw_dots.requestLayout();

        Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(btm_manual_left);
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
        all_dots.draw(canvas);
        draw_dots.setImageBitmap(btm_manual_left);
        draw_dots.setVisibility(View.VISIBLE);
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

