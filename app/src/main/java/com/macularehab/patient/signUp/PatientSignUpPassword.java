package com.macularehab.patient.signUp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;

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
    private final String db_patientsNumericCode = "PatientsNumericCodes";
    private String userID;
    private String professionalID;

    Map<String, Object> patientInfo;

    public static final String extra_password = "com.macularehab.patient.extra_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_signup_password);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        input_password_one = findViewById(R.id.editText_patient_password_one);
        input_password_two = findViewById(R.id.editText_patient_passworsd_two);

        button_signup = findViewById(R.id.button_patient_password_signup);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked();
            }
        });

        ImageButton buttonBack = findViewById(R.id.imageButton_back_ident);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buttonClicked() {

        password_one = input_password_one.getText().toString().trim();
        password_two = input_password_two.getText().toString().trim();

        validatePassword();
    }

    private void validatePassword() {

        boolean all_correct = true;

        Resources resources = this.getResources();
        String st_needToFill = resources.getString(R.string.message_fill_fields);

        if (password_one.equals("")) {
            input_password_one.setError(st_needToFill);
            all_correct = false;
        }
        if (password_two.equals("")) {
            input_password_two.setError(st_needToFill);
            all_correct = false;
        }
        if (!password_one.equals(password_two)) {
            String passwordsDoestMatch = resources.getString(R.string.patient_signup_passwords_doest_match);
            input_password_one.setError(passwordsDoestMatch);
            input_password_two.setError(passwordsDoestMatch);
            all_correct = false;
            showAlertPasswordsDoesNotMatch(passwordsDoestMatch);
        }

        if (password_one.equals("") || password_two.equals("")) {
            showAlertPasswordsEmpty(st_needToFill);
        }

        if (all_correct) {
            isPasswordCorrect();
        }
    }

    private void showAlertPasswordsEmpty(String st_needToFill) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(st_needToFill)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertPasswordsDoesNotMatch(String passwordsDoestMatch) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(passwordsDoestMatch)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //input_password_one.setText("");
                        //input_password_two.setText("");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void isPasswordCorrect() {

        databaseReference.child(db_patientsNumericCode).child(password_one)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    showAlertErrorRegister();
                    //Toast.makeText(PatientSignUpPassword.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());

                    if (value.equals("null")) {
                        showAlertPasswordsDoesNotExist();
                    }
                    else {

                        goToSignUpUsername();
                    }
                }
            }
        });
    }

    private void goToSignUpUsername() {

        Intent intent = new Intent(this, PatientSignUpUsername.class);
        intent.putExtra(extra_password, password_one);
        startActivity(intent);
    }

    private void updatePatient() {

        patientInfo.put("username", username);
        patientInfo.put("patientCode", password_one);
        patientInfo.put("patient_uid", userID);
        databaseReference.child("Patient").child(userID).setValue(patientInfo);

        databaseReference.child("Patient").child(password_one).removeValue();

        professionalID = String.valueOf(patientInfo.get("professional_uid"));

        databaseReference.child("Professional").child(professionalID).child("Patients").child(password_one).setValue(patientInfo);
    }

    private void showAlertPasswordsDoesNotExist() {

        Resources resources = this.getResources();
        String st_passwordDoestExist = resources.getString(R.string.patient_signup_password_doest_exist);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(st_passwordDoestExist)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        input_password_one.setText("");
                        input_password_two.setText("");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertErrorRegister() {

        Resources resources = this.getResources();
        String st_error = resources.getString(R.string.message_error);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(st_error)
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
