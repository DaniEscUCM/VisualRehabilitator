package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MapTestBothExplanationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_map_test_both_explanation);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close(v));

        ImageButton button_play = (ImageButton) findViewById(R.id.imageButton_play);
        button_play.setOnClickListener(v -> play_test(v));

        Button skip = findViewById(R.id.skip_button);
        skip.setOnClickListener(v -> skip(v));
    }

    private void play_test(View v) {
        Intent i = new Intent( this, MapTestBothActivity.class );
        String value = getIntent().getExtras().getString("map_right");
        i.putExtra("map_right",value);
        value = getIntent().getExtras().getString("map_left");
        i.putExtra("map_left",value);
        value = getIntent().getExtras().getString("manual_left");
        i.putExtra("manual_left",value);
        value= getIntent().getExtras().getString("manual_right");
        i.putExtra("manual_right",value);
        value= getIntent().getExtras().getString("manual_both");
        i.putExtra("manual_both",value);
        startActivity(i);
    }

    private void skip(View v) {
        Intent i = new Intent( this, GridTestLeftExplanationActivity.class );
        String value = getIntent().getExtras().getString("map_right");
        i.putExtra("map_right",value);
        value = getIntent().getExtras().getString("map_left");
        i.putExtra("map_left",value);
        value = getIntent().getExtras().getString("manual_left");
        i.putExtra("manual_left",value);
        value= getIntent().getExtras().getString("manual_right");
        i.putExtra("manual_right",value);
        value= getIntent().getExtras().getString("manual_both");
        startActivity(i);
    }

    public void Close(View view){
        finish();
    }
}