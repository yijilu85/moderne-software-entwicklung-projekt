package com.medieninformatik.patientcare.entities;

import java.util.Date;

public class Treatment {

    public Patient patient;
    public Doctor doctor;
    public User creator;
    public Date date;
    public Diagnosis diagnosis;
    public String action;

    // Konstruktor
    public Treatment(Patient patient, Doctor doctor, User creator, Date date, Diagnosis diagnosis, String action) {
        this.patient = patient;
        this.doctor = doctor;
        this.creator = creator;
        this.date = date;
        this.diagnosis = diagnosis;
        this.action = action;
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

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public String getAction() {
        return action;
    }
}
