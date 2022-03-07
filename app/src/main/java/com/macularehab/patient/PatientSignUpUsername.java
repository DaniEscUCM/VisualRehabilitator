package com.macularehab.patient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;

public class PatientSignUpUsername extends AppCompatActivity {

    private EditText input_username;
    private Button button_continue_signup;
    private String username;

    public static final String extra_username = "com.macularehab.patient.extra_username";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_signup_username);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();

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
            isUsernameAvailable();
        }
    }

    private void isUsernameAvailable() {

        databaseReference.child("PatientsUsernames").child(username)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    Log.w("firebase", value);

                    if (value.equals("null")) {
                        addUsernameToDB();
                        continueSignupProcess();
                    }
                    else {
                        showAlertUsernameAlreadyExists();
                    }
                }
            }
        });
    }

    private void addUsernameToDB() {
        databaseReference.child("PatientsUsername").setValue(username);
    }

    //TODO colocar desde @strings el mensaje
    private void showAlertUsernameAlreadyExists() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Username Already Exists")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        input_username.setError("Username already exists");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void continueSignupProcess() {

        Intent signup_password_intent = new Intent(this, PatientSignUpPassword.class);
        signup_password_intent.putExtra(PatientSignUpUsername.extra_username, username);
        startActivity(signup_password_intent);
    }


}
