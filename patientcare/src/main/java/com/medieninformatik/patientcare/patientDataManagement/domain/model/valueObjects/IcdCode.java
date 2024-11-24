package com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IcdCode {
    private String icdCode;

    public IcdCode (String icdCode){
        this.icdCode = icdCode;
    }
}


