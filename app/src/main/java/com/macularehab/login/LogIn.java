package com.macularehab.login;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.PatientHome;
import com.macularehab.PatientLogin;
import com.macularehab.model.Patient;

public class LogIn {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String email_username;
    private String password;

    private PatientLogin patientLogin;

    private static final String TAG = "EmailPassword";

    private FirebaseUser user;
    private boolean user_signedIn_successfully;

    private String username;

    private static final String GENERIC_EMAIL = "@maculaRehabTFG.com";

    public LogIn(PatientLogin patientLogin) {

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();

        this.patientLogin = patientLogin;
    }

    public boolean user_is_signed_in(){

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            user = currentUser;
            Log.w("LogIn Already", currentUser.getUid());
            return true;
        }
        return false;
    }

    public void readEmail(String email_username, String password, PatientLogin patientLogin) {

        this.patientLogin = patientLogin;

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

        //createAccount(email_username, password);
        signIn(email_username, password);
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            LogIn.this.user = user;
                            LogIn.this.user_signedIn_successfully = true;
                            LogIn.this.patientLogin.user_loggedIn_successfully();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            LogIn.this.user_signedIn_successfully = false;
                            LogIn.this.patientLogin.user_loggin_failed();
                        }
                    }
                });
        // [END sign_in_with_email]
    }
}
