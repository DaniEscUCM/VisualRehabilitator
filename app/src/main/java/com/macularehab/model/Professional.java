package com.macularehab.model;

public class Professional {

    private String uid;
    private String Name;
    private int ProfessionalNumericCode;
    private int NumberOfPatients;

    public Professional(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getProfessionalNumericCode() {
        return ProfessionalNumericCode;
    }

    public void setProfessionalNumericCode(int professionalNumericCode) {
        ProfessionalNumericCode = professionalNumericCode;
    }

    public int getNumberOfPatients() {
        return NumberOfPatients;
    }

    public void setNumberOfPatients(int numberOfPatients) {
        NumberOfPatients = numberOfPatients;
    }

    @Override
    public String toString() {
        return Name;
    }
}
