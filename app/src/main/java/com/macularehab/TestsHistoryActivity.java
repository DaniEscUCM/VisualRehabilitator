package com.macularehab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageButton;

import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.patient.Patient;
import com.macularehab.patient.TestsListAdapter;
import com.macularehab.professional.ProfessionalPatientHome;
import com.macularehab.professional.patientList.PatientListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestsHistoryActivity extends AppCompatActivity {


    private final String filenameCurrentPatient = "CurrentPatient.json";
    private RecyclerView recyclerView;
    private TestsListAdapter testListAdapter;

    private ArrayList<String> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        //String id = map.get("id").toString();
        if (map.containsKey("Tests")) {
            dates = (ArrayList<String>) map.get("Tests");
        }
        recyclerView = findViewById(R.id.testsList_recyclerView);
        testListAdapter = new TestsListAdapter(getApplicationContext(), new ArrayList<String>(), this);
        recyclerView.setAdapter(testListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateTestsList(dates);

        ImageButton new_test = findViewById(R.id.new_test);
        new_test.setOnClickListener(v->add_test());
    }

    public void updateTestsList(List<String> testList) {
        testListAdapter.setTestsListData(testList);
        testListAdapter.notifyDataSetChanged();
    }

    private void add_test(){
        Intent i = new Intent( this, ManualInputStainLeftActivity.class );
        startActivity(i);
    }
}