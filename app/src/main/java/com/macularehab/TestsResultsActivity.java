package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.macularehab.draws.DrawDot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestsResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_results);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close(v));


        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm

        //Manual Grid Left
        ImageView grid_manual_left=findViewById(R.id.grid_manual_left);
        ImageView draw_dots_left=findViewById(R.id.draw_dots_manual_left);
        String value = getIntent().getExtras().getString("manual_left");
        draw(grid_manual_left,size,draw_dots_left,value,metric_unit);

        //Manual Grid Right
        ImageView grid_manual_right=findViewById(R.id.grid_manual_right);
        ImageView draw_dots_right=findViewById(R.id.draw_dots_manual_right);
        String value1 = getIntent().getExtras().getString("manual_right");
        draw(grid_manual_right,size,draw_dots_right,value1,metric_unit);

        //Manual Grid Both
        ImageView grid_manual_both=findViewById(R.id.grid_manual_both);
        ImageView draw_dots_both=findViewById(R.id.draw_dots_manual_both);
        String value2 = getIntent().getExtras().getString("manual_both");
        draw(grid_manual_both,size,draw_dots_both,value2,metric_unit);

        //First Test Left
        ImageView grid_first_left=findViewById(R.id.grid_first_left);
        ImageView draw_dots_first_left=findViewById(R.id.draw_dots_first_left);
        String value3 = getIntent().getExtras().getString("map_left");
        draw(grid_first_left,size,draw_dots_first_left,value3,metric_unit);

        //First Test Right
        ImageView grid_first_right=findViewById(R.id.grid_first_right);
        ImageView draw_dots_first_right=findViewById(R.id.draw_dots_first_right);
        String value4 = getIntent().getExtras().getString("map_right");
        draw(grid_first_right,size,draw_dots_first_right,value4,metric_unit);

        //First Test Right
        ImageView grid_first_both=findViewById(R.id.grid_first_both);
        ImageView draw_dots_first_both=findViewById(R.id.draw_dots_first_both);
        String value5 = getIntent().getExtras().getString("map_both");
        draw(grid_first_both,size,draw_dots_first_both,value5,metric_unit);

        //Second Test Left
        ImageView grid_second_left=findViewById(R.id.grid_second_left);
        ImageView draw_dots_second_left=findViewById(R.id.draw_dots_second_left);
        String value6 = getIntent().getExtras().getString("grid_left");
        draw(grid_second_left,size,draw_dots_second_left,value6,metric_unit);

        //Second Test Right
        ImageView grid_second_right=findViewById(R.id.grid_second_right);
        ImageView draw_dots_second_right=findViewById(R.id.draw_dots_second_right);
        String value7 = getIntent().getExtras().getString("grid_right");
        draw(grid_second_right,size,draw_dots_second_right,value7,metric_unit);

        //Second Test Left
        ImageView grid_second_both=findViewById(R.id.grid_second_both);
        ImageView draw_dots_second_both=findViewById(R.id.draw_dots_second_both);
        String value8 = getIntent().getExtras().getString("grid_both");
        draw(grid_second_both,size,draw_dots_second_both,value8,metric_unit);
    }

    void draw(ImageView grid, int size, ImageView draw_dots, String value, int metric_unit){
        grid.getLayoutParams().width = size;
        grid.getLayoutParams().height = size;
        grid.requestLayout();

        draw_dots.getLayoutParams().width = size;
        draw_dots.getLayoutParams().height = size;
        draw_dots.requestLayout();
        if(value!=null) {
            List<String> list = Arrays.asList(value.substring(1, value.length() - 1).split(", "));
            List<Pair<Integer, Integer>> coor_result = new ArrayList<>();
            char aux = ' ';
            String accumulate = "";
            int first = 0;
            int second;
            for (String s : list) {
                for (char x : s.toCharArray()) {
                    if (x != 'P' && x != 'a' && x != 'i' && x != 'r') {
                        if (x == '{' || x == '}') {
                            aux = x;
                            if (x == '}') {
                                second = Integer.parseInt(accumulate);
                                coor_result.add(new Pair<>(first, second));
                                accumulate = "";
                            }
                        } else if (aux == '{' && x == ' ') {
                            first = Integer.parseInt(accumulate);
                            accumulate = "";
                        } else if (x != ' ') {
                            accumulate += x;
                        }
                    }

                }
            }

            Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(btm_manual_left);
            DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit);
            all_dots.draw(canvas);
            draw_dots.setImageBitmap(btm_manual_left);
            draw_dots.setVisibility(View.VISIBLE);
        }
    }


    public void Close(View view){
        finish();
    }
}