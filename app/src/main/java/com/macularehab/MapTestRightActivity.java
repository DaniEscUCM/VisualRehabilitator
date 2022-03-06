package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.macularehab.actions.MapTestActions;

public class MapTestRightActivity extends AppCompatActivity {

    MapTestActions action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_map_test_right);

        TextView instruction = findViewById(R.id.textInstruction);
        ImageView centre_dot = findViewById(R.id.centre_dot);
        ImageView grid = findViewById(R.id.circle_grid);
        ImageView out = findViewById(R.id.multiple_dots);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_map_test);
        button.setOnClickListener(v -> Close(v));

        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm

        action= new MapTestActions(instruction, centre_dot, grid, out, size, metric_unit) {
            @Override
            public void next() {
                ImageButton next=findViewById((R.id.nextButtonTestR));
                next.setVisibility(View.VISIBLE);

                next.setOnClickListener(v -> {
                    next_test(v);
                });

                ImageButton repeat=findViewById(R.id.repeatButton);
                repeat.setVisibility(View.VISIBLE);

                repeat.setOnClickListener(v -> {
                    next.setVisibility(View.GONE);
                    repeat.setVisibility(View.GONE);
                    action.repeat();
                });

            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        action.touch_happened();
        return true;
    }

    private void next_test(View v) {
        Intent i = new Intent( this, MapTestBothExplanationActivity.class );
        startActivity(i);
    }


    public void Close(View view){
        finish();
    }
}