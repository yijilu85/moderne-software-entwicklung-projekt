package com.medieninformatik.patientcare.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Note {
    private Date timestamp;

//    @OneToOne(mappedBy = "patient")
//    private Patient patient;

//    @OneToOne(mappedBy = "doctor")
//    private Doctor doctor;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
