package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ExercisesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_exercises);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_exerc_menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });

        Button first_exercise_button = (Button) findViewById(R.id.button_exercise1);
        first_exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_exercise(v);
            }
        });
    }
    public void Close(View view){
        finish();
    }

    public void first_exercise(View view){
        Intent i = new Intent( this, FirstExerciseDescriptionActivity.class );
        startActivity(i);
    }
}