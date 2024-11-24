package com.medieninformatik.patientcare.appointmentManagement.infrastructure.repositories;

import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long>{
}