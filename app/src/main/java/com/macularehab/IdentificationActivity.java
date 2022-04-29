package com.macularehab;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.macularehab.language.LocaleHelper;
import com.macularehab.patient.PatientHome;
import com.macularehab.patient.PatientIdentificationActivity;
import com.macularehab.professional.account.ProfessionalIdentificationActivity;

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

    private boolean isConnected;

    private final String SHARED_PREF_FILE = "com.macularehab.sharedprefs.user_is_logged";
    private final String SHARED_PREF_PATIENT_USER_LOGGED_KEY = "patient_user_logged";

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

        checkIfPatientIsLogged();

        setUiListener();
    }

    private void setUiListener() {

        View decorView = getWindow().getDecorView();

        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 2000ms
                                    hideNavigationAndStatusBar();
                                }
                            }, 2000);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideNavigationAndStatusBar();
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context.createConfigurationContext(configuration);
            }
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context.createConfigurationContext(configuration);
            }
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
            resources = context.getResources();
            buttonPatient.setText(resources.getString(R.string.textview_loginPatient));
            buttonProfessional.setText(resources.getString(R.string.textview_loginProfessional));
            whosIsUsing.setText(resources.getString(R.string.textview_whoIsUsing));
        }
    }

    public void patient(View view) {

        checkInternetConnection();

        if (isConnected) {

            Intent patientIntent = new Intent(this, PatientIdentificationActivity.class);
            startActivity(patientIntent);
        }
        else {
            showAlertNotInternetConnection();
        }
    }

    public void professional(View view) {

        checkInternetConnection();

        if (isConnected) {

            Intent professionalIntent = new Intent( this, ProfessionalIdentificationActivity.class);
            startActivity(professionalIntent);
        }
        else {
            showAlertNotInternetConnection();
        }
    }

    private void checkInternetConnection() {

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnected();//activeNetwork.isConnectedOrConnecting();

        isConnected = true;
    }

    private void showAlertNotInternetConnection() {

        Resources resources = this.getResources();
        String st_error = resources.getString(R.string.noInternetConnection);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(st_error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void hideNavigationAndStatusBar() {

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        decorView.setSystemUiVisibility(uiOptions);
    }

    private void checkIfPatientIsLogged() {

        if (isPatientLogged()) {

            Intent intent = new Intent(this, PatientHome.class);
            startActivity(intent);
        }
    }

    private boolean isPatientLogged() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        boolean logged = false;

        logged = sharedPreferences.getBoolean(SHARED_PREF_PATIENT_USER_LOGGED_KEY, logged);

        return logged;
    }
}