package com.macularehab.patient;

import java.util.ArrayList;
import java.util.Date;

public class Patient {

    private String date;
    private String name;
    private String first_lastName;
    private String second_lastName;
    private String gender;
    private String date_of_birth;
    private String diagnostic;
    private float av;
    private float cv;
    private String observations;

    /*** visual difficulties in children and adults ***/
    private boolean read;
    private boolean write;
    private boolean use_phone;
    private boolean whiteboard;
    private boolean job_difficulties;
    private boolean face_recognition;
    private boolean see_tv;
    private boolean move_interior;
    private boolean move_exterior;
    private boolean interior_to_exterior;
    private boolean money_management;
    private boolean feeding;
    private boolean home_activities;
    private boolean shopping;
    private boolean eating;
    private boolean personal_care;

    /*** visual behaviour ***/
    private boolean head_movement;
    private boolean tv_closeness;
    private boolean specific_illumination;
    private boolean sun_bother;

    /*** use of optics and no optics help ***/
    private boolean conventional_glasses;
    private boolean uses_sunglasses;
    private boolean glasses_are_useful;

    /*** reading ***/
    private boolean used_to_read_more;
    private boolean wants_to_read_more;

    private ArrayList<Boolean> checkBox;

    public Patient() {

        checkBox = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) checkBox.add(false);
    }

    /**** Getters And Setters ****/

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_lastName() {
        return first_lastName;
    }

    public void setFirst_lastName(String first_lastName) {
        this.first_lastName = first_lastName;
    }

    public String getSecond_lastName() {
        return second_lastName;
    }

    public void setSecond_lastName(String second_lastName) {
        this.second_lastName = second_lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public float getAv() {
        return av;
    }

    public void setAv(float av) {
        this.av = av;
    }

    public float getCv() {
        return cv;
    }

    public void setCv(float cv) {
        this.cv = cv;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public ArrayList<Boolean> getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(ArrayList<Boolean> checkBox) {
        this.checkBox = checkBox;
    }
}
