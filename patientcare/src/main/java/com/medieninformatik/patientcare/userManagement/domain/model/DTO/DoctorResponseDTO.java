package com.medieninformatik.patientcare.userManagement.domain.model.DTO;

import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.domain.model.valueObjects.MedicalSpeciality;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.medieninformatik.patientcare.userManagement.domain.model.shared.User.UserType.DOCTOR;

@Getter
@AllArgsConstructor
public class DoctorResponseDTO{

    private String userType;
    private Long id;
    private String firstName;
    private String lastName;
    private String speciality;
    private String email;
    private String dateOfBirth;
    private String phoneNumber;
    private String street;
    private String houseNumber;
    private String zipCode;
    private String city;
    private String licenseId;

    public DoctorResponseDTO(Doctor doctor) {
        this.userType = doctor.getUserType().toString();
        this.id = doctor.getId();
        this.firstName = doctor.getFirstName();
        this.lastName = doctor.getLastName();
        this.email = doctor.getEmail();
        this.dateOfBirth = doctor.getDateOfBirth().toString();
        this.phoneNumber = doctor.getPhoneNumber();
        this.street = doctor.getStreet();
        this.houseNumber = doctor.getHouseNumber();
        this.zipCode = doctor.getZipCode();
        this.city = doctor.getCity();
        this.licenseId = doctor.getLicenseId();
    }
}
