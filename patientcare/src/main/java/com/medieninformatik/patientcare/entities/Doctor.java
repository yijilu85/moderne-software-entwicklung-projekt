package com.medieninformatik.patientcare.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor
		extends User
{

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID
private Long id;

@Column(name = "first_name", nullable = false)
private String firstName;

@Column(name = "last_name", nullable = false)
private String lastName;

// Default constructor
public Doctor()
{
}

// Constructor with parameters
public Doctor(String firstName, String lastName)
{
	this.firstName = firstName;
	this.lastName = lastName;
}

// Getter and Setter for 'id'
public Long getId()
{
	return id;
}

public void setId(Long id)
{
	this.id = id;
}

// Getter and Setter for 'firstName'
public String getFirstName()
{
	return firstName;
}

public void setFirstName(String firstName)
{
	this.firstName = firstName;
}

// Getter and Setter for 'lastName'
public String getLastName()
{
	return lastName;
}

public void setLastName(String lastName)
{
	this.lastName = lastName;
}

@Override
public String toString()
{
	return "Doctor{" +
			"id=" + id +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			'}';
}
}
