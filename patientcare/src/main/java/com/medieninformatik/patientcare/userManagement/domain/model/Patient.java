package com.medieninformatik.patientcare.userManagement.domain.model;

import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import jakarta.persistence.*;

import java.util.List;

import static com.medieninformatik.patientcare.userManagement.domain.model.shared.User.UserType.PATIENT;

@Entity
public class Patient extends User {

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Note> notes;

    // Default constructor
    public Patient() {
    }

// Constructor with parameters
public Patient(String firstName, String lastName)
{
	this.setFirstName(firstName);
	this.setLastName(lastName);
	this.setUsertype(PATIENT);
}

@Override
public String toString()
{
	return "Patient{" +
			"id=" + super.getId() +
			", firstName='" + super.getFirstName() + '\'' +
			", lastName='" + super.getLastName() + '\'' +
			'}';
}
}
