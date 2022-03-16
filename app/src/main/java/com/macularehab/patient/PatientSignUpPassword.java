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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.PatientHome;
import com.macularehab.PatientSignUp;
import com.macularehab.R;
import com.macularehab.login.SignUp;

import java.util.Map;

public class PatientSignUpPassword extends AppCompatActivity {

    private String username;

    private EditText input_password_one;
    private EditText input_password_two;
    private Button button_signup;

    private String password_one;
    private String password_two;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String userID;
    private String professionalID;

    Map<String, Object> patientInfo;

    private static final String GENERIC_EMAIL = "@maculaRehabTFG.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_signup_password);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        username = intent.getStringExtra(PatientSignUpUsername.extra_username);

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
            isPasswordCorrect();
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

    private void isPasswordCorrect() {

        databaseReference.child("Patient").child(password_one)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    Log.w("firebase", value);

                    if (value.equals("null")) {
                        showAlertPasswordsDoesNotExist();
                    }
                    else {

                        patientInfo = (Map<String, Object>) task.getResult().getValue();
                        readEmail();
                    }
                }
            }
        });
    }

    private void updatePatient() {

        patientInfo.put("username", username);
        patientInfo.put("patientCode", password_one);
        patientInfo.put("patient_uid", userID);
        databaseReference.child("Patient").child(userID).setValue(patientInfo);

        databaseReference.child("Patient").child(password_one).removeValue();

        professionalID = String.valueOf(patientInfo.get("professional_uid"));

        databaseReference.child("Professional").child(professionalID).child("Patients").child(password_one).setValue(patientInfo);

        startPatientHomeActivity();
    }

    private void showAlertPasswordsDoesNotExist() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Password Incorrect")
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

    public void readEmail() {

        int email_length = username.length();
        boolean is_email = false;

        for (int i = 0; i < email_length && !is_email; i++) {

            if (username.charAt(i) == '@') {
                is_email = true;
            }
        }

        if (!is_email) {
            username += GENERIC_EMAIL;
        }

        createAccount();
    }

    private void createAccount() {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(username, password_one)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.w("EMAIL", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            updatePatient();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("EMAIL", "createUserWithEmail:failure", task.getException());
                            showAlertErrorRegister();
                        }
                    }
                });
    }

    private void showAlertErrorRegister() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Error! Try Again Later")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        input_password_one.setText("");
                        input_password_two.setText("");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
