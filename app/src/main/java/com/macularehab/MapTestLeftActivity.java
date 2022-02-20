package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MapTestLeftActivity extends AppCompatActivity{

    private CountDownTimer cTimer;
    private ImageView centre_dot;
    private ImageView find_dot;
    private ImageView grid;
    private final List<Pair<Integer,Integer>> coor = new ArrayList<>();
    private final List<Pair<Integer,Integer>> coor_result = new ArrayList<>();
    private int count=0;
    private int metric_unit=0;
    private boolean blinking = false;
    private boolean find=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_map_test);

        TextView instruction = findViewById((R.id.textInstruction));
        centre_dot= findViewById(R.id.centre_dot);
        find_dot=  findViewById(R.id.find_dot);
        grid = findViewById(R.id.circle_grid);

        init_coor();



        //Make instruction disappear

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                instruction.setVisibility(View.GONE);
            }

        }.start();

        //Make centre dot blink three times

        blink_centre();
    }

    private void init_coor() {

        for(int i=-7;i<=7;i++){
            for(int j=-7; j<=7;j++){
                if (i*i + j*j <= 7*7 && !(i==0 && j==0)) {
                    coor.add(new Pair<>(i, j));
                }
            }
        }
    }

    private void blink_centre(){

        new CountDownTimer(3000, 500) {
            boolean vi=false;

            public void onTick(long millisUntilFinished) {
                if(vi) centre_dot.setVisibility(View.VISIBLE);
                else centre_dot.setVisibility(View.INVISIBLE);
                vi=!vi;
            }

            public void onFinish() {
                centre_dot.setVisibility(View.VISIBLE);
                move();
            }

        }.start();


    }

    private void blink_find(){
        blinking=true;

        cTimer = new CountDownTimer(3000, 500) {
            boolean vi=false;

            public void onTick(long millisUntilFinished) {
                if(vi) find_dot.setVisibility(View.VISIBLE);
                else find_dot.setVisibility(View.INVISIBLE);
                vi=!vi;
            }

            public void onFinish() {
                blinking=false;
                find_dot.setVisibility(View.INVISIBLE);
                if(!find){
                    coor_result.add(coor.get(count));
                    System.out.println(coor.get(count));
                }
                find=false;
                if(count!=coor.size()-1) {
                    count++;
                    blink_centre();
                }
                else{
                    draw_results();
                }
            }

        };
        cTimer.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(blinking){
            find=true;
        }
        return true;
    }

    private void show_results(){
        ImageButton next=findViewById((R.id.nextButtonTest1));
        next.setVisibility(View.VISIBLE);
    }

    private void move(){
        metric_unit = grid.getWidth()/14;
        float x = centre_dot.getX() + (metric_unit * coor.get(count).first);
        float y = centre_dot.getY() + (metric_unit * coor.get(count).second);

        find_dot.getPivotX();
        find_dot.setX(x);
        find_dot.setY(y);

        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                blink_find();
            }

        }.start();
    }

    private void draw_results(){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);

        Canvas canvas= new Canvas();
        //canvas.drawPaint(paint);

        //for(int i=0;i<coor_result.size();i++) {
            ImageView dot = new ImageView(this);
            Bitmap bmp = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
            float x = centre_dot.getX() + (metric_unit * coor.get(0).first);
            float y = centre_dot.getY() + (metric_unit * coor.get(0).second);
            //dot.setX(x);
            //dot.setY(y);
            canvas.drawCircle(bmp.getWidth()/2, bmp.getHeight()/2, 500, paint);
            dot.setImageBitmap(bmp);
            dot.bringToFront();
       // }

        show_results();

    }

}
