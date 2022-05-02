package com.macularehab.professional.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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