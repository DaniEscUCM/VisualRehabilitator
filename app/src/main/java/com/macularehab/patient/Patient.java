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
    private String professional_name;
    private String professional_uid;
    private boolean hasAccount;
    private String patient_uid;
    private String patient_numeric_code;
    private int exercises_accomplished;
    private String date_last_test;
    private boolean focusIsOn;

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

        checkBox = new ArrayList<>(33);
        for (int i = 0; i < 33; i++) checkBox.add(false);

        hasAccount = false;
        exercises_accomplished = 0;
        focusIsOn = true;
        date_last_test = "01/01/01";
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

    public String getProfessional_name() {
        return professional_name;
    }

    public void setProfessional_name(String professional_name) {
        this.professional_name = professional_name;
    }

    public String getProfessional_uid() {
        return professional_uid;
    }

    public void setProfessional_uid(String professional_uid) {
        this.professional_uid = professional_uid;
    }

    public boolean isHasAccount() {
        return hasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        this.hasAccount = hasAccount;
    }

    public String getPatient_uid() {
        return patient_uid;
    }

    public void setPatient_uid(String patient_uid) {
        this.patient_uid = patient_uid;
    }

    public String getPatient_numeric_code() {
        return patient_numeric_code;
    }

    public void setPatient_numeric_code(String patient_numeric_code) {
        this.patient_numeric_code = patient_numeric_code;
    }

    public int getExercises_accomplished() {
        return exercises_accomplished;
    }

    public void setExercises_accomplished(int exercises_accomplished) {
        this.exercises_accomplished = exercises_accomplished;
    }

    public String getDate_last_test() {
        return date_last_test;
    }

    public void setDate_last_test(String date_last_test) {
        this.date_last_test = date_last_test;
    }

    public boolean isFocusIsOn() {
        return focusIsOn;
    }

    public void setFocusIsOn(boolean focusIsOn) {
        this.focusIsOn = focusIsOn;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", first_lastName='" + first_lastName + '\'' +
                ", second_lastName='" + second_lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", diagnostic='" + diagnostic + '\'' +
                ", av=" + av +
                ", cv=" + cv +
                ", observations='" + observations + '\'' +
                ", read=" + read +
                ", write=" + write +
                ", use_phone=" + use_phone +
                ", whiteboard=" + whiteboard +
                ", job_difficulties=" + job_difficulties +
                ", face_recognition=" + face_recognition +
                ", see_tv=" + see_tv +
                ", move_interior=" + move_interior +
                ", move_exterior=" + move_exterior +
                ", interior_to_exterior=" + interior_to_exterior +
                ", money_management=" + money_management +
                ", feeding=" + feeding +
                ", home_activities=" + home_activities +
                ", shopping=" + shopping +
                ", eating=" + eating +
                ", personal_care=" + personal_care +
                ", head_movement=" + head_movement +
                ", tv_closeness=" + tv_closeness +
                ", specific_illumination=" + specific_illumination +
                ", sun_bother=" + sun_bother +
                ", conventional_glasses=" + conventional_glasses +
                ", uses_sunglasses=" + uses_sunglasses +
                ", glasses_are_useful=" + glasses_are_useful +
                ", used_to_read_more=" + used_to_read_more +
                ", wants_to_read_more=" + wants_to_read_more +
                ", checkBox=" + checkBox +
                '}';
    }
}
