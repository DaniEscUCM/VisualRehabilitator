package com.macularehab.exercises;

import java.util.ArrayList;

public class ExerciseInfo {

    private int times_completed;
    private ArrayList<ResultInfo> resultsList;

    public ExerciseInfo() {

        times_completed = 0;
        resultsList = new ArrayList<ResultInfo>();
    }

    public int getTimes_completed() {
        return times_completed;
    }

    public void setTimes_completed(int times_completed) {
        this.times_completed = times_completed;
    }

    public ArrayList<ResultInfo> getResultsList() {
        return resultsList;
    }

    public void setResultsList(ArrayList<ResultInfo> results) {
        this.resultsList = results;
    }
}
