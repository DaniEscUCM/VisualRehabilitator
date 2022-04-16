package com.macularehab.exercises;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.macularehab.R;
import com.macularehab.internalStorage.ReadInternalStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class ExerciseResultHistory extends AppCompatActivity {

    private BarChart barChart;
    private ArrayList<BarEntry> barEntryArrayListCorrects;
    private ArrayList<BarEntry> barEntryArrayListFailed;
    private ArrayList<String> labelNames;
    private ArrayList<ResultInfo> resultsList;

    private final String filenameCurrentPatient = "CurrentPatient.json";
    private int exercise_id;

    private Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_exercise_results_history);

        resources = ExerciseResultHistory.this.getResources();

        exercise_id = getIntent().getIntExtra("exercise_id", 1);

        barChart = findViewById(R.id.exercise_results_history_barChart);

        barEntryArrayListCorrects = new ArrayList<BarEntry>();
        barEntryArrayListFailed = new ArrayList<BarEntry>();
        labelNames = new ArrayList<String>();
        readExercisesResults();
        fillBarChart();

        BarDataSet barDataSetCorrects = new BarDataSet(barEntryArrayListCorrects, resources.getString(R.string.exercises_resultHistory_correct));
        BarDataSet barDataSetFailed = new BarDataSet(barEntryArrayListFailed, resources.getString(R.string.exercises_resultHistory_failed));

        barDataSetCorrects.setColors(Color.BLUE);
        barDataSetFailed.setColors(Color.RED);
        Description description = new Description();
        description.setText(resources.getString(R.string.exercises_resultHistory_trials));
        description.setTextSize(20f);
        description.setTextColor(Color.BLACK);
        barChart.setDescription(description);

        BarData barData = new BarData(barDataSetCorrects, barDataSetFailed);

        barChart.setData(barData);
        float groupSpace = 0.5f;
        float barSpace = 0.05f;
        float barWidth = 0.3f;

        barData.setBarWidth(barWidth);
        barChart.groupBars(-0.5f, groupSpace, barSpace);

        Legend legend = barChart.getLegend();
        legend.setTextSize(20f);
        legend.setTextColor(Color.BLACK);
        legend.setForm(Legend.LegendForm.SQUARE);

        //Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelNames.size());
        xAxis.setLabelRotationAngle(0);
        //xAxis.setTextSize(20f);
        //xAxis.setAxisMinimum(1f);

        barChart.setDrawBorders(true);
        barChart.animateY(2000);
        barChart.invalidate();
    }

    private void readExercisesResults() {

        ReadInternalStorage readInternalStorage = new ReadInternalStorage();
        HashMap<String, Object> patientHashMap = readInternalStorage.read(getApplicationContext(), filenameCurrentPatient);

        if (patientHashMap.containsKey("exercise")) {

            LinkedTreeMap<String, Object> exercise = (LinkedTreeMap<String, Object>) patientHashMap.get("exercise");
            ArrayList<LinkedTreeMap<String, Object>> exercisesList = (ArrayList<LinkedTreeMap<String, Object>>) exercise.get("exerciseInfoList");
            LinkedTreeMap<String, Object> exerciseTwo = exercisesList.get(exercise_id);
            resultsList = new ArrayList<ResultInfo>();

            if (exerciseTwo.containsKey("resultsList")) {
                resultsList = (ArrayList<ResultInfo>) exerciseTwo.get("resultsList");
            }
        }
    }

    private void fillBarChart() {

        for (int i = 0; i < resultsList.size(); i++) {

            Gson gson = new Gson();
            String res_aux = gson.toJson(resultsList.get(i));
            ResultInfo resultInfo = gson.fromJson(res_aux, ResultInfo.class);
            int counterCorrect = resultInfo.getCounterCorrect();
            int counterFailed = resultInfo.getCounterFailed();
            barEntryArrayListCorrects.add(new BarEntry(i, counterCorrect));
            barEntryArrayListFailed.add(new BarEntry(i, counterFailed));
            //barEntryArrayList.add(new BarEntry(i, counterFailed));
            labelNames.add(resources.getString(R.string.exercises_resultHistory_trialsNumber) + i);
        }
    }
}
