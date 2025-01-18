package com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Recommendation {

    private String text;

    // No-argument constructor required by JPA
    public Recommendation() {}

    // Parameterized constructor
    public Recommendation(String text) {
        this.text = text;
    }
}