package com.macularehab.actions;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.macularehab.draws.DrawDot;

import java.util.ArrayList;
import java.util.List;

public class ManualActions {

    private final List<Pair<Integer,Integer>> coor = new ArrayList<>();
    private final List<Pair<Integer,Integer>> result_coor = new ArrayList<>();
    private int metric_unit;
    private float centre_x=0;
    private float centre_y=0;
    private ImageView draw_space;

    public ManualActions(int _metric, float _centre_x, float _centre_y, ImageView _draw_space ){
        this.metric_unit=_metric;
        this.centre_x=_centre_x;
        this.centre_y=_centre_y;
        this.draw_space=_draw_space;


        init_coor();
    }

    public void repeat(){
        result_coor.clear();
        draw();
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

    public void touch_happened(float x, float y){
        Pair<Integer, Integer> res = valid_coor(x, y);
        if (!res.equals(new Pair<>(0, 0))) {
            if (result_coor.contains(res)) {
                result_coor.remove(res);
            } else {
                result_coor.add(res);
            }
            draw();
        }
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
}
