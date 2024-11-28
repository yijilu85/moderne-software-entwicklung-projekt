package com.medieninformatik.patientcare.userManagement.services;

import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.DoctorRepo;
import com.medieninformatik.patientcare.userManagement.domain.model.valueObjects.MedicalSpeciality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.javafaker.Faker;
import java.util.Random;


@Service
public class FakerService {

    private final DoctorRepo doctorRepo;

    @Autowired
    public FakerService(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    public void createDoctors(int amount) {
        Faker faker = new Faker();
        Random random = new Random();
        MedicalSpeciality.Speciality[] specialities = MedicalSpeciality.Speciality.values();
        String[] cities = {"Berlin", "Hamburg", "Munich", "Cologne", "Frankfurt", "Stuttgart"};

        for (int i = 0; i < amount; i++) {
            Doctor doctor = new Doctor();
            doctor.setFirstName(faker.name().firstName());
            doctor.setLastName(faker.name().lastName());
            doctor.setEmail(faker.internet().emailAddress());
            doctor.setPassword("1234567890");
            doctor.setPhoneNumber(faker.phoneNumber().phoneNumber());
            doctor.setStreet(faker.address().streetAddress());
            doctor.setHouseNumber(faker.address().buildingNumber());
            doctor.setZipCode(faker.number().digits(5));
            doctor.setLicenseId("A" + faker.number().digits(5) + "-D" + faker.number().digits(5));
            doctor.setCity(cities[random.nextInt(cities.length)]);
            MedicalSpeciality.Speciality speciality = specialities[random.nextInt(specialities.length)];
            doctor.setSpeciality(speciality);
            doctor.setUsertype(Doctor.UserType.DOCTOR);
            doctorRepo.save(doctor);
        }
    }
}
