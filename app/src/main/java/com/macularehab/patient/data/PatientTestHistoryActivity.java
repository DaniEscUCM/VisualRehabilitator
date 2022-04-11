package com.macularehab.patient.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.ManualInputStainLeftActivity;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;
import com.macularehab.patient.TestsListAdapter;
import com.macularehab.professional.ProfessionalPatientInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientTestHistoryActivity extends AppCompatActivity {

    private final String filenameCurrentPatient = "CurrentPatient.json";
    private RecyclerView recyclerView;
    private TestsListAdapter testListAdapter;

    private ArrayList<String> dates;

    private HashMap<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_test_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        if (map.containsKey("Tests")) {
            LinkedTreeMap<String, LinkedTreeMap<String, Object>> linked = (LinkedTreeMap<String, LinkedTreeMap<String, Object>>) map.get("Tests");
            dates = new ArrayList<>(linked.keySet());
        }

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close(v));

        //Cards
        recyclerView = findViewById(R.id.testsList_recyclerView);
        testListAdapter = new TestsListAdapter(getApplicationContext(), new ArrayList<String>(), this);
        recyclerView.setAdapter(testListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (dates != null) {
            updateTestsList(dates);
        }

        //Search
        SearchView searchView = (SearchView) findViewById(R.id.search_test);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                testListAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    @Override
    public void finish(){
        Intent i = new Intent( this, PatientDataInfoActivity.class );
        startActivity(i);
    }
    public void updateTestsList(List<String> testList) {
        testListAdapter.setTestsListData(testList);
        testListAdapter.notifyDataSetChanged();
    }

    public void Close(View view){
        finish();
    }
}