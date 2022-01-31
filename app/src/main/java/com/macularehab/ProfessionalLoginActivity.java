package com.macularehab;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.macularehab.model.Professional;

public class ProfessionalLoginActivity extends AppCompatActivity {

    EditText unameP, paswP;
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

        unameP = findViewById(R.id.editTextTextProfessionalName);
        paswP = findViewById(R.id.editTextTextProfessionalPassword);

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

    public void close(View view){
        finish();
    }

    public void clean(){
        unameP.setText("");
        paswP.setText("");
    }

    public void goToMain(){
        Intent i = new Intent( this, MainActivity.class);
        startActivity(i);
    }

    public void login(View view){
        String uname = unameP.getText().toString();
        String pasw = paswP.getText().toString();

        if(uname.equals("")||pasw.equals("")){
            validate();
        }else{

            databaseReference.child("Professional").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        encontrado[0]=false;
                        encontrado[1]=false;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Professional p = snapshot.getValue(Professional.class);
                            if(p.getUsername().equals(uname)){
                                encontrado[0] = true;
                                if(p.getPassword().equals(pasw)){
                                    encontrado[1]=true;
                                    prof = p;

                                }
                            }
                        }
                        correct();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public void correct(){
        if(!encontrado[0]){
            Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
            unameP.setError("not found");
            unameP.setText("");
            paswP.setText("");
        }else if(!encontrado[1]){
            Toast.makeText(this, "Password incorrect", Toast.LENGTH_LONG).show();
            paswP.setError("incorrect");
            paswP.setText("");
        }else{
            String toastmes = "Welcome "+ prof.getUsername();
            Toast.makeText(this, toastmes, Toast.LENGTH_LONG).show();
            goToMain();
        }
    }
    public void validate(){
        String name = unameP.getText().toString();
        String pasw = paswP.getText().toString();
        if(name.equals("")){
            unameP.setError("required");
        }
        if(pasw.equals("")){
            paswP.setError("required");
        }
    }

}