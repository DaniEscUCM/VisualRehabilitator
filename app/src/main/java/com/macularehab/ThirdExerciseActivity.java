package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdExerciseActivity extends AppCompatActivity {
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //move();
        ImageButton button_dot = (ImageButton) findViewById(R.id.dot_button);
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = move(counter);
            }
        });

        ImageButton button_setting = (ImageButton) findViewById(R.id.third_exercise_settings);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings(v);
            }
        });

        ImageButton button_home = (ImageButton) findViewById(R.id.home_button);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });
    }

    private int move(int counter){
        ImageButton button_dot = (ImageButton) findViewById(R.id.dot_button);
        Display disp_info = getWindowManager().getDefaultDisplay();
        Point point_info = new Point();
        disp_info.getSize(point_info);
        if(counter == 0) {   //La primera vez aparece en el centro
            int x = (int) ((point_info.x - (2 * button_dot.getWidth()))) + button_dot.getWidth();
            int y = (int) ((point_info.y - (2 * button_dot.getHeight()))) + button_dot.getHeight();
            button_dot.getPivotX();
            button_dot.setX(x);
            button_dot.setY(y);
        }
        else if(counter > 0 && counter < 5) {
            disp_info.getSize(point_info);
            int u = (int) (Math.random() * (point_info.x - (2 * button_dot.getWidth()))) + button_dot.getWidth();
            int v = (int) (Math.random() * (point_info.y - (2 * button_dot.getHeight()))) + button_dot.getHeight();
            button_dot.getPivotX();
            button_dot.setX(u);
            button_dot.setY(v);
        }
        else {
            finish();
        }
        return counter++;
    }

    public void Close(View view){
        finish();
    }

    public void Settings(View view){
        Intent i = new Intent( this, SettingsActivity.class );
        startActivity(i);
    }
}