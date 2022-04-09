package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class FirstExerciseDescriptionActivity extends AppCompatActivity {

    private final String filenameCurrentUser = "CurrentPatient.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_first_exercise_description);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_exerc1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });

        ImageButton button_play = (ImageButton) findViewById(R.id.button_play_ex1);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_exercise(v);
            }
        });

        ImageButton button_settings = (ImageButton) findViewById(R.id.conf_first_exercise_button);
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting(v);
            }
        });
    }

    private void setting(View v) {
        Intent i = new Intent( this, SettingsActivity.class );
        startActivity(i);
    }

    private void play_exercise(View v) {
        Intent i = new Intent( this, FirstExerciseActivity.class );
        startActivity(i);
    }

    public void Close(View view){
        finish();
    }


}
