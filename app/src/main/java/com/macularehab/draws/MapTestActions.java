package com.macularehab.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class MapTestActions {

    ImageView out;
    private final List<Pair<Integer,Integer>> coor = new ArrayList<>();
    private List<Pair<Integer,Integer>> coor_result = new ArrayList<>();
    private final List<Pair<Integer,Integer>> coor_mid = new ArrayList<>();
    private int count=0;
    private final int metric_unit;
    private boolean find=false;
    private final int size;
    private boolean end=false;
    Blinking centre_dot_blink;
    Blinking find_dot_blink;

    public MapTestActions(TextView _instruction, ImageView _centre_dot, ImageView _grid, ImageView _out, int _size, int _metric_unit){
        this.out=_out;
        this.size=_size;
        this.metric_unit=_metric_unit;

        _grid.getLayoutParams().width = this.size;
        _grid.getLayoutParams().height = this.size;
        _grid.requestLayout();

        out.getLayoutParams().width = this.size;
        out.getLayoutParams().height = this.size;
        out.requestLayout();

        centre_dot_blink=new Blinking(_centre_dot,true) {
            @Override
            protected void end_blinking() {
                super.obj.setVisibility(View.VISIBLE);
                draw_results();
                find_dot_blink.do_blink();
            }
        };

        find_dot_blink=new Blinking(out,false) {
            @Override
            protected void end_blinking() {
                next_dot();
            }
        };

        init_coor(); //set coordinates

        //Make instruction disappear

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                _instruction.setVisibility(View.GONE);
                centre_dot_blink.do_blink();
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

    private void draw_results(){
        Bitmap btm= Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);

        Canvas canvas= new Canvas(btm);

        if(!end){
            coor_result.clear();
            coor_result.add(coor.get(count));
        }

        DrawDot all_dots = new DrawDot(size/(float)2,size/(float)2,coor_result, metric_unit/(float)2, metric_unit);
        all_dots.draw(canvas);
        out.setImageBitmap(btm);
    }

    public void touch_happened(){
        if(find_dot_blink.is_blinking()){
            find=true;
            find_dot_blink.cancel_blink();
        }
    }

    public void next_dot(){
        if (!find) {
            coor_mid.add(coor.get(count));
        }
        find = false;
        if (count != coor.size() - 1) {
            count++;
            centre_dot_blink.do_blink();
        } else {
            coor_result = new ArrayList<>(coor_mid);
            end=true;
            draw_results();
            out.setVisibility(View.VISIBLE);
            next();
        }
    }

    public abstract void next();

    public void repeat(){
        out.setVisibility(View.INVISIBLE);
        coor_mid.clear();
        count=0;
        end=false;
        centre_dot_blink.do_blink();
    }

}
