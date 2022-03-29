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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.macularehab.login.LogIn;
import com.macularehab.model.Professional;
import com.macularehab.professional.ProfessionalHome;

public class ProfessionalLoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EmailPassword";
    private static final String GENERIC_EMAIL = "@maculaRehabTFG.com";

    EditText mailP, paswP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Professional prof;
    final boolean[] encontrado = {false,false}; //[0]name in db [1]pasw correct
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

        Button loginButton =(Button) findViewById(R.id.button_professional_login2);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
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

    /*public void goToMain(){
        Intent i = new Intent( this, ProfessionalPageActivity.class);
        //i.putExtra("username",unameP.getText().toString()); //we pass the username to activity : Professional Page
        startActivity(i);
    }*/

    public void login(View view){
        String mail = mailP.getText().toString();
        String pasword = paswP.getText().toString();

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
                            goToProfessionalHome();
                            //updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "loginUserWithEmail:failure", task.getException());
                            Toast.makeText(ProfessionalLoginActivity.this, "Login failed.",
                                    Toast.LENGTH_LONG).show();
                            //updateUI(null);

                        }
                    }
                });
    }

    public void goToProfessionalHome() {
        Intent intent = new Intent(this, ProfessionalHome.class);
        startActivity(intent);
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

}