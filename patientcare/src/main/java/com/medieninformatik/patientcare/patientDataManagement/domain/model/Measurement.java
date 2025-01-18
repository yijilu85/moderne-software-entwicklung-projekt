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

    public enum Type {
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
        this.setNoteType(this.getClass().getSimpleName().toUpperCase());
    }

    public void setType (Type type){
        this.type = type;
    }

    public double getValue(){
        return this.value;
    }
    public void setValue (double value){
        this.value = value;
    }
}
