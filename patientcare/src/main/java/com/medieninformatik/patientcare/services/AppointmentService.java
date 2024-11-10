package com.medieninformatik.patientcare.services;

import com.medieninformatik.patientcare.entities.*;
import com.medieninformatik.patientcare.repo.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AppointmentService {

    private AppointmentRepo appointmentRepo;

    public AppointmentService(AppointmentRepo appointmentRepo){
        this.appointmentRepo = appointmentRepo;
    }

    @Transactional
    public void scheduleAppointment(Appointment appointment, Patient patient, Appointment.Type type) {
        appointment.setPatient(patient);
        appointment.setType(type);
        appointmentRepo.save(appointment);
    }

    public Appointment createAppointmentSlot(Doctor doctor, User creator, LocalDateTime startDateTime,
                                      LocalDateTime endDateTime
            , LocalDateTime createdAt) {
        Appointment appointment = new Appointment(doctor, creator, startDateTime, endDateTime, createdAt);
        appointmentRepo.save(appointment);
        System.out.println(appointment.toString());

        return appointment;
    }

    public void editAppointment() {
    }

    public void addNote(Appointment appointment, Note note) {
        appointment.addNote(note);
    }

    public void removeSingleNote(Appointment appointment, Note note) {
        appointment.removeSingleNote(note);
    }

    public void removeAllNotes(Appointment appointment) {
        appointment.removeAllNotes();
    }

    public void sendAppoinmentReminder() {
    }

    public boolean isStartAppointmentBeforeToEndAppointment(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return endDateTime.isAfter(startDateTime);
    }

    public boolean isEndAppointmentBeforeStartAppointment(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return !endDateTime.isAfter(startDateTime);
    }

    public boolean isDateInFuture(LocalDateTime startDateTime) {
        return startDateTime.isAfter(LocalDateTime.now());
    }

}
