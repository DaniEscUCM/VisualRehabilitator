package com.macularehab.professional.account;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;

public class ChangePassword extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private String email;

    private TextInputLayout oldPasswordLayout;
    private TextInputLayout newPasswordLayout;
    private TextInputLayout repeatNewPasswordLayout;
    private TextInputEditText oldPasswordInput;
    private TextInputEditText newPasswordInput;
    private TextInputEditText repeatNewPasswordInput;

    private String oldPassword;
    private String newPassword;
    private String repeatNewPassword;

    private Resources resources;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_change_password);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        mAuth.updateCurrentUser(mAuth.getCurrentUser());

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        email = firebaseUser.getEmail();

        resources = ChangePassword.this.getResources();

        oldPasswordLayout = findViewById(R.id.professional_home_change_password_oldPassword_textInputLayout);
        newPasswordLayout = findViewById(R.id.professional_home_change_password_newPassword_textInputLayout);
        repeatNewPasswordLayout = findViewById(R.id.professional_home_change_password_repeatNewPassword_textInputLayout);
        oldPasswordInput = findViewById(R.id.professional_home_change_password_oldPassword_textInput);
        newPasswordInput = findViewById(R.id.professional_home_change_password_newPassword_textInput);
        repeatNewPasswordInput = findViewById(R.id.professional_home_change_password_repeatNewPassword_textInput);

        Button changePassButton = findViewById(R.id.professional_home_change_password_button);
        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readInputs();
            }
        });

        oldPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        oldPasswordLayout.setErrorIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPasswordLayout.setError(null);
            }
        });

        newPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newPasswordLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPasswordLayout.setErrorIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPasswordLayout.setError(null);
            }
        });

        repeatNewPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                repeatNewPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        repeatNewPasswordLayout.setErrorIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeatNewPasswordLayout.setError(null);
            }
        });

        ImageButton backButton = findViewById(R.id.professional_home_changePassword_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lottieAnimationView = findViewById(R.id.professional_home_change_password_imageSuccess);
        setImageInvisible();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setImageInvisible();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setImageInvisible();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImageInvisible();
    }

    private void setImageInvisible() {

        lottieAnimationView.setVisibility(View.INVISIBLE);
    }

    private void readInputs() {

        oldPassword = String.valueOf(oldPasswordInput.getText());
        newPassword = String.valueOf(newPasswordInput.getText());
        repeatNewPassword = String.valueOf(repeatNewPasswordInput.getText());

        validateInputs();
    }

    private void validateInputs() {

        boolean all_ok = true;
        boolean error_empty = false;
        boolean error_length = false;
        boolean error_not_match = false;

        if (oldPassword.equals("")) {
            oldPasswordLayout.setError(resources.getString(R.string.professional_home_changePassword_error_fillFields));
            all_ok = false;
            error_empty = true;
        }
        if (newPassword.equals("")) {
            newPasswordLayout.setError(resources.getString(R.string.professional_home_changePassword_error_fillFields));
            all_ok = false;
            error_empty = true;
        }
        if (repeatNewPassword.equals("")) {
            repeatNewPasswordLayout.setError(resources.getString(R.string.professional_home_changePassword_error_fillFields));
            all_ok = false;
            error_empty = true;
        }

        if (newPassword.length() < 6) {
            newPasswordLayout.setError(resources.getString(R.string.professional_home_changePassword_error_passwordWeak));
            all_ok = false;
            error_length = true;
        }
        if (repeatNewPassword.length() < 6) {
            newPasswordLayout.setError(resources.getString(R.string.professional_home_changePassword_error_passwordWeak));
            all_ok = false;
            error_length = true;
        }

        if (!newPassword.equals(repeatNewPassword)) {
            repeatNewPasswordLayout.setError(resources.getString(R.string.professional_home_changePassword_error_passwordsDoesNotMatch));
            all_ok = false;
            error_not_match = true;
        }

        if (!all_ok) {

            String st_error = "";
            if (error_empty) {
                st_error = resources.getString(R.string.professional_home_changePassword_error_fillFields);
            }
            else if (error_length) {
                st_error = resources.getString(R.string.professional_home_changePassword_error_passwordWeak);
            }
            else if (error_not_match) {
                st_error = resources.getString(R.string.professional_home_changePassword_error_passwordsDoesNotMatch);
            }
            else {
                st_error = resources.getString(R.string.message_error);
            }

            showAlertErrorUser(st_error);
        }
        else {
            checkActualPasswordIsCorrect();
        }
    }

    private void checkActualPasswordIsCorrect() {

        mAuth.signInWithEmailAndPassword(email, oldPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            changePassword();
                        }
                        else {

                            Toast.makeText(ChangePassword.this,
                                    task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            FirebaseAuthException firebaseAuthException = (FirebaseAuthException) task.getException();
                            String st_error;

                            switch (firebaseAuthException.getErrorCode()) {

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    st_error = resources.getString(R.string.patient_signup_error_user_already_in_use);
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    st_error = resources.getString(R.string.professional_home_changePassword_error_passwordWeak);
                                    break;
                                case "ERROR_WRONG_PASSWORD":
                                    st_error = resources.getString(R.string.professional_home_changePassword_error_wrongPassword);
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
    }

    private void changePassword() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        firebaseUser.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showSuccessImage();
                            Toast.makeText(ChangePassword.this, resources.getString(R.string.professional_home_changePassword_successMessage), Toast.LENGTH_LONG).show();
                        }
                        else {

                            Toast.makeText(ChangePassword.this,
                                    task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            FirebaseAuthException firebaseAuthException = (FirebaseAuthException) task.getException();
                            String st_error;

                            switch (firebaseAuthException.getErrorCode()) {

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    st_error = resources.getString(R.string.patient_signup_error_user_already_in_use);
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    st_error = resources.getString(R.string.professional_home_changePassword_error_passwordWeak);
                                    break;
                                case "ERROR_WRONG_PASSWORD":
                                    st_error = resources.getString(R.string.professional_home_changePassword_error_wrongPassword);
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

    private void showSuccessImage() {

        lottieAnimationView.setAnimation(R.raw.ready);
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }
            @Override
            public void onAnimationEnd(Animator animation) {
                lottieAnimationView.setVisibility(View.INVISIBLE);
                ChangePassword.this.finish();
            }
            @Override
            public void onAnimationCancel(Animator animation) { }
            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
    }
}
