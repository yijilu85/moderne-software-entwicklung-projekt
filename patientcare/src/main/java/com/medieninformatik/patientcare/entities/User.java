package com.medieninformatik.patientcare.entities;

import jakarta.persistence.*;

public abstract class User {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID
private Long id;

@Column(name = "first_name", nullable = false)
private String firstName;

@Column(name = "last_name", nullable = false)
private String lastName;

// Getter and Setter for 'id'
public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

// Getter and Setter for 'firstName'
public String getFirstName() {
	return firstName;
}

public void setFirstName(String firstName) {
	this.firstName = firstName;
}

// Getter and Setter for 'lastName'
public String getLastName() {
	return lastName;
}

public void setLastName(String lastName) {
	this.lastName = lastName;
}


}
