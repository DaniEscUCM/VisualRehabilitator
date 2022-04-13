package com.macularehab.exercises;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.macularehab.R;

public class ShowResultActivity extends AppCompatActivity {

    private TextView numCorrectText;
    private TextView numFailedText;

    private int numCorrect;
    private int numFailed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_exercises_show_results);

        numCorrectText = findViewById(R.id.exercises_showResult_numCorrect_textView);
        numFailedText = findViewById(R.id.exercises_showResult_numFailed_textView);

        readResults();

        Button buttonReturnToExercises = findViewById(R.id.exercises_showResult_returnExercises_button);
        buttonReturnToExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToExercises();
            }
        });
    }

    private void readResults() {

        numCorrect = getIntent().getIntExtra("numCorrect", 0);
        numFailed = getIntent().getIntExtra("numFailed", 0);

        numCorrectText.setText(numCorrect);
        numFailedText.setText(numFailed);
    }

    private void returnToExercises() {

        this.finish();
    }
}
