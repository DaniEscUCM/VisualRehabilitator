package com.macularehab.professional.account;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;

public class ChangeEmail extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private final String TAG = "ProfessChangeEmail";

    private TextInputEditText emailAddressTextInput;
    private String emailAddress;

    private LottieAnimationView loading_imageView;
    private LottieAnimationView result_imageView;

    private Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_profile_change_email);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        resources = ChangeEmail.this.getResources();

        mAuth.updateCurrentUser(mAuth.getCurrentUser());

        emailAddressTextInput = findViewById(R.id.professional_profile_changeEmail_textInputLayout_textInput);

        loading_imageView = findViewById(R.id.professional_logIn_restorePassword_loadingEffect);
        result_imageView = findViewById(R.id.professional_logIn_restorePassword_loadingEffect_result);

        Button changeEmailButton = findViewById(R.id.professional_profile_changeEmail_button);
        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasInternetConnection()) {
                    readEmailAddress();
                }
                else {
                    showAlertErrorUser(resources.getString(R.string.noInternetConnection));
                }
            }
        });

        ImageButton backButton = findViewById(R.id.professional_profile_changeEmail_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setImagesInvisible();

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
    protected void onResume() {
        super.onResume();
        setImagesInvisible();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setImagesInvisible();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setImagesInvisible();
        hideNavigationAndStatusBar();
    }

    private void setImagesInvisible() {

        loading_imageView.setVisibility(View.INVISIBLE);
        result_imageView.setVisibility(View.INVISIBLE);
    }

    private void readEmailAddress() {

        result_imageView.setVisibility(View.INVISIBLE);

        emailAddress = String.valueOf(emailAddressTextInput.getText());
        if (!emailAddress.equals("")) {
            showLoadingImage();
            changeEmail();
        }
        else {
            Resources resources = ChangeEmail.this.getResources();
            showAlertErrorUser(resources.getString(R.string.emptyField));
        }
    }

    private void changeEmail() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        firebaseUser.updateEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                stopLoadingImage();

                if(task.isSuccessful()) {
                    showReadyImage();
                    Toast.makeText(ChangeEmail.this,
                            resources.getString(R.string.professional_profile_changeEmail_successMessage),
                            Toast.LENGTH_LONG).show();
                    changeDataBase();
                }
                else {

                    showErrorImage();

                    Toast.makeText(ChangeEmail.this,
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

    private void changeDataBase() {

        databaseReference.child("Professional").child(mAuth.getCurrentUser().getUid())
                .child("contact_info").child("email_address").setValue(emailAddress);
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
    }

    private void showReadyImage() {

        result_imageView.setAnimation(R.raw.ready);
        result_imageView.setVisibility(View.VISIBLE);
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

    private boolean hasInternetConnection() {

        boolean isConnected = false;

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        return isConnected;
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
