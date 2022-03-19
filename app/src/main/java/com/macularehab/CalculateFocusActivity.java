package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.macularehab.draws.DrawDot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculateFocusActivity extends AppCompatActivity {

    private float centre_x;
    private float centre_y;
    private int metric_unit;
    private int size;
    //private final List<Pair<Float,Float>> coor = new ArrayList<>();
    private final List<Pair<Float,Float>> result_coor = new ArrayList<>();
    private Pair<Float,Float> calculated_focus;
    private ImageView focus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_focus);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

       // init_coor();

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close(v));

        ImageButton repeat = findViewById(R.id.repeatButton);
        repeat.setOnClickListener(v->repeat());

        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit = (int) Math.round(display.xdpi * 0.19685); //0.5cm
        size = metric_unit * 20;//10cm

        Display display_measure = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display_measure.getSize(point);
        centre_x = point.x / (float) 2;
        centre_y = point.y / (float) 2;

        focus = findViewById(R.id.focus);
        focus.getLayoutParams().width = size;
        focus.getLayoutParams().height = size;
        focus.requestLayout();

        ImageView grid = findViewById(R.id.grid);
        ImageView stain = findViewById(R.id.stain);
        String value = getIntent().getExtras().getString("resume_stain");
        draw_stain(grid, size, stain, value, metric_unit);


    }

    /*private void init_coor() {
        int dot=(20/2);
        for(int i=-dot;i<=dot;i++){
            for(int j=-dot; j<=dot;j++){
                if (i*i + j*j <= dot*dot && i!=0 && j!=0) {
                    coor.add(new Pair<>((float)i,(float)j));
                }
            }
        }
    }*/

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
            calculated_focus = new Pair<>(x/coor_result.size(),y/coor_result.size());
            result_coor.add(calculated_focus);

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
        Float x_about_centre = -(centre_x - x) / metric_unit;
        Float y_about_centre = -(centre_y - y) / metric_unit ;
        return new Pair<>(x_about_centre,y_about_centre);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            Pair<Float,Float> res = valid_coor(x,y);
            result_coor.clear();
            result_coor.add(res);
            draw_focus();
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

    public void Close(View view){
        finish();
    }
}