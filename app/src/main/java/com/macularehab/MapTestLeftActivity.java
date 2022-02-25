package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    private ImageView find_dot;
    private ImageView grid;
    private final List<Pair<Integer,Integer>> coor = new ArrayList<>();
    private final List<Pair<Integer,Integer>> coor_result = new ArrayList<>();
    private int count=0;
    private float metric_unit=0;
    private boolean blinking = false;
    private boolean find=false;
    private final float diameter_dots=14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_map_test_left);

        TextView instruction = findViewById(R.id.textInstruction);
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

        new CountDownTimer(3000, 500) {
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

        }.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(blinking){
            find=true;
        }
        return true;
    }

    private void move(){
        metric_unit = grid.getWidth()/diameter_dots;
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
        ImageView out=findViewById(R.id.multiple_dots);
        Bitmap btm= Bitmap.createBitmap(out.getWidth(),out.getWidth(), Bitmap.Config.ARGB_8888);

        float x = (out.getWidth()/(float)2) ;
        float y = (out.getWidth()/(float)2);

        Canvas canvas= new Canvas(btm);

        Dot all_dots = new Dot(x,y,coor_result, find_dot.getWidth()/(float)2, metric_unit);
        all_dots.draw(canvas);
        out.setImageBitmap(btm);
        out.setVisibility(View.VISIBLE);

        ImageButton next=findViewById((R.id.nextButtonTest1));
        next.setVisibility(View.VISIBLE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO move to next window
            }
        });

    }

}
