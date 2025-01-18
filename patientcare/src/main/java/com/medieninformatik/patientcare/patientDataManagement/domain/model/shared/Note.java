package com.medieninformatik.patientcare.patientDataManagement.domain.model.shared;

import com.fasterxml.jackson.annotation.*;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Diagnosis;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Measurement;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Treatment;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.File;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("note")
    private List<File> files;

    @ManyToOne
    @JoinColumn(name = "appointment")
    @JsonIgnore // Ignore this field for serialization
    private Appointment appointment;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Note() {
        this.timestamp = new Date(); // Set timestamp to current date by default
    }

    private String noteType;

    // Constructor with parameters
    public Note(Patient patient, Doctor doctor, Appointment appointment, List<File> files) {
        this.timestamp = new Date(); // Default to current timestamp
        this.patient = patient;
        this.doctor = doctor;
        this.appointment = appointment;
        this.files = files != null ? files : new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public List<File> getFiles() {
        return this.files;
    }

    public void addFile(File file) {
        this.files.add(file);
    }

    public void removeFile(File file) {
        this.files.remove(file);
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

}
