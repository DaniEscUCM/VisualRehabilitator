package com.macularehab.patient;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.macularehab.R;

public class PatientSignUpUsername extends AppCompatActivity {

    private EditText input_username;
    private Button button_continue_signup;
    private String username;

    public static final String extra_username = "com.macularehab.patient.extra_username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_signup_username);

        //EditText of username
        input_username = findViewById(R.id.editText_patient_signup_username);
        //Button
        button_continue_signup = findViewById(R.id.button_patient_continue_signup);
        button_continue_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked();
            }
        });
    }

    private void buttonClicked() {

        username = input_username.getText().toString();
        validateUsername();
    }

    //TODO Hacer que verifique que el nombre de usuario no exista ya
    private void validateUsername() {

        boolean all_correct = true;

        if (username.equals("")) {
            input_username.setError("Need to fill");
            all_correct = false;
        }

        if (all_correct) {
            continueSignupProcess();
        }
    }

    private void continueSignupProcess() {

        Intent signup_password_intent = new Intent(this, PatientSignUpPassword.class);
        signup_password_intent.putExtra(PatientSignUpUsername.extra_username, username);
        startActivity(signup_password_intent);
    }


}
