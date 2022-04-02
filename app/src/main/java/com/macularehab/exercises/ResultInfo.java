package com.macularehab.exercises;

public class ResultInfo {

    private int counterCorrect;
    private int counterFailed;
    private int counterNotClicked;

    public ResultInfo(int counterCorrect, int counterFailed, int counterNotClicked) {

        this.counterCorrect = counterCorrect;
        this.counterFailed = counterFailed;
        this.counterNotClicked = counterNotClicked;
    }
}
