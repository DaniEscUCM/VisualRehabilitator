package com.macularehab.professional.account;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.macularehab.R;

public class RestorePassword extends AppCompatActivity {

    private TextInputEditText emailAddressTextInput;
    private String emailAddress;

    private final String TAG = "ProfessRestorePassword";

    private LottieAnimationView loading_imageView;
    private LottieAnimationView result_imageView;
    private TextView successTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_restore_password);

        //Email Address Text View
        emailAddressTextInput = findViewById(R.id.professional_logIn_restorePassword_EmailAddress_textInput);

        Button sendEmailButton = findViewById(R.id.professional_logIn_restorePassword_sendEmail_button);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readEmailAddress();
            }
        });

        ImageButton backButton = findViewById(R.id.professional_logIn_restorePassword_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loading_imageView = findViewById(R.id.professional_logIn_restorePassword_loadingEffect);
        loading_imageView.setVisibility(View.INVISIBLE);

        result_imageView = findViewById(R.id.professional_logIn_restorePassword_loadingEffect_result);
        result_imageView.setVisibility(View.INVISIBLE);

        successTextView = findViewById(R.id.professional_logIn_restorePassword_successTextView);
        successTextView.setVisibility(View.INVISIBLE);
    }

    private void readEmailAddress() {

        result_imageView.setVisibility(View.INVISIBLE);

        emailAddress = String.valueOf(emailAddressTextInput.getText());
        if (!emailAddress.equals("")) {
            showLoadingImage();
            sendEmail();
        }
        else {
            Resources resources = RestorePassword.this.getResources();
            showAlertErrorUser(resources.getString(R.string.emptyField));
        }
    }

    private void sendEmail() {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        stopLoadingImage();

                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            showReadyImage();
                            successTextView.setVisibility(View.VISIBLE);
                        }
                        else {

                            showErrorImage();
                            Toast.makeText(RestorePassword.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            FirebaseAuthException firebaseAuthException = (FirebaseAuthException) task.getException();
                            Resources resources = RestorePassword.this.getResources();
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
                                case "ERROR_USER_NOT_FOUND":
                                    st_error = resources.getString(R.string.professional_login_restorePassword_error_userNotFound);
                                    break;
                                case "ERROR_INVALID_EMAIL":
                                    st_error = resources.getString(R.string.professional_login_restorePassword_error_emailNotValid);
                                    break;
                                case "ERROR_USER_MISMATCH":
                                    st_error = resources.getString(R.string.professional_login_restorePassword_error_userMismatch);
                                    break;
                                case "USER_DISABLED":
                                    st_error = resources.getString(R.string.professional_login_restorePassword_error_userDisabled);
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

    private void showLoadingImage() {

        loading_imageView.setAnimation(R.raw.loading_rainbow_small);
        loading_imageView.setVisibility(View.VISIBLE);
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

    private void stopLoadingImage() {

        loading_imageView.cancelAnimation();
        loading_imageView.setVisibility(View.INVISIBLE);
        loading_imageView.setImageResource(R.drawable.ic_launcher_foreground);
    }

    private void showReadyImage() {

        result_imageView.setVisibility(View.VISIBLE);
        result_imageView.setAnimation(R.raw.ready);
        result_imageView.playAnimation();
    }

    private void showErrorImage() {

        result_imageView.setVisibility(View.VISIBLE);
        result_imageView.setAnimation(R.raw.error);
        result_imageView.playAnimation();
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
}
