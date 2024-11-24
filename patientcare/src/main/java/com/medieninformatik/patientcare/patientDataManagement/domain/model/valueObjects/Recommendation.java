package com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recommendation {
private String text;

    public Recommendation (String text){
        this.text = text;
    }
}
