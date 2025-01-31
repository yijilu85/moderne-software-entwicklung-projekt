package com.medieninformatik.patientcare.patientDataManagement.domain.model.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Diagnosis;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Measurement;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Treatment;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)

@JsonSubTypes({
        @JsonSubTypes.Type(value = Diagnosis.class, name = "Diagnosis"),
        @JsonSubTypes.Type(value = Treatment.class, name = "Treatment"),
        @JsonSubTypes.Type(value = Measurement.class, name = "Measurement")
})
public class Note {
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "patient")
    @JsonIgnore // Ignore this field for serialization
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor")
    @JsonIgnore // Ignore this field for serialization
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "appointment")
    @JsonIgnore // Ignore this field for serialization
    private Appointment appointment;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String noteType;

    public Note() {
        this.timestamp = new Date(); // Set timestamp to current date by default
    }

    // Constructor with parameters
    public Note(Patient patient, Doctor doctor, Appointment appointment) {
        this.timestamp = new Date(); // Default to current timestamp
        this.patient = patient;
        this.doctor = doctor;
        this.appointment = appointment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Patient getPatient() {
        return patient;
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

    public String getNoteType() {
        return this.noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
