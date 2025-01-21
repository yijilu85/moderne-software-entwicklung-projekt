package com.medieninformatik.patientcare.patientDataManagement.domain.model;

import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.IcdCode;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.Recommendation;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

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
