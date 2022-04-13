package com.macularehab.login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Deprecated
public class SignUp {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String name;
    private String email_username;
    private String password;

    //private PatientSignUp patientSignUp;

    private static final String TAG = "EmailPassword";

    private FirebaseUser user;
    private boolean user_created_successfully;

    private static final String GENERIC_EMAIL = "@maculaRehabTFG.com";

    public SignUp() {

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
    }

    public boolean user_is_signed_in(){

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            Log.w("actividad", currentUser.getUid());
            return true;
        }
        return false;
    }

    public void readEmail( String email_username, String password/*, PatientSignUp patientSignUp*/) {

        /*this.patientSignUp = patientSignUp;*/

        int email_length = email_username.length();
        boolean is_email = false;

        for (int i = 0; i < email_length && !is_email; i++) {

            if (email_username.charAt(i) == '@') {
                is_email = true;
            }
        }

        if (!is_email) {
            email_username += GENERIC_EMAIL;
        }

        this.email_username = email_username;
        this.password = password;

        this.user_created_successfully = true;

        createAccount(email_username, password);
        //the session will be established as a patient


    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.w(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            SignUp.this.user = user;
                            SignUp.this.user_created_successfully = true;
                           // createUserDB(user.getUid());
                            //SignUp.this.patientSignUp.patient_signed_successfully();
                            //updateUI(user);
                            //SignUp.this.patientSignUp.addDB();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            SignUp.this.user_created_successfully = false;
                            //SignUp.this.patientSignUp.patient_signingUp_failed();

                        }
                    }
                });
        // [END create_user_with_email]
    }
    public String getUID(){

        FirebaseUser user = mAuth.getCurrentUser();
        return user.getUid().toString();
    }

    private void updateUI(FirebaseUser user) { };
}
