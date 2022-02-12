package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MapTest extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_map_test);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Make instruction disappear
                    TextView instruction = findViewById((R.id.textInstruction));
                    //Thread.sleep(600000);
                    (new Handler()).postDelayed(this, 10000);
                    instruction.setVisibility(View.GONE);

                    //Make centre dot blink three times
                    ImageView centre_dot= findViewById(R.id.centre_dot);
                    blink(centre_dot);
                    centre_dot.setVisibility(View.VISIBLE);

                    //Start test
                    ImageView find_dot=  findViewById(R.id.find_dot);
                    ImageView grid = findViewById(R.id.circle_grid);
                    testing(centre_dot,find_dot,grid);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void blink(View obj) throws InterruptedException {

        obj.setVisibility(View.VISIBLE);
        (new Handler()).postDelayed((Runnable) this, 10000);
        obj.setVisibility(View.INVISIBLE);
        (new Handler()).postDelayed((Runnable) this, 10000);

        obj.setVisibility(View.VISIBLE);
        (new Handler()).postDelayed((Runnable) this, 10000);
        obj.setVisibility(View.INVISIBLE);
        (new Handler()).postDelayed((Runnable) this, 10000);

        obj.setVisibility(View.VISIBLE);
        (new Handler()).postDelayed((Runnable) this, 10000);
        obj.setVisibility(View.INVISIBLE);
        (new Handler()).postDelayed((Runnable) this, 10000);

    }
    private void testing(ImageView centre_dot, ImageView find_dot, ImageView grid) throws InterruptedException {
        blink(find_dot);
    }


}
