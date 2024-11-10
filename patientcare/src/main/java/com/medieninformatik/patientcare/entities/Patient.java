package com.medieninformatik.patientcare.entities;

import jakarta.persistence.*;

import java.util.List;

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
