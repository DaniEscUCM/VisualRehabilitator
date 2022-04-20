package com.macularehab.professional.account;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;

public class ChangePhone extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private final String TAG = "ProfessChangePhone";

    private TextInputLayout changePhoneLayout;
    private TextInputEditText phoneAddressTextInput;
    private String phoneAddress;

    private LottieAnimationView loading_imageView;
    private LottieAnimationView result_imageView;

    private Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_profile_change_phone);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        resources = ChangePhone.this.getResources();

        changePhoneLayout = findViewById(R.id.professional_profile_changePhone_textInputLayout);
        phoneAddressTextInput = findViewById(R.id.professional_profile_changePhone_textInputLayout_textInput);

        loading_imageView = findViewById(R.id.professional_logIn_restorePassword_loadingEffect);
        result_imageView = findViewById(R.id.professional_logIn_restorePassword_loadingEffect_result);

        Button changeEmailButton = findViewById(R.id.professional_profile_changePhone_button);
        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readPhoneAddress();
            }
        });

        ImageButton backButton = findViewById(R.id.professional_profile_changePhone_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setImagesInvisible();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImagesInvisible();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setImagesInvisible();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setImagesInvisible();
    }

    private void setImagesInvisible() {

        loading_imageView.setVisibility(View.INVISIBLE);
        result_imageView.setVisibility(View.INVISIBLE);
    }

    private void readPhoneAddress() {

        result_imageView.setVisibility(View.INVISIBLE);

        phoneAddress = String.valueOf(phoneAddressTextInput.getText());

        Resources resources = ChangePhone.this.getResources();

        if (phoneAddress.equals("")) {
            changePhoneLayout.setError(resources.getString(R.string.emptyField));
            showAlertErrorUser(resources.getString(R.string.emptyField));
        }
        else if (phoneAddress.length() != 9) {
            changePhoneLayout.setError(resources.getString(R.string.professional_profile_changePhone_errorFormat));
            showAlertErrorUser(resources.getString(R.string.professional_profile_changePhone_errorFormat));
        }
        else {
            changePhone();
        }
    }

    private void changePhone() {

        changeDataBase();
    }

    private void changeDataBase() {

        databaseReference.child("Professional").child(mAuth.getCurrentUser().getUid())
                .child("contact_info").child("phone_number").setValue(phoneAddress);

        Toast.makeText(this, resources.getString(R.string.professional_profile_changePhone_successMessage), Toast.LENGTH_LONG).show();

        showReadyImage();
    }


    private void showReadyImage() {

        result_imageView.setAnimation(R.raw.ready);
        result_imageView.setVisibility(View.VISIBLE);
        result_imageView.playAnimation();
    }

    private void showAlertErrorUser(String st_error) {

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
