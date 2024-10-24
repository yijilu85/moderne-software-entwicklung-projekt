package com.medieninformatik.patientcare.entities;

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
    }

    public Measurement(Type type, double value){
        this.type = type;
        this.value = value;
    }

}
