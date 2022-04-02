package com.macularehab.exercises;

import java.util.ArrayList;

public class ExerciseInfo {

    private int times_completed;
    private ArrayList<ResultInfo> results;

    public ExerciseInfo() {

        times_completed = 0;
        results = new ArrayList<>();
    }

    public int getTimes_completed() {
        return times_completed;
    }

    public void setTimes_completed(int times_completed) {
        this.times_completed = times_completed;
    }

    public ArrayList<ResultInfo> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultInfo> results) {
        this.results = results;
    }
}
