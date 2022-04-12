package com.macularehab.professional.account;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.macularehab.R;

public class RestorePassword extends AppCompatActivity {

    private TextInputEditText emailAddressTextInput;
    private String emailAddress;

    private final String TAG = "ProfessRestorePassword";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_restore_password);

        emailAddressTextInput = findViewById(R.id.professional_logIn_restorePassword_EmailAddress_textInput);

        Button sendEmailButton = findViewById(R.id.professional_logIn_restorePassword_sendEmail_button);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readEmailAddress();
            }
        });
    }

    private void readEmailAddress() {

        emailAddress = String.valueOf(emailAddressTextInput.getText());

        Toast.makeText(this, emailAddress, Toast.LENGTH_LONG).show();

        sendEmail();
    }

    private void sendEmail() {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                        else {

                            Toast.makeText(RestorePassword.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
