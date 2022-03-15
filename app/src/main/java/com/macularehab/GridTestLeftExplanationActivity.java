package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GridTestLeftExplanationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_grid_test_left_explanation);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_map_test);
        button.setOnClickListener(v -> Close(v));

        ImageButton button_play = (ImageButton) findViewById(R.id.imageButton_play);
        button_play.setOnClickListener(v -> play_test(v));
    }

    private void play_test(View v) {
        Intent i = new Intent( this, GridTestLeftActivity.class );
        startActivity(i);
    }

    public void Close(View view){
        finish();
    }
}