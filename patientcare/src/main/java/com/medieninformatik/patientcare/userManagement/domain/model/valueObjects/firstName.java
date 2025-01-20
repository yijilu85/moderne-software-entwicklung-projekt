package com.medieninformatik.patientcare.userManagement.domain.model.valueObjects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class firstName {
    private String firstName;

    public firstName(String firstName) {
        this.firstName = firstName;
    }
}

