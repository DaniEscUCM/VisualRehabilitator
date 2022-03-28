package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
//Firebase Analytics
import com.google.firebase.analytics.FirebaseAnalytics;
import com.macularehab.login.LogIn;
import com.macularehab.patient.PatientHome;

public class PatientLogin extends AppCompatActivity  {

    private FirebaseAnalytics mFirebaseAnalytics;
    private LogIn logIn;
    private TextView email_text;
    private TextView password_text;
    private String email_username;
    private String password;
    public static final String getUserName = "com.macularehab.getUserName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_login);

        this.logIn = new LogIn();

        this.email_text = findViewById(R.id.patient_email_login);
        this.password_text = findViewById(R.id.patient_password_login);

        Log.w("actividad LogIn", " creada");

        Button signUpButton = findViewById(R.id.patient_login_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.w("boton LogIn", " presionado");
                readEmail(); //Se bloquea para hacerlo bien desde professional
            }
        });

        ImageButton buttonBack = (ImageButton) findViewById(R.id.imageButton_back_pat_login);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(v);
            }
        });

    }

    public void close(View view){
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        Log.w("actividad LogIn", " empezada");
        boolean signedIn = this.logIn.patient_is_signed_in(this);
        if(signedIn){
            Log.w("actividad LogIn", " paciente conectado");
            reload();
        }
    }

    private void readEmail() {

        this.email_username = this.email_text.getText().toString();
        this.password = this.password_text.getText().toString();

        Log.w("EMAIL: ", email_username);
        Log.w("PASSWORD: ", password);

        if (validate_user_input()) {
            this.logIn.readEmail(email_username, password, this);
        }
    }

    public void user_loggedIn_successfully() {

        Toast.makeText(PatientLogin.this, "User LoggedIn", Toast.LENGTH_LONG).show();
        reload();
        clean();
    }

    public void user_login_failed() {

        Toast.makeText(PatientLogin.this, "Authentication failed.",
                Toast.LENGTH_LONG).show();
        showAlertFailToLogIn();
    }


    public boolean validate_user_input() {

        boolean all_correct = true;

        if(this.email_username.equals("")){
            this.email_text.setError("required");
            all_correct = false;
            showAlertEnterEmailAndPassword();
        }
        if(this.password.equals("")){
            this.password_text.setError("required");
            all_correct = false;
            showAlertEnterEmailAndPassword();
        }
        if (this.password.length() < 6) {
            this.password_text.setError("Password must be at least 6 characters");
            all_correct = false;
            showAlertWeakPassword();
        }

        return all_correct;
    }

    public void showAlertEnterEmailAndPassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Name and/or Email and/or Password field is empty")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlertWeakPassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Name and/or Email and/or Password field is empty")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showAlertFailToLogIn() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Failed To LogIn. Email/Username and/or Password incorrect")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clean(){
        this.email_text.setText("");
        this.password_text.setText("");
    }

    public void reload() {

        Intent intent = new Intent( this, PatientHome.class);
        startActivity(intent);
    }
}
