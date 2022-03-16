package com.macularehab.professional;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.ManualInputStainLeftActivity;
import com.macularehab.R;

import java.util.Map;

public class ProfessionalCreateNewPatientShowNumericCode extends AppCompatActivity {

    private int numericCode;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final String db_patient = "Patient";
    private final String db_patients = "Patients";
    private final String db_professional = "Professional";
    private TextView text_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_create_new_patient_show_numeric_code);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();

        Intent intent = getIntent();
        numericCode = intent.getIntExtra(ProfessionalCreateNewPatientDifficulties.numericCodeString, 0);

        Log.w("Patient Code", String.valueOf(numericCode));

        TextView textView = findViewById(R.id.create_new_patient_show_numeric_code_numeric_code);
        textView.setText(String.valueOf(numericCode));

        text_name = findViewById(R.id.create_new_patient_show_numeric_code_patient_name);

        Log.w("Aqui", "Aqui");

        getPatientName();

        Button button = findViewById(R.id.create_new_patient_show_numeric_code_continue_to_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueToNextActivity();
            }
        });
    }

    private void getPatientName() {

        databaseReference.child(db_professional).child(db_patients).child(String.valueOf(numericCode)).child("name")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Patient", "No se ha conseguido");
                }
                else {

                    String name = String.valueOf(task.getResult().getValue());
                    Log.w("Patient Name", name);
                    text_name.setText(name);
                }
            }
        });
    }

    private void continueToNextActivity() {

        Intent i = new Intent( this, ManualInputStainLeftActivity.class);
        startActivity(i);
    }
}
