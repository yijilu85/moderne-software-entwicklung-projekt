package com.medieninformatik.patientcare.userManagement.services;

import com.medieninformatik.patientcare.userManagement.domain.model.DTO.DoctorResponseDTO;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DoctorResponseDTO convertToDTO(Doctor doctor) {
        return new DoctorResponseDTO(
                doctor.getUserType().toString(),
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpeciality() != null ? doctor.getSpeciality().getLabel() : null, // Get the German label
                doctor.getEmail(),
                doctor.getDateOfBirth() != null ? doctor.getDateOfBirth().toString() : null,
                doctor.getPhoneNumber(),
                doctor.getStreet(),
                doctor.getHouseNumber(),
                doctor.getZipCode(),
                doctor.getCity(),
                doctor.getLicenseId(),
                doctor.getTitle() != null ? doctor.getTitle() : null
        );
    }
}
