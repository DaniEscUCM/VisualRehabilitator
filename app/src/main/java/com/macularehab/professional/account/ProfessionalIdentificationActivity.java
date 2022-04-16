package com.macularehab.professional.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.macularehab.R;
import com.macularehab.professional.account.ProfessionalLoginActivity;
import com.macularehab.professional.account.ProfessionalSingingActivity;

public class ProfessionalIdentificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_identification);

        ImageButton buttonBack = (ImageButton) findViewById(R.id.imageButton_back_ident);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(v);
            }
        });

        Button buttonLogin = (Button) findViewById(R.id.button_professional_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        Button butonSingIn = (Button) findViewById(R.id.button_professional_singin);
        butonSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singin(v);
            }
        });
    }

    public void close(View view){
        finish();
    }

    public void login(View view){
        Intent i = new Intent(this, ProfessionalLoginActivity.class);
        startActivity(i);
    }

    public void singin(View view){
        Intent i = new Intent(this, ProfessionalSingingActivity.class);
        startActivity(i);
    }
}