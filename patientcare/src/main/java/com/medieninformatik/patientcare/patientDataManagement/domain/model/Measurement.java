package com.medieninformatik.patientcare.patientDataManagement.domain.model;

import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Measurement extends Note {

    private Type type;
    private double value;

    public Measurement() {
    }

    enum Type {
        BLOOD_SUGAR,
        BLOOD_PRESSURE,
        HEART_RATE,
        BODY_FAT,
        OXYGEN_BLOOD_SATURATION,
        BODY_TEMPERATURE,
        BODY_WEIGHT,
        BODY_HEIGHT,
    }

    public Measurement(Type type, double value){
        this.type = type;
        this.value = value;
    }

    private void setType (Type type){
        this.type = type;
    }

    private double getValue(){
        return this.value;
    }

    private void setValue (double value){
        this.value = value;
    }

}
