package com.macularehab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.macularehab.model.Professional;

public class ProfessionalSingingActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EmailPassword";

    public String currentUserID;

    EditText nameP, unameP, paswP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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

        nameP = findViewById(R.id.editTextProfesionalName);
        unameP = findViewById(R.id.editTextProfesionalUname);
        paswP = findViewById(R.id.editTextProfesionalPasword);

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

    public void singInProfessional (View view){
        //validate
        String name = nameP.getText().toString();
        String uname = unameP.getText().toString();
        String pasw = paswP.getText().toString(); //ver como guardar el hash

        if(name.equals("")||uname.equals("")||pasw.equals("") || (pasw.length() < 6)) {
            validate();

        }else{

            createAccount(uname, pasw);

            Log.d(TAG, "hola");

            /*Professional p = new Professional();
            p.setName(name);
            p.setUsername(uname);
            p.setPassword(pasw);
            p.setUid(currentUserID);
            databaseReference.child("Professional").child(currentUserID).setValue(p);
            Toast.makeText(this, "User created", Toast.LENGTH_LONG).show();
            clean();
            goToMain();*/
        }

    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            //updateUI(user);
                            ProfessionalSingingActivity.this.currentUserID = currentUser.getUid();

                            Professional p = new Professional();
                            p.setName(email);
                            p.setUsername(email);
                            p.setPassword(password);
                            p.setUid(currentUserID);
                            databaseReference.child("Professional").child(currentUserID).setValue(p);

                            Toast.makeText(ProfessionalSingingActivity.this, "User created", Toast.LENGTH_LONG).show();
                            clean();
                            goToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ProfessionalSingingActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            //updateUI(null);

                            //showAlertFailToSignUp();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    public void validate(){
        String name = nameP.getText().toString();
        String uname = unameP.getText().toString();
        String pasw = paswP.getText().toString(); //ver como guardar el hash
        if(name.equals("")){
            nameP.setError("required");
        }
        if(uname.equals("")){
            unameP.setError("required");
        }
        if(pasw.equals("")){
            paswP.setError("required");
        }
        if (pasw.length() < 6) {
            paswP.setError("Password must be at least 6 characters");
        }
    }

    public void clean(){
        nameP.setText("");
        unameP.setText("");
        paswP.setText("");
    }

    public void goToMain(){
        Intent i = new Intent( this, ProfessionalPageActivity.class);
        i.putExtra("username",unameP.getText().toString()); //we pass the username to activity : Professional Page
        startActivity(i);
    }
}