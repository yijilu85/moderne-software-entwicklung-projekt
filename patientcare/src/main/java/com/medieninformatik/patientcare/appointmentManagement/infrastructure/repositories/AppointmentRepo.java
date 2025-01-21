package com.medieninformatik.patientcare.appointmentManagement.infrastructure.repositories;

import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :userId")
    List<Appointment> findByPatient(@Param("userId") Long userId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :userId")
    List<Appointment> findByDoctor(@Param("userId") Long userId);
}