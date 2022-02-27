package com.macularehab;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfessionalCreateNewPatient extends AppCompatActivity {

    private Spinner userGender_spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_create_new_patient);
    }

    private void spinner() {

        userGender_spinner = (Spinner) findViewById(R.id.spinner);

        String genders[] = new String[] {
                "MALE", "FEMALE", "OTHER"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userGender_spinner.setAdapter(adapter);
    }
}
