package com.macularehab.professional.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.macularehab.R;
import com.macularehab.login.LogIn;
import com.macularehab.model.Professional;
import com.macularehab.patient.signUp.PatientSignUpUsername;
import com.macularehab.professional.ProfessionalHome;

import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ProfessionalLoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EmailPassword";
    private static final String GENERIC_EMAIL = "@maculaRehabTFG.com";

    EditText mailP, paswP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Professional prof;
    final boolean[] encontrado = {false,false}; //[0]name in db [1]pasw correct

    private LottieAnimationView loading_imageView;
    private ProgressDialog progressDialog;
    private View layout_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_login);

        //initialize database
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();

        mailP = findViewById(R.id.editTextTextProfessionalEmail);
        paswP = findViewById(R.id.editTextTextProfessionalPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        ImageButton buttonBack = (ImageButton) findViewById(R.id.imageButton_back_prof_login);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(v);
            }
        });

        Button loginButton = (Button) findViewById(R.id.button_professional_login2);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        Button forgotPasswordButton = findViewById(R.id.professional_logIn_forgotPassword_button);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRestorePasswordActivity();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) { //API 19

            ConstraintLayout constraintLayout = findViewById(R.id.professional_logIn_constrainLayout);
            layout_loading = getLayoutInflater().inflate(R.layout.layout_loading, constraintLayout, false);
            constraintLayout.addView(layout_loading);

            loading_imageView = findViewById(R.id.general_loading_image);
        }
        else {

            progressDialog = new ProgressDialog(this);
        }

        showLoadingImage();
        //setImagesInvisible();
    }

    @Override
    protected void onStart() {
        super.onStart();

        LogIn logIn = new LogIn();

        boolean is_login = logIn.professional_is_signed_in(this);

        /*if (is_login) {
            goToMain();
        }*/
    }

    public void close(View view){
        finish();
    }

    public void clean(){
        mailP.setText("");
        paswP.setText("");
    }

    public void login(View view) {

        String mail = mailP.getText().toString().trim();
        String pasword = paswP.getText().toString().trim();

        if(mail.equals("")||pasword.equals("")){
            validate();
        }else{

            int email_length = mail.length();
            boolean is_email = false;
            for (int i = 0; i < email_length && !is_email; i++) if (mail.charAt(i) == '@') is_email = true;
            if (!is_email) mail += GENERIC_EMAIL;
            loginAuth(mail,pasword);

        }

    }
    private void loginAuth(String mail, String password) {

        firebaseAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "loginUserWithEmail:success");
                            //goToMain();
                            //goToProfessionalHome();
                            //updateUI(user);
                            checkIfIsProfessional();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "loginUserWithEmail:failure", task.getException());
                            Toast.makeText(ProfessionalLoginActivity.this,
                                    task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();

                            FirebaseAuthException firebaseAuthException = (FirebaseAuthException) task.getException();
                            Resources resources = ProfessionalLoginActivity.this.getResources();
                            String st_error;

                            switch (firebaseAuthException.getErrorCode()) {

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    st_error = resources.getString(R.string.patient_signup_error_user_already_in_use);
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    st_error = resources.getString(R.string.professional_home_changePassword_error_passwordWeak);
                                    break;
                                case "ERROR_WRONG_PASSWORD":
                                    st_error = resources.getString(R.string.professional_login_wrongPassword);
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

    private void checkIfIsProfessional() {

        databaseReference.child("Professional").child(firebaseAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    isNotAProfessional();
                }
                else {

                    String value = String.valueOf(task.getResult().getValue());

                    if (value.equals("null")) {
                        isNotAProfessional();
                    }
                    else {
                        goToProfessionalHome();
                    }
                }
            }
        });
    }

    private void isNotAProfessional() {

        firebaseAuth.signOut();
        showAlertYouAreNotAProfessional();
    }

    public void goToProfessionalHome() {

        if (firebaseAuth.getCurrentUser() != null) {

            String name = firebaseAuth.getCurrentUser().getDisplayName();

            if (name != null) {
                Log.e("ProfName", firebaseAuth.getCurrentUser().getDisplayName());

                Resources resources = ProfessionalLoginActivity.this.getResources();
                Toast.makeText(this,
                        resources.getString(R.string.professional_login_logInSuccessfully) + " " + firebaseAuth.getCurrentUser().getDisplayName(),
                        Toast.LENGTH_LONG).show();
            }
        }

        Intent intent = new Intent(this, ProfessionalHome.class);
        startActivity(intent);
    }

    private void showAlertYouAreNotAProfessional() {

        Resources resources = this.getResources();
        String st_passwordDoestExist = resources.getString(R.string.professional_login_notAProfessionalAccount);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(st_passwordDoestExist)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void correct(){ //to make provide correct error message
        if(!encontrado[0]){
            Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
            mailP.setError("not found");
            mailP.setText("");
            paswP.setText("");
        }else if(!encontrado[1]){
            Toast.makeText(this, "Password incorrect", Toast.LENGTH_LONG).show();
            paswP.setError("incorrect");
            paswP.setText("");
        }else{
            String toastmes = "Welcome "+ prof.getName();
            Toast.makeText(this, toastmes, Toast.LENGTH_LONG).show();
            //goToMain();
            goToProfessionalHome();
        }
    }

    public void validate(){ //to make sure everything is filled
        String mail = mailP.getText().toString();
        String pasw = paswP.getText().toString();
        if(mail.equals("")){
            mailP.setError("required");
        }
        if(pasw.equals("")){
            paswP.setError("required");
        }
    }

    private void goToRestorePasswordActivity() {

        Intent forgotPasswordIntent = new Intent(this, RestorePassword.class);
        startActivity(forgotPasswordIntent);
    }

    private void setImagesInvisible() {

        loading_imageView.setVisibility(View.INVISIBLE);
    }

    private void showLoadingImage() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) { //API 19
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
        else {

            //TODO for spanish
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
        }
    }

    public void stopLoadingImage() {

        if (Build.VERSION.SDK_INT > 21) {
            loading_imageView.cancelAnimation();
            loading_imageView.setVisibility(View.INVISIBLE);
            loading_imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
        else {
            progressDialog.dismiss();
        }
    }
}