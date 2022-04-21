package com.macularehab.exercises;

import java.util.ArrayList;

public class Exercise {

    private final int num_exercises = 17;
    private int exercises_completed;
    private ArrayList<ExerciseInfo> exerciseInfoList;

    public Exercise() {

        exercises_completed = 0;
        exerciseInfoList = new ArrayList<ExerciseInfo>();
        for (int i = 0; i < num_exercises; i++) {
            exerciseInfoList.add(new ExerciseInfo());
        }
    }

    public int getNum_exercises() {
        return num_exercises;
    }

    public int getExercises_completed() {
        return exercises_completed;
    }

    public void setExercises_completed(int exercises_completed) {
        this.exercises_completed = exercises_completed;
    }

    public ArrayList<ExerciseInfo> getExerciseInfoList() {
        return exerciseInfoList;
    }

    public void setExerciseInfoList(ArrayList<ExerciseInfo> exerciseInfoList) {
        this.exerciseInfoList = exerciseInfoList;
    }
}
