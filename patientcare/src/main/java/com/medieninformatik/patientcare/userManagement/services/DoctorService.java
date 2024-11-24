package com.medieninformatik.patientcare.userManagement.services;

import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorService {

private final DoctorRepo doctorRepo;

@Autowired
public DoctorService(DoctorRepo doctorRepo) {
	this.doctorRepo = doctorRepo;
}

public Optional<Doctor> getDoctor(Long personId) {
	return doctorRepo.findById(personId);
}
}
