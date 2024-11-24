package com.medieninformatik.patientcare.patientDataManagement.infrastructure.repositories;

import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepo extends JpaRepository<Patient, Long>{
}