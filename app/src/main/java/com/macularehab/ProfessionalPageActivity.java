package com.macularehab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.macularehab.model.Patient;
import com.macularehab.login.SignUp;

import java.util.ArrayList;
import java.util.List;

public class ProfessionalPageActivity extends AppCompatActivity {

    private List<String> listPatientUID = new ArrayList<String>();
    private List<Patient> listPatient = new ArrayList<Patient>();
    private SignUp signUp;
    private  String professional_uid;

    ListView listV_patients;

    ArrayAdapter<Patient> arrayAdapterPatient;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    
    private TextView welc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_page);

        listV_patients = findViewById(R.id.listView_professionalPatients);
        welc = (TextView) findViewById(R.id.textView_welcome_professional);

        getUname();


        Button firstPatientButton =(Button) findViewById(R.id.button_firstpatient);
        firstPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain(v);
            }
        });

        Button button_add_patient =(Button) findViewById(R.id.button_opticaltest);
        button_add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_patient(v);
            }
        });

        Button button_logOut =(Button) findViewById(R.id.button_professional_logout);
        button_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        Button button_newpatient = (Button) findViewById(R.id.button_professional_newpatient);
        button_newpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNewPat(v);
            }
        });
        //initializeListFirebase();
        this.signUp = new SignUp();
        this.professional_uid = this.signUp.getUID();


        listUID();
    }

    private void listUID() {
        //In these function we get a list of the Patient UIDs for later adding to array;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Professional").child(professional_uid).child("Patients").addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPatientUID.clear();
                for(DataSnapshot objSnapshot : snapshot.getChildren()){
                    String patientUID= objSnapshot.getValue(String.class);
                    listPatientUID.add(patientUID);
                }
                listPatientData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listPatientData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        listPatient.clear();
        for(String patUID : listPatientUID) {
            databaseReference.child("Patient").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {

                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                            //String s = objSnapshot.child("name").getValue(String.class);
                            Patient patient = objSnapshot.getValue(Patient.class);
                            if(patient.getUid().equals(patUID)) {
                                listPatient.add(patient);
                                arrayAdapterPatient = new ArrayAdapter<Patient>(ProfessionalPageActivity.this, android.R.layout.simple_list_item_1, listPatient);
                                listV_patients.setAdapter(arrayAdapterPatient);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void initializeListFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
    }

    public void goToMain(View view){
        Intent i = new Intent( this, MainActivity.class);
        startActivity(i);
    }

    public void gotoNewPat(View view){
        Intent i = new Intent( this, PatientSignUp.class);
        startActivity(i);
    }

    public void add_patient(View view){
        Intent i = new Intent( this, ManualInputStainLeftActivity.class);
        startActivity(i);
    }

    private void getUname(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        databaseReference.child("Professional").child(currentUser.getUid()).child("name")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    String value = String.valueOf(task.getResult().getValue());
                    Log.w("firebase", value);
                    welc.setText(value);
                }
            }
        });
    }

    private void logOut() {

        FirebaseAuth.getInstance().signOut();

        welc.setText("Patient");

        Intent mainActivity = new Intent(this, IdentificationActivity.class);
        startActivity(mainActivity);
    }
}