package com.medieninformatik.patientcare.entities;

import jakarta.persistence.*;

@Entity
public class Doctor
		extends User
{

// Default constructor
public Doctor()
{
}

// Constructor with parameters
public Doctor(String firstName, String lastName)
{
	this.setFirstName(firstName);
	this.setLastName(lastName);
}

@Override
public String toString()
{
	return "Doctor{" +
			"id=" + super.getId() +
			", firstName='" + super.getFirstName() + '\'' +
			", lastName='" + super.getLastName() + '\'' +
			'}';
}
}
