package com.medieninformatik.patientcare.entities;

import java.util.Date;

public class Diagnosis extends Note {

    private Patient patient;
    private Doctor doctor;
    private User creator;
    private Date date;
    private String icdCode;
    private String recommendation;

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
        return this.patient;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public User getCreator() {
        return this.creator;
    }

    public Date getDate() {
        return this.date;
    }

    public String getIcdCode() {
        return this.icdCode;
    }

    public String getRecommendation() {
        return this.recommendation;
    }
}
