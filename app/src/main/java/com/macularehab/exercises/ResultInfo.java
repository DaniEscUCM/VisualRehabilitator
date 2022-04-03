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

    public int getCounterCorrect() {
        return counterCorrect;
    }

    public void setCounterCorrect(int counterCorrect) {
        this.counterCorrect = counterCorrect;
    }

    public int getCounterFailed() {
        return counterFailed;
    }

    public void setCounterFailed(int counterFailed) {
        this.counterFailed = counterFailed;
    }

    public int getCounterNotClicked() {
        return counterNotClicked;
    }

    public void setCounterNotClicked(int counterNotClicked) {
        this.counterNotClicked = counterNotClicked;
    }
}
