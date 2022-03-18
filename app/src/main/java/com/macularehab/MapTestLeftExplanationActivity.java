package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MapTestLeftExplanationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_map_test_left_explanation);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close(v));

        ImageButton button_play = (ImageButton) findViewById(R.id.imageButton_play);
        button_play.setOnClickListener(v -> play_test(v));

        Button skip = findViewById(R.id.skip_button);
        skip.setOnClickListener(v -> skip(v));
    }

    private void play_test(View v) {
        Intent i = new Intent( this, MapTestLeftActivity.class );
        String value = getIntent().getExtras().getString("manual_left");
        i.putExtra("manual_left",value);
        value= getIntent().getExtras().getString("manual_right");
        i.putExtra("manual_right",value);
        value= getIntent().getExtras().getString("manual_both");
        i.putExtra("manual_both",value);
        startActivity(i);
    }

    private void skip(View v) {
        Intent i = new Intent( this, MapTestRightExplanationActivity.class );
        String value = getIntent().getExtras().getString("manual_left");
        i.putExtra("manual_left",value);
        value= getIntent().getExtras().getString("manual_right");
        i.putExtra("manual_right",value);
        value= getIntent().getExtras().getString("manual_both");
        i.putExtra("manual_both",value);
        startActivity(i);
    }

    public void Close(View view){
        finish();
    }
}