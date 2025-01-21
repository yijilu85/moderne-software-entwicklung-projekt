package com.medieninformatik.patientcare.userManagement.domain.model.valueObjects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class lastName {
    private String lastName;

    public lastName(String firstName) {
        this.lastName = firstName;
    }
}

