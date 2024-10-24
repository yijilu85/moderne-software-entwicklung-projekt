package com.medieninformatik.patientcare.services;

import com.medieninformatik.patientcare.entities.Patient;
import com.medieninformatik.patientcare.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepo patientRepo;

    @Autowired
    public PatientService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public Optional<Patient> getPatient(Long personId) {
        return patientRepo.findById(personId);
    }
}
