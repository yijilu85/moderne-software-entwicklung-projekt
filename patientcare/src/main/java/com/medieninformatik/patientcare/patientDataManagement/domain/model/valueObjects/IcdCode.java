package com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IcdCode {
    private String code;

    public IcdCode (String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
         this.code = code;
    }
}


