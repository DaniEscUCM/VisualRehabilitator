package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.macularehab.model.Professional;

import java.util.UUID;

public class ProfessionalSingingActivity extends AppCompatActivity {

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

        if(name.equals("")||uname.equals("")||pasw.equals("")) {
            validate();

        }else{
            Professional p = new Professional();
            p.setName(name);
            p.setUsername(uname);
            p.setPassword(pasw);
            p.setUid(UUID.randomUUID().toString());
            databaseReference.child("Professional").child(p.getUid()).setValue(p);
            Toast.makeText(this, "User created", Toast.LENGTH_LONG).show();
            clean();
            goToMain();
        }

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
    }

    public void clean(){
        nameP.setText("");
        unameP.setText("");
        paswP.setText("");
    }

    public void goToMain(){
        Intent i = new Intent( this, MainActivity.class);
        startActivity(i);
    }
}