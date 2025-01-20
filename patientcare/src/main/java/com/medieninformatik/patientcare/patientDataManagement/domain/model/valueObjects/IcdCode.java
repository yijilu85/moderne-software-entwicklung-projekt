package com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class IcdCode {

    private String code;

    // Default no-argument constructor
    public IcdCode() {
    }

    // Constructor to initialize the code
    public IcdCode(String code) {
        this.code = code;
    }

    // Getter and Setter
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "IcdCode{" +
                "code='" + code + '\'' +
                '}';
    }
}

