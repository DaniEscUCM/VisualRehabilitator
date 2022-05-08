package com.macularehab.patient.signUp;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;
import com.macularehab.patient.PatientHome;

import java.util.Map;

public class PatientSignUpUsername extends AppCompatActivity {

    private EditText input_username;
    private Button button_continue_signup;
    private String username;
    private String password;
    private LottieAnimationView loading_imageView;
    private View layout_loading;

    private final String db_patientsNumericCode = "PatientsNumericCodes";
    private static final String GENERIC_EMAIL = "@maculaRehabTFG.com";
    private final String SHARED_PREF_FILE = "com.macularehab.sharedprefs.user_is_logged";
    private final String SHARED_PREF_PATIENT_USER_LOGGED_KEY = "patient_user_logged";

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

        //EditText of username
        input_username = findViewById(R.id.editText_patient_signup_username);

        //Loading Image
        ConstraintLayout constraintLayout = findViewById(R.id.patient_signup_constrainLayout_lottieImage);
        layout_loading = getLayoutInflater().inflate(R.layout.layout_loading, constraintLayout, false);
        constraintLayout.addView(layout_loading);

        loading_imageView = findViewById(R.id.general_loading_image);
        loading_imageView.setVisibility(View.INVISIBLE);

        //Buttons
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

        setUiListener();
    }

    private void setUiListener() {

        View decorView = getWindow().getDecorView();

        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 2000ms
                                    hideNavigationAndStatusBar();
                                }
                            }, 2000);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideNavigationAndStatusBar();
    }

    private void buttonClicked() {

        username = input_username.getText().toString().trim();
        username = username.replaceAll(" ", "");
        validateUsername();
    }

    private void validateUsername() {

        boolean all_correct = true;
        Resources resources = this.getResources();

        if (username.equals("")) {
            input_username.setError(resources.getString(R.string.message_fill_fields));
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

        Resources resources = PatientSignUpUsername.this.getResources();

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        loading_imageView.cancelAnimation();
                        loading_imageView.setVisibility(View.INVISIBLE);

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.w("EMAIL", "createUserWithEmail:success");

                            Toast.makeText(PatientSignUpUsername.this, resources.getString(R.string.user_created_text), Toast.LENGTH_LONG).show();
                            updatePatientUID();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("EMAIL", "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(PatientSignUpUsername.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();

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

        databaseReference.child("Patient").child(patient_uid).setValue(patientInfo);

        String professionalUID = patientInfo.get("ProfessionalUID").toString();
        databaseReference.child("Professional").child(professionalUID)
                .child("Patients").child(password).child("patient_uid").setValue(mAuth.getUid());
        databaseReference.child("Professional").child(professionalUID)
                .child("Patients").child(password).child("hasAccount").setValue(true);
        databaseReference.child("Professional").child(professionalUID)
                .child("Patients").child(password).child("patient_username").setValue(username);

        databaseReference.child("Patient").child(password).removeValue();

        startPatientHomeActivity();
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

        saveLoggedUser();

        databaseReference.child(db_patientsNumericCode).child(password).removeValue();

        Intent patient_home_intent = new Intent(this, PatientHome.class);
        startActivity(patient_home_intent);
    }

    private void saveLoggedUser() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SHARED_PREF_PATIENT_USER_LOGGED_KEY, true);

        editor.apply();
    }

    private void hideNavigationAndStatusBar() {

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        decorView.setSystemUiVisibility(uiOptions);
    }
}
