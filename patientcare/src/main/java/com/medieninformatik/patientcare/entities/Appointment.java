package com.medieninformatik.patientcare.entities;

import java.time.LocalDateTime;

public class Appointment{

    private LocalDateTime APDate;
    private Patient patient;
    private Doctor doctor;
    private Note note;

    //Konstruktor
    public Appointment(LocalDateTime APDate, Patient patient, Doctor doctor, Note note){

        this.APDate = APDate;
        this.patient = patient;
        this.doctor = doctor;
        this.note = note;
}
    //Getter und Setter
    public LocalDateTime getAPDate() {
        return APDate;
}

    public void setAPDate(LocalDateTime APDate) {
        this.APDate = APDate;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Note getNote() {
        return this.note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    //Methoden

    public void scheduleAppointment(){

    }

    public void editAppointment(){

    }

    public void addNote(Note note){

    }

    public void deleteNote(Note note){

    }

    public void sendAppoinmentReminder(){

    }
}