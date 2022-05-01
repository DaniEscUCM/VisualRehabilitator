package com.macularehab.exercises;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.macularehab.R;

public class ChooseExerciseActivity extends AppCompatActivity {

    private int exercise_id;
    private Resources resources;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_exercises);

        resources = this.getResources();
        TextView textExercises = findViewById(R.id.chooseExercise_exerciseText_textView);
        textExercises.setText(resources.getString(R.string.choose_exercise_exercises_text_hint));

        Button button_exercise_1 = findViewById(R.id.button_exercise1);
        button_exercise_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertExerciseOneDoesNotHaveResults();
            }
        });

        Button button_exercise_2 = findViewById(R.id.button_exercise2);
        button_exercise_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 1;
                showResults();
            }
        });

        Button button_exercise_3 = findViewById(R.id.button_exercise3);
        button_exercise_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 2;
                showResults();
            }
        });

        Button button_exercise_4 = findViewById(R.id.button_exercise4);
        button_exercise_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 3;
                showResults();
            }
        });

        Button button_exercise_5 = findViewById(R.id.button_exercise5);
        button_exercise_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 4;
                showResults();
            }
        });

        Button button_exercise_6 = findViewById(R.id.button_exercise6);
        button_exercise_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 5;
                showResults();
            }
        });

        Button button_exercise_7 = findViewById(R.id.button_exercise7);
        button_exercise_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 6;
                showResults();
            }
        });

        Button button_exercise_8 = findViewById(R.id.button_exercise8);
        button_exercise_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 7;
                showResults();
            }
        });

        Button button_exercise_9 = findViewById(R.id.button_exercise9);
        button_exercise_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 8;
                showResults();
            }
        });

        Button button_exercise_10 = findViewById(R.id.button_exercise10);
        button_exercise_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 9;
                showResults();
            }
        });

        Button button_exercise_11 = findViewById(R.id.button_exercise11);
        button_exercise_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 10;
                showResults();
            }
        });

        Button button_exercise_12 = findViewById(R.id.button_exercise12);
        button_exercise_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 11;
                showResults();
            }
        });

        Button button_exercise_13 = findViewById(R.id.button_exercise13);
        button_exercise_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 12;
                showResults();
            }
        });

        Button button_exercise_14 = findViewById(R.id.button_exercise14);
        button_exercise_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 13;
                showResults();
            }
        });

        Button button_exercise_15 = findViewById(R.id.button_exercise15);
        button_exercise_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 14;
                showResults();
            }
        });

        Button button_exercise_16 = findViewById(R.id.button_exercise16);
        button_exercise_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 15;
                showResults();
            }
        });

        Button button_exercise_17 = findViewById(R.id.button_exercise17);
        button_exercise_17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_id = 16;
                showResults();
            }
        });

        ImageButton back_button = findViewById(R.id.imageButton_back_exerc_menu);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    private void showResults() {

        Intent intent = new Intent(this, ExerciseResultHistory.class);
        intent.putExtra("exercise_id", exercise_id);
        startActivity(intent);
    }

    private void showAlertExerciseOneDoesNotHaveResults() {

        Resources resources = ChooseExerciseActivity.this.getResources();
        String st_error = resources.getString(R.string.exercises_show_results_exerciseOneDoesNotHaveResults);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(st_error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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
