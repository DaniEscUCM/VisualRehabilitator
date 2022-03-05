package com.macularehab.patient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.macularehab.PatientHome;
import com.macularehab.R;

public class PatientSignUpPassword extends AppCompatActivity {

    private EditText input_password_one;
    private EditText input_password_two;
    private Button button_signup;

    private String password_one;
    private String password_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_signup_password);

        input_password_one = findViewById(R.id.editText_patient_password_one);
        input_password_two = findViewById(R.id.editText_patient_passworsd_two);

        button_signup = findViewById(R.id.button_patient_password_signup);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked();
            }
        });
    }

    private void buttonClicked() {

        password_one = input_password_one.getText().toString();
        password_two = input_password_two.getText().toString();

        validatePassword();
    }

    //TODO colocar los errores tambien en espanhol
    private void validatePassword() {

        boolean all_correct = true;

        if (password_one.equals("")) {
            input_password_one.setError("Need to fill");
            all_correct = false;
        }
        if (password_two.equals("")) {
            input_password_two.setError("Need to fill");
            all_correct = false;
        }
        if (!password_one.equals(password_two)) {
            input_password_one.setError("Passwords doesn't match");
            input_password_two.setError("Passwords doesn't match");
            all_correct = false;
            showAlertPasswordsDoesNotMatch();
        }

        if (all_correct) {
            startPatientHomeActivity();
        }
    }

    private void showAlertPasswordsDoesNotMatch() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Passwords doesn't match")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        input_password_one.setText("");
                        input_password_two.setText("");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startPatientHomeActivity() {

        Intent patient_home_intent = new Intent(this, PatientHome.class);
        startActivity(patient_home_intent);
    }
}
