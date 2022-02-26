package com.macularehab;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.macularehab.login.SignUp;

public class PatientSignUp extends AppCompatActivity {

    private SignUp signUp;

    private TextView name_text;
    private TextView email_text;
    private TextView password_text;

    private String name;
    private String email_username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_signup);

        this.signUp = new SignUp();

        this.name_text = findViewById(R.id.patient_name_signup);
        this.email_text = findViewById(R.id.patient_email_signup);
        this.password_text = findViewById(R.id.patient_password_signup);

        Log.w("actividad signUp", " creada");

        Button signUpButton = findViewById(R.id.patient_signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.w("boton signUp", " presionado");
                readEmail();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        Log.w("actividad signUp", " empezada");
        boolean signedIn = this.signUp.user_is_signed_in();
        if(signedIn){
            Log.w("actividad", " paciente conectado");
            //TODO hay que hacer la interfaz en caso de que el usuario este ya iniciado
            reload();
        }
    }

    private void readEmail() {

        this.name = this.name_text.getText().toString();
        this.email_username = this.email_text.getText().toString();
        this.password = this.password_text.getText().toString();

        Log.w("EMAIL: ", email_username);
        Log.w("PASSWORD: ", password);

        validate_user_input();

        this.signUp.readEmail(name, email_username, password, this);
    }

    public void patient_signed_successfully() {

        Toast.makeText(PatientSignUp.this, "User created", Toast.LENGTH_LONG).show();
        clean();
        reload();
    }

    public void patient_signingUp_failed() {

        Toast.makeText(PatientSignUp.this, "Authentication failed.",
                Toast.LENGTH_LONG).show();
        showAlertFailToSignUp();
    }

    public void validate_user_input(){

        if(this.name.equals("")){
            this.name_text.setError("required");
            showAlertEnterEmailAndPassword();
        }
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


    public void showAlertFailToSignUp() {

        //FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Failed To SignUp")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clean(){
        this.name_text.setText("");
        this.email_text.setText("");
        this.password_text.setText("");
    }

    //TODO
    private void reload() {

        Intent intent = new Intent( this, PatientHome.class);
        startActivity(intent);
    }
    //TODO
    private void updateUI(FirebaseUser user) { }
}
