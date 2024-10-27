package com.medieninformatik.patientcare.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Note {
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor")
        private Doctor doctor;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("note")
    private List<File> files;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public List<File> getFiles(){
        return this.files;
    }

    public void addFile(File file){
        this.files.add(file);
    }

    public void removeFile(File file){
        this.files.remove(file);
    }

        public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
      this.doctor = doctor;
    }


}
