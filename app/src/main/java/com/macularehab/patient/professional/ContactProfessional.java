package com.macularehab.patient.professional;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.patient.Patient;

import java.util.HashMap;

public class ContactProfessional extends AppCompatActivity {

    private TextView emailTextView;
    private TextView phoneTextView;

    private String professionalUID;
    private String professionalEmail;
    private String professionalPhone;

    private final String filenameCurrentPatient = "CurrentPatient.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_patient_contact_professional);

        emailTextView = findViewById(R.id.patient_professional_contact_emailTextView);
        phoneTextView = findViewById(R.id.patient_professional_contact_phoneTextView);

        Button sendEmailButton = findViewById(R.id.patient_professional_contact_sendEmail_button);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        Button callProfessionalButton = findViewById(R.id.patient_professional_contact_call_button);
        callProfessionalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callProfessional();
            }
        });

        ImageButton backButton = findViewById(R.id.patient_professional_contact_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button returnHomeButton = findViewById(R.id.patient_professional_contact_returnBack_button);
        returnHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getProfessionalInfo();

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

    private void getProfessionalInfo() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        if (map.containsKey("professional_uid")) {

            professionalUID = String.valueOf(map.get("professional_uid"));
        }

        getProfessionalContactInfo();
    }

    private void getProfessionalContactInfo() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        databaseReference.child("Professional").child(professionalUID).child("contact_info")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    HashMap<String, Object> map = (HashMap<String, Object>) task.getResult().getValue();

                    professionalEmail = "";
                    professionalPhone = "";

                    if (map.containsKey("email_address")) {

                        professionalEmail = String.valueOf(map.get("email_address"));
                    }
                    if (map.containsKey("phone_number")) {

                        professionalPhone = String.valueOf(map.get("phone_number"));
                    }

                    emailTextView.setText(professionalEmail);
                    if (!professionalPhone.equals("none")) {
                        phoneTextView.setText(professionalPhone);
                    }
                }
            }
        });
    }

    private void sendEmail() {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ professionalEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MaculaRehabApp:");
        emailIntent.setType("message/rfc822");
        //emailIntent.setType("*/*");
        startActivity(Intent.createChooser(emailIntent, "Choose:"));
    }

    private void callProfessional() {

        if (!professionalPhone.equals("none")) {
            
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, professionalPhone);
            startActivity(callIntent);
        }
        else {

            Resources resources = ContactProfessional.this.getResources();
            String st_error = resources.getString(R.string.error_no_valid_phoneNumber);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(st_error)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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
}
