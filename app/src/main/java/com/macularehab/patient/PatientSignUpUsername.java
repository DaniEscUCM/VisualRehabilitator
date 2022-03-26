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
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;

public class PatientSignUpUsername extends AppCompatActivity {

    private EditText input_username;
    private Button button_continue_signup;
    private String username;
    private String password;

    public static final String extra_username = "com.macularehab.patient.extra_username";
    private static final String GENERIC_EMAIL = "@maculaRehabTFG.com";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_signup_username);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        password = intent.getStringExtra(PatientSignUpPassword.extra_password);

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

        ImageButton buttonBack = (ImageButton) findViewById(R.id.imageButton_back_ident);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buttonClicked() {

        username = input_username.getText().toString().trim();
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
            readUserName();
        }
    }

    public void readUserName() {

        int email_length = username.length();
        boolean is_email = false;

        for (int i = 0; i < email_length && !is_email; i++) {

            if (username.charAt(i) == '@') {
                is_email = true;
            }
        }

        if (!is_email) username += GENERIC_EMAIL;

        createAccount();
    }

    private void createAccount() {
        // [START create_user_with_email]

        //mAuth.signInAnonymously();
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.w("EMAIL", "createUserWithEmail:success");
                            Toast.makeText(PatientSignUpUsername.this, "User Created!", Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("EMAIL", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(PatientSignUpUsername.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            FirebaseError firebaseError = new FirebaseError(task.getException().hashCode());

                            switch (firebaseError.getErrorCode()) {

                                case FirebaseError.ERROR_EMAIL_ALREADY_IN_USE:
                                    break;
                                case FirebaseError.ERROR_WEAK_PASSWORD:
                                    break;
                                case FirebaseError.ERROR_WRONG_PASSWORD:
                                    break;
                                case FirebaseError.ERROR_NETWORK_REQUEST_FAILED:
                                    break;
                                case FirebaseError.ERROR_OPERATION_NOT_ALLOWED:
                                    break;
                            }
                        }
                    }
                });
    }

    /*private void isUsernameAvailable() {

        databaseReference.child("PatientsUsernames").child(username)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.w("PatientsSignUp", task.getException());
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
    }*/

    private void addUsernameToDB() {
        databaseReference.child("PatientsUsername").child(username).setValue(username);
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

    /*private void continueSignupProcess() {

        Intent signup_password_intent = new Intent(this, PatientSignUpPassword.class);
        signup_password_intent.putExtra(PatientSignUpUsername.extra_username, username);
        startActivity(signup_password_intent);
    }*/

    private void startPatientHomeActivity() {

        Intent patient_home_intent = new Intent(this, PatientHome.class);
        startActivity(patient_home_intent);
    }

}
