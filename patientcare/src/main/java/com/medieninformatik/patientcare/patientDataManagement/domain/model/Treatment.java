package com.medieninformatik.patientcare.patientDataManagement.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.IcdCode;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.Recommendation;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Treatment extends Note {

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonIgnore
    private User creator;

    private Date date;

    @Embedded
    private Recommendation recommendation;
    @Embedded
    private IcdCode icdCode;

    @Getter
    private String action;

    public Treatment() {
    }

    // Konstruktor
    public Treatment(String icdCode,
                     String recommendation, String action) {

        this.icdCode = new IcdCode(icdCode);
        this.recommendation = new Recommendation(recommendation);
        this.action = action;
        this.setNoteType(this.getClass().getSimpleName().toUpperCase());
    }

    // Getter und Setter (optional, falls du sie benötigst)

    public String getIcdCode() {
        return this.icdCode.getCode();
    }

    public String getRecommendation() {
        return this.recommendation.getText();
    }

}
