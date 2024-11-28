package com.medieninformatik.patientcare.userManagement.domain.model.shared;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name= "users")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "userType", visible = true)
@JsonSubTypes({
		@JsonSubTypes.Type(value = Patient.class, name = "PATIENT"),
		@JsonSubTypes.Type(value = Doctor.class, name = "DOCTOR")
})
public abstract class User {

	public enum UserType{
		PATIENT,
		DOCTOR
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "user_type", nullable = false)
	private UserType userType;

	@Column(name = "date_of_birth", nullable = true)
	private LocalDate dateOfBirth;

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

	public void setUsertype(UserType userType){
		this.userType = userType;
	}

	public UserType getUserType(){
		return this.userType;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}