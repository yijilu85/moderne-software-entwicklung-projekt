package com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Recommendation {

    private String text;

    public Recommendation() {}

    public Recommendation(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}