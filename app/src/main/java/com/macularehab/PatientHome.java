package com.macularehab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.login.LogIn;

public class PatientHome extends AppCompatActivity {

    private TextView welcome_name_text;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_home);

        welcome_name_text = findViewById(R.id.patient_home_welcome_name);

        Button logOut_button = findViewById(R.id.button_patient_home_logout);
        logOut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        getUsername();
    }

    private void getUsername() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        databaseReference.child("Patient").child(currentUser.getUid()).child("name")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    Log.w("firebase", value);

                    PatientHome.this.setUsername(value);
                    //LogIn.this.patientLogin.startHome(value);
                }
            }
        });
    }

    private void setUsername(String username) {

        welcome_name_text.setText(username);
    }

    private void logOut() {

        FirebaseAuth.getInstance().signOut();

        welcome_name_text.setText("Patient");

        Intent mainActivity = new Intent(this, IdentificationActivity.class);
        startActivity(mainActivity);
    }
}
