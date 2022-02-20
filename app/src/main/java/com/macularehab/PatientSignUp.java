package com.macularehab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatientSignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_signup);

        //Initialize Authenticator
        mAuth = FirebaseAuth.getInstance();

        Button signUpButton = findViewById(R.id.patient_signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readEmail();
            }
        });
    }

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //TODO
            reload();
        }
    }*/

    private void readEmail() {

        TextView email_text = findViewById(R.id.patient_email_signup);
        TextView password_text = findViewById(R.id.patient_password_signup);

        if (email_text.getText().toString().isEmpty() || password_text.getText().toString().isEmpty()) {

            showAlertEnterEmailAndPassword();
        }
        else {

            String email = email_text.getText().toString();
            String password = password_text.getText().toString();

            createAccount(email, password);
        }
    }

    public void showAlertEnterEmailAndPassword() {

        //FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Email and/or Password field is empty")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(PatientSignUp.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);

                            showAlertFailToSignUp();
                        }
                    }
                });
        // [END create_user_with_email]
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

    //TODO
    private void reload() { }
    //TODO
    private void updateUI(FirebaseUser user) { }
}
