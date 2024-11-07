package com.medieninformatik.patientcare.entities;

import java.util.Date;

public class Diagnosis {

    public Patient patient;
    public Doctor doctor;
    public User creator;
    public Date date;
    public String icdCode;
    public String recommendation;

    // Konstruktor
    public Diagnosis(Patient patient, Doctor doctor, User creator, Date date, String icdCode, String recommendation) {
        this.patient = patient;
        this.doctor = doctor;
        this.creator = creator;
        this.date = date;
        this.icdCode = icdCode;
        this.recommendation = recommendation;
    }

    // Getter und Setter (optional, falls du sie benötigst)
    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public User getCreator() {
        return creator;
    }

    public Date getDate() {
        return date;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public String getRecommendation() {
        return recommendation;
    }
}
