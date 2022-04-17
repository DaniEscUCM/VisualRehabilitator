package com.macularehab.professional.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.macularehab.R;
import com.macularehab.model.Professional;
import com.macularehab.professional.ProfessionalHome;

public class ProfessionalSignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EmailPassword";
    private static final String GENERIC_EMAIL = "@maculaRehabTFG.com";

    public String currentUserID;

    EditText nameP, mailP, paswP, repPasw;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private String name;
    private String email;
    private String password;
    private String repeatPassword;

    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_singing);

        //initialize database
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        resources = ProfessionalSignUpActivity.this.getResources();

        nameP = findViewById(R.id.editTextProfesionalName);
        mailP = findViewById(R.id.editTextProfesionalMail);
        paswP = findViewById(R.id.editTextProfesionalPasword);
        repPasw = findViewById(R.id.editTextProfesionalRepeatPasword);

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_exerc_prof);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(v);
            }
        });

        Button singinButton = (Button) findViewById(R.id.button_singIn);
        singinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singInProfessional(v);
            }
        });

    }

    public void close(View view){
        finish();
    }

    public void singInProfessional (View view) {
        //validate
        name = nameP.getText().toString().trim();
        email = mailP.getText().toString().trim();
        password = paswP.getText().toString().trim();
        repeatPassword = repPasw.getText().toString().trim();

        if(name.equals("") || email.equals("") || password.equals("") || (password.length() < 6) || (!password.equals(repeatPassword))) {
            validate();

        }else{

            int email_length = email.length();
            boolean is_email = false;
            for (int i = 0; i < email_length && !is_email; i++) {
                if (email.charAt(i) == '@') is_email = true;
            }
            //if (!is_email) mail += GENERIC_EMAIL;
            if (!is_email) {
                showAlertBadFormatEmail();
            }
            else {
                createAccount(name, email, password);
            }
        }
    }

    private void createAccount(String name,String mail, String password) {

        firebaseAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                            setUserName(currentUser);
                            ProfessionalSignUpActivity.this.currentUserID = currentUser.getUid();
                            addDB(name,mail,password);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ProfessionalSignUpActivity.this,
                                    task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();

                            FirebaseAuthException firebaseAuthException = (FirebaseAuthException) task.getException();
                            Resources resources = ProfessionalSignUpActivity.this.getResources();
                            String st_error;

                            switch (firebaseAuthException.getErrorCode()) {

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    st_error = resources.getString(R.string.patient_signup_error_user_already_in_use);
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    st_error = resources.getString(R.string.professional_home_changePassword_error_passwordWeak);
                                    break;
                                case "ERROR_WRONG_PASSWORD":
                                    st_error = resources.getString(R.string.professional_signup_wrongPassword);
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

    private void setUserName(FirebaseUser firebaseUser) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        Resources resources = ProfessionalSignUpActivity.this.getResources();
        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Toast.makeText(ProfessionalSignUpActivity.this,
                                    resources.getString(R.string.professional_login_logInSuccessfully) + " " + name,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void showAlertAuthenticationFailed(String exception) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(exception)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addDB(String name, String email, String password){

        this.name = name;
        this.email = email;
        this.password = password;

        getNumberOfProfessionals();
    }

    private void getNumberOfProfessionals() {

        databaseReference.child("NumberOfProfessionals")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    String value = String.valueOf(task.getResult().getValue());
                    createNewProfessional(Integer.parseInt(value));
                }
            }
        });
    }

    private void createNewProfessional(int professionalNumericCode) {

        Professional p = new Professional();
        p.setName(name);
        p.setUid(currentUserID);
        p.setProfessionalNumericCode(professionalNumericCode);
        p.setNumberOfPatients(100);

        databaseReference.child("NumberOfProfessionals").setValue(professionalNumericCode + 1);
        databaseReference.child("Professional").child(currentUserID).setValue(p);

        Toast.makeText(ProfessionalSignUpActivity.this, "User created", Toast.LENGTH_LONG).show();
        clean();
        goToMain();
    }

    public void validate() {

        String name = nameP.getText().toString();
        String mail = mailP.getText().toString();
        String pasw = paswP.getText().toString();
        String rep_pasw = repPasw.getText().toString();

        String required = resources.getString(R.string.error_required_field);

        if(name.equals("")){
            nameP.setError(required);
        }
        if(mail.equals("")){
            mailP.setError(required);
        }
        if(pasw.equals("")){
            paswP.setError(required);
        }
        if (pasw.length() < 6) {
            paswP.setError(resources.getString(R.string.error_weak_password));
        }
        if (!pasw.equals(rep_pasw)) {

            String pass_not_match = resources.getString(R.string.error_passwords_not_match);

            paswP.setError(pass_not_match);
            repPasw.setError(pass_not_match);
            /*paswP.setText("");
            repPasw.setText("");*/
        }
    }

    public void clean(){
        nameP.setText("");
        mailP.setText("");
        paswP.setText("");
        repPasw.setText("");
    }

    public void goToMain(){

        Intent i = new Intent( this, ProfessionalHome.class);
        startActivity(i);
    }

    private void showAlertBadFormatEmail() {

        Resources resources = this.getResources();
        String st_passwordDoestExist = resources.getString(R.string.professional_login_restorePassword_error_emailNotValid);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(st_passwordDoestExist)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}