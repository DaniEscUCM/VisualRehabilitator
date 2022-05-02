package com.macularehab.patient.logIn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
//Firebase Analytics
//import com.google.firebase.analytics.FirebaseAnalytics;
import com.macularehab.R;
import com.macularehab.login.LogIn;
import com.macularehab.patient.PatientHome;

public class PatientLogin extends AppCompatActivity  {

    //private FirebaseAnalytics mFirebaseAnalytics;
    private LogIn logIn;
    private TextView email_text;
    private TextView password_text;
    private String email_username;
    private String password;
    public static final String getUserName = "com.macularehab.getUserName";
    private final String SHARED_PREF_FILE = "com.macularehab.sharedprefs.user_is_logged";
    private final String SHARED_PREF_PATIENT_USER_LOGGED_KEY = "patient_user_logged";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_login);

        this.logIn = new LogIn();

        this.email_text = findViewById(R.id.patient_email_login);
        this.password_text = findViewById(R.id.patient_password_login);

        Log.w("actividad LogIn", " creada");

        Button signUpButton = findViewById(R.id.patient_login_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.w("boton LogIn", " presionado");
                readEmail(); //Se bloquea para hacerlo bien desde professional
            }
        });

        ImageButton buttonBack = (ImageButton) findViewById(R.id.imageButton_back_pat_login);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(v);
            }
        });

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
    protected void onResume() {
        super.onResume();
        hideNavigationAndStatusBar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideNavigationAndStatusBar();
    }

    public void close(View view){
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        hideNavigationAndStatusBar();
        // Check if user is signed in (non-null) and update UI accordingly.

        Log.w("actividad LogIn", " empezada");

        if (isConnected()) {
            boolean signedIn = this.logIn.patient_is_signed_in(this);
            if (signedIn) {
                Log.w("actividad LogIn", " paciente conectado");
                reload();
            }
        }
        else {

            boolean isLogged = false;
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
            isLogged = sharedPreferences.getBoolean(SHARED_PREF_PATIENT_USER_LOGGED_KEY, isLogged);

            if (isLogged) {
                reload();
            }
        }
    }

    private void readEmail() {

        this.email_username = this.email_text.getText().toString().trim();
        this.password = this.password_text.getText().toString().trim();

        Log.w("EMAIL: ", email_username);
        Log.w("PASSWORD: ", password);

        if (validate_user_input()) {
            this.logIn.readEmail(email_username, password, this);
        }
    }

    //TODO
    public void user_loggedIn_successfully() {

        Resources resources = PatientLogin.this.getResources();
        Toast.makeText(PatientLogin.this, resources.getString(R.string.user_loggedIn_text), Toast.LENGTH_LONG).show();
        reload();
        clean();
    }

    public void user_login_failed() {

        Resources resources = PatientLogin.this.getResources();
        Toast.makeText(PatientLogin.this, resources.getString(R.string.error_authentication_failed),
                Toast.LENGTH_LONG).show();
        showAlertFailToLogIn();
    }


    public boolean validate_user_input() {

        boolean all_correct = true;

        if(this.email_username.equals("")){
            this.email_text.setError("required");
            all_correct = false;
            showAlertEnterEmailAndPassword();
        }
        if(this.password.equals("")){
            this.password_text.setError("required");
            all_correct = false;
            showAlertEnterEmailAndPassword();
        }
        if (this.password.length() < 6) {
            this.password_text.setError("Password must be at least 6 characters");
            all_correct = false;
            showAlertWeakPassword();
        }

        return all_correct;
    }

    //TODO
    public void showAlertEnterEmailAndPassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Name and/or Email and/or Password field is empty")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlertWeakPassword() {

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


    public void showAlertFailToLogIn() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Failed To LogIn. Email/Username and/or Password incorrect")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clean(){
        this.email_text.setText("");
        this.password_text.setText("");
    }

    public void reload() {

        saveLoggedUser();

        Intent intent = new Intent( this, PatientHome.class);
        startActivity(intent);
    }

    public void showAlertYouAreAProfessional() {

        Resources resources = this.getResources();
        String st_passwordDoestExist = resources.getString(R.string.patient_login_youAreAProfessional);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(st_passwordDoestExist)
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

    private boolean isConnected() {

        boolean isConnected = false;

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        return isConnected;
    }

    private void saveLoggedUser() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SHARED_PREF_PATIENT_USER_LOGGED_KEY, true);

        editor.apply();
    }
}
