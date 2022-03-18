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
        grid_manual_left.getLayoutParams().width = size;
        grid_manual_left.getLayoutParams().height = size;
        grid_manual_left.requestLayout();

        ImageView draw_dots_left=findViewById(R.id.draw_dots_manual_left);
        draw_dots_left.getLayoutParams().width = size;
        draw_dots_left.getLayoutParams().height = size;
        draw_dots_left.requestLayout();

        String value = getIntent().getExtras().getString("manual_left");

        List<Pair<Integer,Integer>> coor_result = new ArrayList<>();
        char aux = ' ';
        String accumulate = "" ;
        int first=0;
        int second;
        for (char x:value.toCharArray()) {
            if(x=='(' || x==')'){
                aux=x;
                if(x==')'){
                    second=Integer.getInteger(accumulate);
                    coor_result.add(new Pair<>(first,second));
                }
                accumulate="";
            }
            else if(x==','){
                if(aux=='('){
                    first=Integer.getInteger(accumulate);
                }
            }
            else if(x!=' '){
                accumulate+=x;
            }

        }

        Bitmap btm_manual_left= Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);

        Canvas canvas= new Canvas(btm_manual_left);
        DrawDot all_dots = new DrawDot(size/(float)2,size/(float)2,coor_result, metric_unit/(float)2, metric_unit);
        all_dots.draw(canvas);
        draw_dots_left.setImageBitmap(btm_manual_left);
        draw_dots_left.setVisibility(View.VISIBLE);



    }


    public void Close(View view){
        finish();
    }
}