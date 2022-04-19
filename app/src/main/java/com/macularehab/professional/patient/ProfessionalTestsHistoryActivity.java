package com.macularehab.professional.patient;

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
import com.macularehab.professional.patient.ProfessionalPatientInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfessionalTestsHistoryActivity extends AppCompatActivity {


    private final String filenameCurrentPatient = "CurrentPatient.json";
    private RecyclerView recyclerView;
    private TestsListAdapter testListAdapter;

    private ArrayList<String> dates;

    private HashMap<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_tests_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        map = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        if (map.containsKey("Tests")) {
            LinkedTreeMap<String, LinkedTreeMap<String, Object>> linked = (LinkedTreeMap<String, LinkedTreeMap<String, Object>>) map.get("Tests");
            dates = new ArrayList<>(linked.keySet());
        }

        ImageButton button = (ImageButton) findViewById(R.id.imageButton_back_results);
        button.setOnClickListener(v -> Close());

        //Cards
        recyclerView = findViewById(R.id.testsList_recyclerView);
        testListAdapter = new TestsListAdapter(getApplicationContext(), new ArrayList<String>(), this);
        recyclerView.setAdapter(testListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (dates != null) {
            updateTestsList(dates);
        }

        //new test
        ImageButton new_test = findViewById(R.id.new_test);
        new_test.setOnClickListener(v->add_test());

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

    public void Close(){
        Intent i = new Intent( this, ProfessionalPatientInfo.class );
        startActivity(i);
    }
    public void updateTestsList(List<String> testList) {
        testListAdapter.setTestsListData(testList);
        testListAdapter.notifyDataSetChanged();
    }

    private void add_test(){
        String patient_id = (String) map.get("patient_numeric_code");
        Intent i = new Intent( this, ManualInputStainLeftActivity.class );
        i.putExtra("patient_id",patient_id);
        startActivity(i);
    }

}