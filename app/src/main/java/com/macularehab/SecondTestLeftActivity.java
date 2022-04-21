package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.macularehab.actions.ManualActions;

public class SecondTestLeftActivity extends AppCompatActivity {

    private ManualActions actions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_second_test_left);
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm

        Display display_measure = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display_measure.getSize(point);
        float centre_x=point.x/(float)2;
        float centre_y=point.y/(float)2;

        ImageView grid=findViewById(R.id.circleGrid);
        grid.getLayoutParams().width = size;
        grid.getLayoutParams().height = size;
        grid.requestLayout();

        ImageView draw_space=findViewById(R.id.dotsPainting);
        draw_space.getLayoutParams().width = size;
        draw_space.getLayoutParams().height = size;
        draw_space.requestLayout();

        actions=new ManualActions(metric_unit,centre_x,centre_y,draw_space);

        ImageButton next=findViewById(R.id.nextButton);

        next.setOnClickListener(v -> {
            next_test(v);
        });

        ImageButton repeat=findViewById(R.id.repeatButton);
        repeat.setOnClickListener(v -> {
            actions.repeat();
        });

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close(v));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            actions.touch_happened(x,y);
            return true;
        }
        return false;
    }

    private void next_test(View v) {
        Intent i = new Intent( this, SecondTestRightExplanationActivity.class );
        String value = actions.getResult_coor().toString();
        i.putExtra("grid_left",value);
        value =  getIntent().getExtras().getString("map_both");
        i.putExtra("map_both",value);
        value = getIntent().getExtras().getString("map_right");
        i.putExtra("map_right",value);
        value = getIntent().getExtras().getString("map_left");
        i.putExtra("map_left",value);
        value = getIntent().getExtras().getString("manual_left");
        i.putExtra("manual_left",value);
        value= getIntent().getExtras().getString("manual_right");
        i.putExtra("manual_right",value);
        value= getIntent().getExtras().getString("manual_both");
        i.putExtra("manual_both",value);
        value= getIntent().getExtras().getString("patient_id");
        i.putExtra("patient_id",value);
        startActivity(i);
    }


    public void Close(View view){
        finish();
    }
}