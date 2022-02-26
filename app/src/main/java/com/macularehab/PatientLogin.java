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
import android.widget.TextView;
import android.widget.Toast;
//Firebase Analytics
import com.google.firebase.analytics.FirebaseAnalytics;
import com.macularehab.login.LogIn;

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

        this.logIn = new LogIn(this);

        this.email_text = findViewById(R.id.patient_email_login);
        this.password_text = findViewById(R.id.patient_password_login);

        Log.w("actividad LogIn", " creada");

        Button signUpButton = findViewById(R.id.patient_login_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.w("boton LogIn", " presionado");
                readEmail();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        Log.w("actividad LogIn", " empezada");
        boolean signedIn = this.logIn.user_is_signed_in();
        if(signedIn){
            Log.w("actividad LogIn", " paciente conectado");
            //TODO hay que hacer la interfaz en caso de que el usuario este ya iniciado
            reload();
        }
    }

    private void readEmail() {

        this.email_username = this.email_text.getText().toString();
        this.password = this.password_text.getText().toString();

        Log.w("EMAIL: ", email_username);
        Log.w("PASSWORD: ", password);

        validate_user_input();

        this.logIn.readEmail(email_username, password, this);
    }

    public void user_loggedIn_successfully() {

        Toast.makeText(PatientLogin.this, "User LoggedIn", Toast.LENGTH_LONG).show();
        reload();
        clean();
    }

    public void user_loggin_failed() {

        Toast.makeText(PatientLogin.this, "Authentication failed.",
                Toast.LENGTH_LONG).show();
        showAlertFailToLogIn();
    }


    public void validate_user_input(){

        if(this.email_username.equals("")){
            this.email_text.setError("required");
            showAlertEnterEmailAndPassword();
        }
        if(this.password.equals("")){
            this.password_text.setError("required");
            showAlertEnterEmailAndPassword();
        }
        if (this.password.length() < 6) {
            this.password_text.setError("Password must be at least 6 characters");
        }
    }

    public void showAlertEnterEmailAndPassword() {

        //FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();

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

        //FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();

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

    //TODO
    private void reload() {

        Intent intent = new Intent( this, PatientHome.class);
        startActivity(intent);
    }
}
