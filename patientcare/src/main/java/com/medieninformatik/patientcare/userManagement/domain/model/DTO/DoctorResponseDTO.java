package com.medieninformatik.patientcare.userManagement.domain.model.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorResponseDTO {

    @JsonProperty("userType")
    private String userType;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("speciality")
    private String speciality;

    @JsonProperty("email")
    private String email;

    @JsonProperty("dateOfBirth")
    private String dateOfBirth;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("street")
    private String street;

    @JsonProperty("houseNumber")
    private String houseNumber;

    @JsonProperty("zipCode")
    private String zipCode;

    @JsonProperty("city")
    private String city;

    @JsonProperty("licenseId")
    private String licenseId;

    @JsonProperty("title")
    private String title;

    @JsonCreator
    public DoctorResponseDTO(Doctor doctor) {
        this.userType = doctor.getUserType().toString();
        this.id = doctor.getId();
        this.firstName = doctor.getFirstName();
        this.lastName = doctor.getLastName();
        this.email = doctor.getEmail();
        this.dateOfBirth = doctor.getDateOfBirth() != null ? doctor.getDateOfBirth().toString() : null;
        this.phoneNumber = doctor.getPhoneNumber();
        this.street = doctor.getStreet();
        this.houseNumber = doctor.getHouseNumber();
        this.zipCode = doctor.getZipCode();
        this.city = doctor.getCity();
        this.licenseId = doctor.getLicenseId();
        this.title = doctor.getTitle() != null ? doctor.getTitle() : null;
        this.speciality = doctor.getSpeciality() != null ? doctor.getSpeciality().getLabel() : null;
    }
}
