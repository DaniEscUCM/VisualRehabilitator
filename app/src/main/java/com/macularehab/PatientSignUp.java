package com.macularehab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.login.SignUp;
import com.macularehab.model.Patient;

public class PatientSignUp extends AppCompatActivity {

    private SignUp signUp;

    private TextView name_text;
    private TextView email_text;
    private TextView firstname_text;
    private TextView lastname_text;
    private TextView focuspoint_text;
    private TextView leftstain_text;
    private TextView rightstain_text;
    private TextView stain_text;
    private TextView age_text;
    private TextView gender_text;
    private TextView password_text;

    private String name;
    private String firstname;
    private String lastname;
    private String focuspoint;
    private String leftstain;
    private String rightstain;
    private String stain;
    private String age;
    private String gender;
    private String email_username;
    private String password;

    private  String professional_uid;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_signup);

        this.signUp = new SignUp();
        this.professional_uid = this.signUp.getUID();

        this.name_text = findViewById(R.id.patient_name_signup);
        this.email_text = findViewById(R.id.patient_email_signup);
        this.password_text = findViewById(R.id.patient_password_signup);
        this.firstname_text = findViewById(R.id.patient_firstname_signup);
        this.lastname_text = findViewById(R.id.patient_lastname_signup);
        this.gender_text = findViewById(R.id.patient_gender_signup);
        this.age_text = findViewById(R.id.patient_age_signup);
        this.focuspoint_text = findViewById(R.id.patient_focuspoint_signup);
        this.leftstain_text = findViewById(R.id.patient_leftstain_signup);
        this.rightstain_text = findViewById(R.id.patient_rightstain_signup);
        this.stain_text = findViewById(R.id.patient_stain_signup);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();

        Log.w("actividad signUp", " creada");

        Button signUpButton = findViewById(R.id.patient_signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.w("boton signUp", " presionado");
                readEmail();
            }
        });
    }

   /* @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        Log.w("actividad signUp", " empezada");
        boolean signedIn = this.signUp.user_is_signed_in();
        if(signedIn){
            Log.w("actividad", " paciente conectado");
            //TODO hay que hacer la interfaz en caso de que el usuario este ya iniciado
            reload();
        }
    }*/

    private void readEmail() {

        this.name = this.name_text.getText().toString();
        this.email_username = this.email_text.getText().toString();
        this.password = this.password_text.getText().toString();
        this.firstname = this.firstname_text.getText().toString();
        this.lastname = this.lastname_text.getText().toString();
        this.gender = this.gender_text.getText().toString();
        this.age = this.age_text.getText().toString();
        this.focuspoint = this.focuspoint_text.getText().toString();
        this.leftstain = this.leftstain_text.getText().toString();
        this.rightstain = this.rightstain_text.getText().toString();
        this.stain= this.stain_text.getText().toString();

        Log.w("EMAIL: ", email_username);
        Log.w("PASSWORD: ", password);

        validate_user_input();//to do

        this.signUp.readEmail( email_username, password, this);
       // addDB();
    }
    public void addDB(){
        Patient p = new Patient();

        String uid = this.signUp.getUID();
        p.setUid(uid);
        p.setName(name);
        p.setFirstname(firstname);
        p.setLastname(lastname);
        p.setGender(gender);
        p.setAge(age);
        p.setLeftstain(leftstain);
        p.setRightstain(rightstain);
        p.setStain(stain);
        p.setEmail_username(email_username);
        p.setFocuspoint(focuspoint);
        p.setProfessional(professional_uid);
        addPatientProfessional(uid,professional_uid);

        databaseReference.child("Patient").child(uid).setValue(p);
    }
    public void patient_signed_successfully() {

        Toast.makeText(PatientSignUp.this, "User created", Toast.LENGTH_LONG).show();
        clean();
        reload();
    }

    public void patient_signingUp_failed() {

        Toast.makeText(PatientSignUp.this, "Authentication failed.",
                Toast.LENGTH_LONG).show();
        showAlertFailToSignUp();
    }

    public void validate_user_input(){

        if(this.name.equals("")){
            this.name_text.setError("required");
            showAlertEnterEmailAndPassword();
        }
        if(this.email_username.equals("")){
            this.email_text.setError("required");
            showAlertEnterEmailAndPassword();
        }
        if(this.password.equals("")){
            this.password_text.setError("required");
            showAlertEnterEmailAndPassword();
        }
        if (this.password.length() < 6) {
            this.password_text.setError("Password must be at least 6 characters");
        }
    }

    public void showAlertEnterEmailAndPassword() {

        //FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Name and/or Email and/or Password field is empty")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showAlertFailToSignUp() {

        //FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Failed To SignUp")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clean(){
        this.name_text.setText("");
        this.email_text.setText("");
        this.password_text.setText("");
    }

    //TODO
    private void reload() {

        Intent intent = new Intent( this, PatientHome.class);
        startActivity(intent);
        Toast.makeText(PatientSignUp.this, "User changed to new one",
                Toast.LENGTH_LONG).show();
    }
    //TODO
    public void addPatientProfessional(String patient_uid,String professional_uid){
        databaseReference.child("Professional").child(professional_uid).child("Patients").child(patient_uid).setValue(patient_uid);
    }
}
