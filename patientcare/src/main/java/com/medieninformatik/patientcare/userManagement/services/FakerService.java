package com.medieninformatik.patientcare.userManagement.services;

import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.Optional;

@Service
public class FakerService {

    private final DoctorRepo doctorRepo;

    @Autowired
    public FakerService(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    public void createDoctors() {
        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            Doctor doctor = new Doctor();
            doctor.setFirstName(faker.name().firstName());
            doctor.setLastName(faker.name().lastName());
//            doctor.set(faker.medical().department());
            doctorRepo.save(doctor);
        }
    }
}
