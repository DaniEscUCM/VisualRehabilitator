package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        ImageButton button1 = (ImageButton) findViewById(R.id.imageButton_settings);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings(v);
            }
        });

        Button button2 = (Button) findViewById(R.id.button_exercises);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exercises(v);
            }
        });
    }

    public void Settings(View view){
        Intent i = new Intent( this, SettingsActivity.class );
        startActivity(i);
    }

    public void Exercises(View view){
        Intent i = new Intent( this, ExercisesActivity.class );
        startActivity(i);
    }
}