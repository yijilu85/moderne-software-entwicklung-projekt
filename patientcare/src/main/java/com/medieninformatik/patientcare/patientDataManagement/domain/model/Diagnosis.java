package com.medieninformatik.patientcare.patientDataManagement.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.IcdCode;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.Recommendation;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Diagnosis extends Note {

    @Embedded
    private Recommendation recommendation;
    @Embedded
    private IcdCode icdCode;

    public Diagnosis() {
    }
    // Konstruktor
    public Diagnosis(String icdCode,
                     String recommendation) {

        this.icdCode = new IcdCode(icdCode);
        this.recommendation = new Recommendation(recommendation);
        this.setNoteType(this.getClass().getSimpleName().toUpperCase());

    }

    public String getIcdCode() {
        return this.icdCode.getCode();
    }

    public String getRecommendation() {
        return this.recommendation.getText();
    }
}
