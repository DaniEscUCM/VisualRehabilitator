package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.draws.DrawDot;
import com.macularehab.exercises.ExerciseWriteDB;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.internalStorage.WriteInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;


public class FirstExerciseActivity extends AppCompatActivity {


    private CountDownTimer timer;

    private final String filenameCurrentUser = "CurrentPatient.json";
    private long time_left = 3000;
    private ImageButton button_dot;
    private final String isFocus = "focusIsOn";
    private boolean isOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_exercise);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ReadInternalStorage readIS = new ReadInternalStorage();
        HashMap<String, Object> map = readIS.read(getApplicationContext(), filenameCurrentUser);

        button_dot = findViewById(R.id.dot_button);
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { results(); }
        });

        ImageButton button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(v -> close());

        ImageButton button_pause = findViewById(R.id.pause_button);
        button_pause.setOnClickListener(v -> pause_menu());

        ImageButton button_resume = findViewById(R.id.return_button);
        button_resume.setOnClickListener(v->resume());

        Switch focus_switch = findViewById(R.id.focus_switch1);
        focus_switch.setChecked((Boolean) map.get(isFocus));
        isOn=(Boolean) map.get(isFocus);
        focus_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
            HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentUser);
            isOn=!(Boolean)mapS.get(isFocus);
        });

        //Calculate based on screen size
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int metric_unit=(int) Math.round(display.xdpi * 0.19685); //0.5cm
        int size = metric_unit*20;//10cm

        ImageView focus = findViewById(R.id.focus_point);
        ArrayList<Pair<Float, Float>> coor_result;
        LinkedTreeMap tree= (LinkedTreeMap)map.get("focus");
        coor_result = new ArrayList<>();
        coor_result.add(new Pair<>(Float.parseFloat(tree.get("first").toString()), Float.parseFloat(tree.get("second").toString())));

        focus.getLayoutParams().width = size;
        focus.getLayoutParams().height = size;
        focus.requestLayout();

        Bitmap btm_manual_left = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btm_manual_left);
        DrawDot all_dots = new DrawDot(size / (float) 2, size / (float) 2, coor_result, metric_unit / (float) 2, metric_unit, Color.RED);
        all_dots.draw(canvas);
        focus.setImageBitmap(btm_manual_left);
        focus.setVisibility(View.VISIBLE);

        button_dot.getLayoutParams().width = metric_unit*6;
        button_dot.getLayoutParams().height = metric_unit*6;
        focus.requestLayout();

         start();

    }

    private void pause_menu(){
        timer.cancel();
        button_dot.setClickable(false);
        ImageView gone_image = findViewById(R.id.pause);
        gone_image.setVisibility(View.GONE);
        TextView txt_gone=findViewById(R.id.textoPause);
        txt_gone.setVisibility(View.GONE);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.VISIBLE);
        ImageView inst = findViewById(R.id.volver);
        inst.setVisibility(View.VISIBLE);
        TextView txt=findViewById(R.id.textoVolver);
        txt.setVisibility(View.VISIBLE);
    }

    private void resume(){
        button_dot.setClickable(true);
        ConstraintLayout menu=findViewById(R.id.menu);
        menu.setVisibility(View.GONE);
        ImageView inst = findViewById(R.id.volver);
        inst.setVisibility(View.GONE);
        TextView txt=findViewById(R.id.textoVolver);
        txt.setVisibility(View.GONE);
        start();
    }

    public void start() {
        timer = new CountDownTimer(time_left, 1000) {

            public void onTick(long millisUntilFinished) {
                time_left=millisUntilFinished;
            }

            public void onFinish() {
                button_dot.setVisibility(View.VISIBLE);
            }

        }.start();

    }

    private void results(){
        saveFocusOn();
        Intent i = new Intent( this, ResultsFirstExerciseActivity.class );
        startActivity(i);
        finish();
    }

    public void close(){
        Intent i = new Intent( this, FirstExerciseDescriptionActivity.class );
        startActivity(i);
        finish();
    }

    private void saveFocusOn(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        ReadInternalStorage readInternalStorageS = new ReadInternalStorage();
        HashMap<String, Object> mapS= readInternalStorageS.read(getApplicationContext(), filenameCurrentUser);

        mapS.put(isFocus, isOn);

        Gson gson = new Gson();
        String data = gson.toJson(mapS);
        WriteInternalStorage writeInternalStorage = new WriteInternalStorage();
        writeInternalStorage.write(getApplicationContext(), filenameCurrentUser, data);
        databaseReference.child("Professional").child((String) mapS.get("professional_uid")).
                child("Patients").child((String) mapS.get("patient_numeric_code")).child(isFocus).setValue(isOn);
    }

}