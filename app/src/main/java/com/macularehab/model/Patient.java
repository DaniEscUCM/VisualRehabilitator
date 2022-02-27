package com.macularehab.model;

public class Patient {

    private String name;
    private String email_username;
    private String uid;
    private String firstname;
    private String lastname;
    private String focuspoint;
    private String leftstain;
    private String rightstain;
    private String stain;
    private String professional;

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public void setEmail_username(String email_username) {
        this.email_username = email_username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFocuspoint() {
        return focuspoint;
    }

    public void setFocuspoint(String focuspoint) {
        this.focuspoint = focuspoint;
    }

    public String getLeftstain() {
        return leftstain;
    }

    public void setLeftstain(String leftstain) {
        this.leftstain = leftstain;
    }

    public String getRightstain() {
        return rightstain;
    }

    public void setRightstain(String rightstain) {
        this.rightstain = rightstain;
    }

    public String getStain() {
        return stain;
    }

    public void setStain(String stain) {
        this.stain = stain;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public void setName(String name) {
        this.name = name;
    }

    private String age;
    private String gender;

    public Patient() {

    }

    public String getName() {
        return this.name;
    }
    public String getEmail_username() { return this.email_username; }
    public String getUid() {
        return this.uid;
    }
}
