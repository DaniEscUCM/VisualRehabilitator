package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import java.util.List;

public class ManualInputStainLeftActivity extends AppCompatActivity {

    private final List<Pair<Integer,Integer>> coor = new ArrayList<>();
    private final List<Pair<Integer,Integer>> result_coor = new ArrayList<>();
    private int metric_unit;
    private float centre_x=0;
    private float centre_y=0;
    private ImageView draw_space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_manual_input_stain_left);

        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm

        Display display_measure = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display_measure.getSize(point);
        centre_x=point.x/(float)2;
        centre_y=point.y/(float)2;

        ImageView grid=findViewById(R.id.circleGrid);
        grid.getLayoutParams().width = size;
        grid.getLayoutParams().height = size;
        grid.requestLayout();

        draw_space=findViewById(R.id.dotsPainting);
        draw_space.getLayoutParams().width = size;
        draw_space.getLayoutParams().height = size;
        draw_space.requestLayout();

        init_coor();


        ImageButton next=findViewById(R.id.nextButton);

        next.setOnClickListener(v -> {
            next_test(v);
        });

        ImageButton repeat=findViewById(R.id.repeatButton);
        repeat.setOnClickListener(v -> {
            result_coor.clear();
            draw();
        });

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_map_test);
        button.setOnClickListener(v -> Close(v));
    }

    private void init_coor() {
        int dot=(20/2);
        for(int i=-dot;i<=dot;i++){
            for(int j=-dot; j<=dot;j++){
                if (i*i + j*j <= dot*dot && i!=0 && j!=0) {
                    coor.add(new Pair<>(i,j));
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            Pair<Integer, Integer> res = valid_coor(x, y);
            //System.out.println(x + " " + y);
            if (!res.equals(new Pair<>(0, 0))) {
                if (result_coor.contains(res)) {
                    result_coor.remove(res);
                } else {
                    result_coor.add(res);
                }
                draw();
                return true;
            }
        }
        return false;
    }

    private void draw(){
        Bitmap btm = Bitmap.createBitmap(draw_space.getWidth(), draw_space.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btm);
        DrawDot draw = new DrawDot(draw_space.getWidth() / (float) 2, draw_space.getWidth() / (float) 2, result_coor, metric_unit / (float) 2, metric_unit);
        draw.draw(canvas);
        draw_space.setImageBitmap(btm);
        draw_space.setVisibility(View.VISIBLE);
    }

    private Pair<Integer,Integer> valid_coor(float x, float y){
        float ref_coor_x_minor,ref_coor_x_mayor;
        float ref_coor_y_minor,ref_coor_y_mayor;
        int i=0;
        do{
            ref_coor_x_minor = centre_x + (metric_unit * coor.get(i).first) +
                    (coor.get(i).first >= 0 ? -(metric_unit /(float) 2) : (metric_unit/ (float)2)) -
                    (metric_unit/ (float)2);
            ref_coor_x_mayor = centre_x + (metric_unit * coor.get(i).first) +
                    (coor.get(i).first >= 0 ? -(metric_unit /(float) 2) : (metric_unit/ (float)2)) +
                    (metric_unit/ (float)2);

            ref_coor_y_minor = centre_y + (metric_unit * coor.get(i).second) +
                    (coor.get(i).second >= 0 ? -(metric_unit /(float) 2) : (metric_unit/(float) 2)) -
                    (metric_unit/ (float)2);

            ref_coor_y_mayor = centre_y + (metric_unit * coor.get(i).second) +
                    (coor.get(i).second >= 0 ? -(metric_unit /(float) 2) : (metric_unit/(float) 2)) +
                    (metric_unit/ (float)2);
            i++;
        }
        while (!(ref_coor_x_minor<=x && x<= ref_coor_x_mayor && ref_coor_y_minor<= y&& y<=ref_coor_y_mayor) && i!=coor.size());

        if(ref_coor_x_minor<=x && x<= ref_coor_x_mayor && ref_coor_y_minor<= y&& y<=ref_coor_y_mayor){
            return coor.get(i-1);
        }
        return new Pair<>(0,0);
    }

    private void next_test(View v) {
        Intent i = new Intent( this, MapTestLeftExplanationActivity.class );
        startActivity(i);
    }


    public void Close(View view){
        finish();
    }
}