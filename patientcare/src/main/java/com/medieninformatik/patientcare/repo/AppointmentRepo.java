package com.medieninformatik.patientcare.repo;

import com.medieninformatik.patientcare.entities.Appointment;
import com.medieninformatik.patientcare.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long>{
}