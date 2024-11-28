package com.medieninformatik.patientcare.userManagement.domain.model;

import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import jakarta.persistence.*;

import static com.medieninformatik.patientcare.userManagement.domain.model.shared.User.UserType.DOCTOR;

@Entity
public class Doctor
        extends User {

    // Default constructor
    public Doctor() {
    }

    // Constructor with parameters
    public Doctor(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUsertype(DOCTOR);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + super.getId() +
                ", firstName='" + super.getFirstName() + '\'' +
                ", lastName='" + super.getLastName() + '\'' +
                '}';
    }

}
