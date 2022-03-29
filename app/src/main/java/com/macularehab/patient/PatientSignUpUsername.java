package com.macularehab.patient;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;

import java.util.Map;

public class PatientSignUpUsername extends AppCompatActivity {

    private EditText input_username;
    private Button button_continue_signup;
    private String username;
    private String password;
    private LottieAnimationView loading_imageView;

    private final String db_patientsNumericCode = "PatientsNumericCodes";
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

        //Get the password
        Intent intent = getIntent();
        password = intent.getStringExtra(PatientSignUpPassword.extra_password);

        //Loading Image
        loading_imageView = findViewById(R.id.patient_signup_loading_imageView);
        loading_imageView.setVisibility(View.INVISIBLE);

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

        showLoadingImage();
        createAccount();
    }

    private void showLoadingImage() {
        
        loading_imageView.setVisibility(View.VISIBLE);
        loading_imageView.setAnimation(R.raw.loading_rainbow);
        loading_imageView.playAnimation();
        loading_imageView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                loading_imageView.playAnimation();
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void createAccount() {

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        loading_imageView.cancelAnimation();
                        loading_imageView.setImageResource(R.drawable.ic_launcher_foreground);
                        loading_imageView.setVisibility(View.INVISIBLE);

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.w("EMAIL", "createUserWithEmail:success");
                            Toast.makeText(PatientSignUpUsername.this, "User Created!", Toast.LENGTH_LONG).show();
                            updatePatientUID();
                            startPatientHomeActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("EMAIL", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(PatientSignUpUsername.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            FirebaseAuthException firebaseAuthException = (FirebaseAuthException) task.getException();
                            Resources resources = PatientSignUpUsername.this.getResources();
                            String st_error;

                            switch (firebaseAuthException.getErrorCode()) {

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    st_error = resources.getString(R.string.patient_signup_error_user_already_in_use);
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    st_error = resources.getString(R.string.patient_signup_error_weak_password);
                                    break;
                                case "ERROR_WRONG_PASSWORD":
                                    st_error = resources.getString(R.string.patient_signup_error_wrong_password);
                                    break;
                                case "ERROR_NETWORK_REQUEST_FAILED":
                                    st_error = resources.getString(R.string.patient_signup_error_network_failed);
                                    break;
                                case "ERROR_OPERATION_NOT_ALLOWED":
                                    st_error = resources.getString(R.string.patient_signup_error_operations_not_allowed);
                                    break;
                                default:
                                    st_error = resources.getString(R.string.message_error);
                                    break;
                            }

                            showAlertErrorUser(st_error);
                        }
                    }
                });

        loading_imageView.playAnimation();
    }

    private void updatePatientUID() {

        String patient_uid = mAuth.getUid();

        databaseReference.child("Patient").child(password)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Patient", "No se ha conseguido");
                }
                else {

                    Map<String, Object> patientInfo = (Map<String, Object>) task.getResult().getValue();
                    updatePatient(patientInfo, patient_uid);
                }
            }
        });
    }

    private void updatePatient(Map<String, Object> patientInfo, String patient_uid) {
        
        databaseReference.child("Patient").child(password).removeValue();
        databaseReference.child("Patient").child(patient_uid).setValue(patientInfo);

        String professionalUID = patientInfo.get("ProfessionalUID").toString();
        databaseReference.child("Professional").child(professionalUID)
                .child("Patients").child(password).child("PatientUID").setValue(mAuth.getUid());
        databaseReference.child("Professional").child(professionalUID)
                .child("Patients").child(password).child("hasAccount").setValue(true);
    }

    private void showAlertErrorUser(String st_error) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(st_error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startPatientHomeActivity() {

        databaseReference.child(db_patientsNumericCode).child(password).removeValue();

        Intent patient_home_intent = new Intent(this, PatientHome.class);
        startActivity(patient_home_intent);
    }

}
