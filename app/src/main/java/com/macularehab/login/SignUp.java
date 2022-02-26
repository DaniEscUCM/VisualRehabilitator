package com.macularehab.login;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.PatientSignUp;
import com.macularehab.ProfessionalSingingActivity;
import com.macularehab.R;
import com.macularehab.model.Patient;
import com.macularehab.model.Professional;

import java.util.concurrent.Executor;

public class SignUp {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String name;
    private String email_username;
    private String password;

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

    public boolean readEmail(String name, String email_username, String password) {

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

        this.name = name;
        this.email_username = email_username;
        this.password = password;

        this.user_created_successfully = true;

        createAccount(email_username, password);

        return this.user_created_successfully;
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
                            createUserDB(user.getUid());
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            SignUp.this.user_created_successfully = false;

                        }
                    }
                });
        // [END create_user_with_email]
    }

    public void createUserDB(String currentUserID) {

        Patient patient = new Patient(this.name, this.email_username, currentUserID);

        databaseReference.child("Patient").child(currentUserID).setValue(patient);
    }
}
