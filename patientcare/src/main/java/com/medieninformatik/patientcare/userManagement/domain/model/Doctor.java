package com.medieninformatik.patientcare.userManagement.domain.model;

import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.domain.model.valueObjects.MedicalSpeciality;
import jakarta.persistence.*;

import static com.medieninformatik.patientcare.userManagement.domain.model.shared.User.UserType.DOCTOR;

@Entity
public class Doctor
        extends User {

    private MedicalSpeciality.Speciality speciality;
    private String phoneNumber;
    private String street;
    private String houseNumber;
    private String zipCode;
    private String city;
    private String licenseId;
    private String title;

    // Default constructor
    public Doctor() {
    }

    // Constructor with parameters
    public Doctor(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUsertype(DOCTOR);
    }

    // Getter and Setter for 'speciality'
    public MedicalSpeciality.Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(MedicalSpeciality.Speciality speciality) {
        this.speciality = speciality;
    }

    // Getter and Setter for 'phoneNumber'
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter and Setter for 'street'
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    // Getter and Setter for 'houseNumber'
    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    // Getter and Setter for 'zipCode'
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    // Getter and Setter for 'city'
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // Getter and Setter for 'licenseId'
    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
     return "Doctor{" +
             "id=" + super.getId() +
             ", title='" + title + '\'' +
             ", firstName='" + super.getFirstName() + '\'' +
             ", lastName='" + super.getLastName() + '\'' +
             ", speciality='" + speciality + '\'' +
             ", phoneNumber='" + phoneNumber + '\'' +
             ", street='" + street + '\'' +
             ", houseNumber='" + houseNumber + '\'' +
             ", zipCode='" + zipCode + '\'' +
             ", city='" + city + '\'' +
             ", licenseId='" + licenseId + '\'' +
             '}';
    }

}
