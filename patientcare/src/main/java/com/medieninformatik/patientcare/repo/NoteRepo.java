package com.medieninformatik.patientcare.repo;

import com.medieninformatik.patientcare.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepo extends JpaRepository<Patient, Long>{
}