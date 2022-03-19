package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.macularehab.language.LocaleHelper;

import java.util.Locale;

public class IdentificationActivity extends AppCompatActivity {

    private ImageView selectLanguage;
    private int currentLanguage = 0;
    private LocaleHelper localeHelper;
    private Context context;
    private Resources resources;

    private TextView whosIsUsing;
    private Button buttonPatient;
    private Button buttonProfessional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_identification);

        selectLanguage = findViewById(R.id.imageView_select_language);
        selectLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage();
            }
        });

        whosIsUsing = findViewById(R.id.textView_who_is_using_this_app);

        buttonPatient =(Button) findViewById(R.id.button_patient_iden);
        buttonPatient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { patient(v);}
        });

        buttonProfessional =(Button) findViewById(R.id.button_professional_ident);
        buttonProfessional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                professional(v);
            }
        });
    }

    private void changeLanguage() {

        context = this;

        if (currentLanguage == 1) {

            selectLanguage.setImageResource(R.drawable.united_kingdom);
            currentLanguage = 0;
            //context = LocaleHelper.setLocale(IdentificationActivity.this, "es");
            /*resources = context.getResources();
            buttonPatient.setText(resources.getString(R.string.textview_loginPatient));
            buttonProfessional.setText(resources.getString(R.string.textview_loginProfessional));
            whosIsUsing.setText(resources.getString(R.string.textview_whoIsUsing));*/
            Locale locale = new Locale("es");
            Locale.setDefault(locale);
            Configuration configuration = context.getResources().getConfiguration();
            configuration.setLocale(locale);
            context.createConfigurationContext(configuration);
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
            resources = context.getResources();
            buttonPatient.setText(resources.getString(R.string.textview_loginPatient));
            buttonProfessional.setText(resources.getString(R.string.textview_loginProfessional));
            whosIsUsing.setText(resources.getString(R.string.textview_whoIsUsing));
        }
        else {
            selectLanguage.setImageResource(R.drawable.spain);
            currentLanguage = 1;
            //context = LocaleHelper.setLocale(IdentificationActivity.this, "en");
            /*resources = context.getResources();
            buttonPatient.setText(resources.getString(R.string.textview_loginPatient));
            buttonProfessional.setText(resources.getString(R.string.textview_loginProfessional));
            whosIsUsing.setText(resources.getString(R.string.textview_whoIsUsing));*/
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration configuration = context.getResources().getConfiguration();
            configuration.setLocale(locale);
            context.createConfigurationContext(configuration);
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
            resources = context.getResources();
            buttonPatient.setText(resources.getString(R.string.textview_loginPatient));
            buttonProfessional.setText(resources.getString(R.string.textview_loginProfessional));
            whosIsUsing.setText(resources.getString(R.string.textview_whoIsUsing));
        }
    }

    public void patient(View view){
        Intent patientView = new Intent(this, PatientIdentificationActivity.class);
        //Intent patientView = new Intent(this, PatientLogin.class);
        startActivity(patientView);
    }

    public void professional(View view){
        Intent i = new Intent( this, ProfessionalIdentificationActivity.class);
        startActivity(i);
    }
}