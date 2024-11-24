package com.medieninformatik.patientcare.patientDataManagement.domain.model;

import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.IcdCode;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.Recommendation;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Diagnosis extends Note {

    private Patient patient;
    private Doctor doctor;
    private User creator;
    private Date date;
//    private String icdCode;

    private Recommendation recommendation;
    private IcdCode icdCode;

    // Konstruktor
    public Diagnosis(Patient patient, Doctor doctor, User creator, Date date, String icdCode, String recommendation) {
        this.patient = patient;
        this.doctor = doctor;
        this.creator = creator;
        this.date = date;
        this.icdCode = new IcdCode(icdCode);
        this.recommendation = new Recommendation(recommendation);
    }
}
