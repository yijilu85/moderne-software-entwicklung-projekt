package com.medieninformatik.patientcare.patientDataManagement.domain.model.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.File;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
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

    @ManyToOne
    @JoinColumn(name = "appointment")
    private Appointment appointment;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void addFile(File file) {
        this.files.add(file);
    }

    public void removeFile(File file) {
        this.files.remove(file);
    }


}
