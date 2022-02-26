package com.macularehab.model;

public class Patient {

    private String name;
    private String email_username;
    private String uid;

    public Patient(String name, String email_username, String uid) {

        this.name = name;
        this.email_username = email_username;
        this.uid = uid;
    }

    public String getName() {
        return this.name;
    }
    public String getEmail_username() { return this.email_username; }
    public String getUid() {
        return this.uid;
    }
}
