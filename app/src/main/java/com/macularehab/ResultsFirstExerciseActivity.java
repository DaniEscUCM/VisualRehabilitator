package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResultsFirstExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_first_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button button_dot = findViewById(R.id.finish_first_ex);
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { exmenu(); }
        });
    }

    private void exmenu(){
        Intent i = new Intent( this, ExercisesActivity.class );
        startActivity(i);
    }

}