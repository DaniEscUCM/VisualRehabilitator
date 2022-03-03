package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
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
    //private ImageView find_dot;
    private ImageView grid;
    ImageView out;
    private final List<Pair<Integer,Integer>> coor = new ArrayList<>();
    private List<Pair<Integer,Integer>> coor_result = new ArrayList<>();
    private final List<Pair<Integer,Integer>> coor_mid = new ArrayList<>();
    private int count=0;
    private float metric_unit=0;
    private boolean blinking = false;
    private boolean find=false;
    private float centre_x = 0;
    private float centre_y = 0;
    private float radius=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_map_test_left);

        TextView instruction = findViewById(R.id.textInstruction);
        centre_dot= findViewById(R.id.centre_dot);
        //find_dot=  findViewById(R.id.find_dot);
        grid = findViewById(R.id.circle_grid);

        out=findViewById(R.id.multiple_dots);

        //Set grid to correct size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        metric_unit=(float) (display.xdpi * 0.19685); //0.5cm
        double width = metric_unit*20;//10cm


        grid.getLayoutParams().width = (int) Math.ceil(width);
        grid.getLayoutParams().height = (int) Math.ceil(width);
        grid.requestLayout();

        radius=centre_dot.getWidth()/(float)2;

        out.getLayoutParams().width = (int) Math.ceil(width)  ;
        out.getLayoutParams().height = (int) Math.ceil(width);

        centre_x = (float)(width)/(float)2;
        centre_y = (float)(width)/(float)2;

        out.requestLayout();

        init_coor(); //set coordinates

        //Make instruction disappear

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                instruction.setVisibility(View.GONE);

                //blink_centre();
                draw_results();
            }

        }.start();

    }

    private void init_coor() {
        int dot=(20/2);
        for(int i=-dot;i<=dot;i++){
            for(int j=-dot; j<=dot;j++){
                if (i*i + j*j <= dot*dot && !(i==0 && j==0)) {
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
                //move();
                draw_results();
                blink_find();
            }

        }.start();


    }

    private void blink_find(){
        blinking=true;

        new CountDownTimer(3000, 500) {
            boolean vi=false;

            public void onTick(long millisUntilFinished) {
                if(vi) out.setVisibility(View.VISIBLE);
                else out.setVisibility(View.INVISIBLE);
                vi=!vi;
            }

            public void onFinish() {
                blinking=false;
                out.setVisibility(View.INVISIBLE);
                if(!find){
                    coor_mid.add(coor.get(count));
                    //System.out.println(coor.get(count));
                }
                find=false;
                if(count!=coor.size()-1) {
                    count++;
                    blink_centre();
                }
                else{
                    coor_result=coor_mid;
                    draw_results();
                    next();
                }
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
            find=true;
        }
        return true;
    }

   // private void move(){
        //metric_unit = grid.getWidth()/(float) diameter_dots;
        /*float x = centre_dot.getX() + (metric_unit * coor.get(count).first);
        float y = centre_dot.getY() + (metric_unit * coor.get(count).second);

        find_dot.getPivotX();
        find_dot.setX(x);
        find_dot.setY(y);*/

       /* coor_result.clear();
        coor_result.add(coor.get(count));
        draw_results();

        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                blink_find();
            }

        }.start();
    }*/

    private void draw_results(){

        Bitmap btm= Bitmap.createBitmap(out.getWidth(),out.getWidth(), Bitmap.Config.ARGB_8888);


        //tem.out.println(radius);
        System.out.println(out.getX());
        System.out.println(out.getY());
        System.out.println(out.getWidth());
        System.out.println(out.getHeight());
        System.out.println("-----------------");
        System.out.println(out.getWidth()/(float)2);
        System.out.println(out.getWidth()/(float)2);

        Canvas canvas= new Canvas(btm);

        if(count!=coor.size()-1){
            coor_result.clear();
            coor_result.add(coor.get(count));
        }
        //coor_result.add(coor.get(20));
        Dot all_dots = new Dot(out.getWidth()/(float)2,out.getWidth()/(float)2,coor, radius, metric_unit);
        all_dots.draw(canvas);
        out.setImageBitmap(btm);
        out.setVisibility(View.VISIBLE);



    }

}
