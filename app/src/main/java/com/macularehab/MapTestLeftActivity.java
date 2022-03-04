package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.macularehab.draws.Dot;

import java.util.ArrayList;
import java.util.List;

public class MapTestLeftActivity extends AppCompatActivity{

    private ImageView centre_dot;
    private ImageView grid;
    ImageView out;
    private final List<Pair<Integer,Integer>> coor = new ArrayList<>();
    private List<Pair<Integer,Integer>> coor_result = new ArrayList<>();
    private final List<Pair<Integer,Integer>> coor_mid = new ArrayList<>();
    private int count=0;
    private int metric_unit=0;
    private boolean blinking = false;
    private boolean find=false;
    private int size =0;
    CountDownTimer counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_map_test_left);

        TextView instruction = findViewById(R.id.textInstruction);
        centre_dot= findViewById(R.id.centre_dot);
        grid = findViewById(R.id.circle_grid);
        out=findViewById(R.id.multiple_dots);

        //Set grid to correct size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        size = metric_unit*20;//10cm

        grid.getLayoutParams().width = size;
        grid.getLayoutParams().height = size;
        grid.requestLayout();


        out.getLayoutParams().width = size;
        out.getLayoutParams().height = size;
        out.requestLayout();

        init_coor(); //set coordinates

        //Make instruction disappear

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                instruction.setVisibility(View.GONE);
                blink_centre();
            }

        }.start();

    }

    private void init_coor() {
        int dot=(20/2);
        for(int i=-dot;i<=dot;i++){
            for(int j=-dot; j<=dot;j++){
                if (i*i + j*j <= dot*dot && i!=0 && j!=0) {
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
                draw_results();
                blink_find();
            }

        }.start();


    }

    private void blink_find(){
        counter = new CountDownTimer(3000, 500) {
            boolean vi = false;

            public void onTick(long millisUntilFinished) {
                if (vi){ out.setVisibility(View.VISIBLE);blinking=true;}
                else out.setVisibility(View.INVISIBLE);
                vi = !vi;
            }

            public void onFinish() {
               next_dot();
            }

        }.start();

    }

    private void next() {
        ImageButton next=findViewById((R.id.nextButtonTest1));
        next.setVisibility(View.VISIBLE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO move to next window
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(blinking){
            counter.cancel();
            find=true;
            next_dot();
        }
        return true;
    }

    public void next_dot(){
        blinking = false;
        out.setVisibility(View.INVISIBLE);
        if (!find) {
            coor_mid.add(coor.get(count));
        }
        find = false;
        if (count != coor.size() - 1) {
            count++;
            blink_centre();
        } else {
            coor_result = coor_mid;
            draw_results();
            out.setVisibility(View.VISIBLE);
            next();
        }
    }
    private void draw_results(){
        Bitmap btm= Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);

        Canvas canvas= new Canvas(btm);

        if(count!=coor.size()-1){
            coor_result.clear();
            coor_result.add(coor.get(count));
        }

        Dot all_dots = new Dot(size/(float)2,size/(float)2,coor_result, metric_unit/(float)2, metric_unit);
        all_dots.draw(canvas);
        out.setImageBitmap(btm);
    }

}
