package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DistanceExercisesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_distance_exercises);

        ImageButton dataButton = (ImageButton) findViewById(R.id.imageButton_play);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToExercise();
            }
        });

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });
    }
    public void Close(View view){
        finish();
    }
    private void goToExercise(){

        Intent intent = new Intent(this, ExercisesActivity.class);
        startActivity(intent);
    }
}