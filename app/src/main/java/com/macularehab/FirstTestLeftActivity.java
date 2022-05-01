package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.macularehab.actions.MapTestActions;

public class FirstTestLeftActivity extends AppCompatActivity{

    MapTestActions action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_first_test_left);

        TextView instruction = findViewById(R.id.textInstruction);
        ImageView centre_dot = findViewById(R.id.centre_dot);
        ImageView grid = findViewById(R.id.circle_grid);
        ImageView out = findViewById(R.id.multiple_dots);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close(v));

        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm

        action= new MapTestActions(instruction, centre_dot, grid, out, size, metric_unit) {
            @Override
            public void next() {
                ImageButton next=findViewById((R.id.nextButton));
                next.setVisibility(View.VISIBLE);

                next.setOnClickListener(v -> {
                    next_test(v);
                });

                ImageButton repeat=findViewById(R.id.repeatButton);
                repeat.setVisibility(View.VISIBLE);

                repeat.setOnClickListener(v -> {
                    next.setVisibility(View.GONE);
                    repeat.setVisibility(View.GONE);
                    action.repeat();
                });

            }
        };

        setUiListener();
    }

    private void setUiListener() {

        View decorView = getWindow().getDecorView();

        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 2000ms
                                    hideNavigationAndStatusBar();
                                }
                            }, 2000);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideNavigationAndStatusBar();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        action.touch_happened();
        return true;
    }
    private void next_test(View v) {
        Intent i = new Intent( this, FirstTestRightExplanationActivity.class );
        Gson gson = new Gson();
        String value= gson.toJson(action.getCoor_result());
        i.putExtra("map_left",value);
        value = getIntent().getExtras().getString("manual_left");
        i.putExtra("manual_left",value);
        value= getIntent().getExtras().getString("manual_right");
        i.putExtra("manual_right",value);
        value= getIntent().getExtras().getString("manual_both");
        i.putExtra("manual_both",value);
        value= getIntent().getExtras().getString("patient_id");
        i.putExtra("patient_id",value);
        startActivity(i);
    }

    public void Close(View view){
        finish();
    }

    private void hideNavigationAndStatusBar() {

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        decorView.setSystemUiVisibility(uiOptions);
    }
}
