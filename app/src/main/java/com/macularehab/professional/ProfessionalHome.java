package com.macularehab.professional;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macularehab.R;
import com.macularehab.patient.Patient;

import java.util.List;

public class ProfessionalHome extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private final int LOADER_ID = 0;
    private PatientListLoaderCallBacks patientListLoaderCallBacks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_professional_home);

        firebaseDatabase = FirebaseDatabase.getInstance("https://macularehab-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        patientListLoaderCallBacks = new PatientListLoaderCallBacks();
    }

    private void createBundle() {

        Bundle queryBundle = new Bundle();
        //TODO posible error al obtener uid
        queryBundle.putString(PatientListLoaderCallBacks.PROFESSIONAL_UID, mAuth.getUid());
        //queryBundle.putString(PatientListLoaderCallBacks.EXTRA_TITLE, title);
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, queryBundle, patientListLoaderCallBacks);
    }

    public class PatientListLoaderCallBacks implements LoaderManager.LoaderCallbacks<List<Patient>> {

        private static final String PROFESSIONAL_UID = "professionalID";

        private Context context;

        public PatientListLoaderCallBacks(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public Loader<List<Patient>> onCreateLoader(int id, @Nullable Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<Patient>> loader, List<Patient> data) {

        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<Patient>> loader) {

        }
    }
}
