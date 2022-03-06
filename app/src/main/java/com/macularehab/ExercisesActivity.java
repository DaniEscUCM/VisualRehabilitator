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
                close(v);
            }
        });

        //All the bottons for each exercise:

        Button first_exercise_button = (Button) findViewById(R.id.button_exercise1);
        first_exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_exercise(v);
            }
        });

        Button second_exercise_button = (Button) findViewById(R.id.button_exercise2);
        second_exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second_exercise(v);
            }
        });

        Button third_exercise_button = (Button) findViewById(R.id.button_exercise3);
        third_exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                third_exercise(v);
            }
        });
        Button fourth_exercise_button = (Button) findViewById(R.id.button_exercise4);
        fourth_exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fourth_exercise(v);
            }
        });
        Button fifth_exercise_button = (Button) findViewById(R.id.button_exercise5);
        fifth_exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fifth_exercise(v);
            }
        });
        Button sixth_exercise_button = (Button) findViewById(R.id.button_exercise6);
        sixth_exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sixth_exercise(v);
            }
        });

    }
    public void close(View view){
        finish();
    }

    public void first_exercise(View view){
        Intent i = new Intent( this, FirstExerciseDescriptionActivity.class );
        startActivity(i);
    }
    public void second_exercise(View view){
        Intent i = new Intent(this, SecondExerciseDescriptionActivity.class);
        startActivity(i);
    }

    public void third_exercise(View view){
        Intent i = new Intent( this, ThirdExerciseDescriptionActivity.class );
        startActivity(i);
    }
    public void fourth_exercise(View view){
        Intent i = new Intent( this, FourthExerciseDescriptionActivity.class );
        startActivity(i);
    }
    public void fifth_exercise(View view){
        Intent i = new Intent( this, FifthExerciseDescriptionActivity.class );
        startActivity(i);
    }
    public void sixth_exercise(View view){
        Intent i = new Intent( this, SixthExerciseDescriptionActivity.class );
        startActivity(i);
    }
}