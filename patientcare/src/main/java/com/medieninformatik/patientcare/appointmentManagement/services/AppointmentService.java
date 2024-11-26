package com.medieninformatik.patientcare.appointmentManagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.appointmentManagement.infrastructure.repositories.AppointmentRepo;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private AppointmentRepo appointmentRepo;
    private UserRepo userRepo;

    public AppointmentService(AppointmentRepo appointmentRepo, UserRepo userRepo) {
        this.appointmentRepo = appointmentRepo;
        this.userRepo = userRepo;
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

    public Optional<Appointment> getAppointment(Long appointmentId) {
        return appointmentRepo.findById(appointmentId);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public List<Appointment> getAllAppointmentsForUser(Long userId) {
        Optional<User> foundUser = userRepo.findById(userId);
        User.UserType type = null;
        if (!foundUser.isEmpty()) {
            type = foundUser.get().getUserType();
        }
        List<Appointment> appointments = new ArrayList<>();
        if (type == User.UserType.PATIENT) {
            appointments = appointmentRepo.findByPatient(userId);
        } else if (type == User.UserType.DOCTOR) {
            appointments = appointmentRepo.findByDoctor(userId);
        }
        System.out.println("USER TYPE: " + type);
        return appointments;
    }

    ;

    public Appointment parseJSONCreateAppointmentSlot(String payload) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(payload);

        JsonNode patientNode = rootNode.get("patient");
        JsonNode doctorNode = rootNode.get("doctor");
        JsonNode creatorNode = rootNode.get("creator");

        if (doctorNode == null || creatorNode == null) {
            return null;
        }
        Optional<User> doctor = userRepo.findById(doctorNode.get("id").asLong());
        Optional<User> creator = userRepo.findById(creatorNode.get("id").asLong());

        if (doctor.isEmpty() || creator.isEmpty()) {
            return null;
        }

        Doctor doctorEntity = null;
        User creatorEntity = null;

        if (doctor.get() instanceof Doctor) {
            doctorEntity = (Doctor) doctor.get();
        } else {
            return null;
        }

        creatorEntity = creator.get();

        LocalDateTime startDate = LocalDateTime.parse(rootNode.get("startDateTime").asText());
        LocalDateTime endDate = LocalDateTime.parse(rootNode.get("endDateTime").asText());
        LocalDateTime createdAt = LocalDateTime.now();

        boolean valid = false;

        if (isDateInFuture(startDate) && !isEndAppointmentBeforeStartAppointment(startDate, endDate)) {
            valid = true;
        }

        if (valid) {
            Appointment appointment = createAppointmentSlot(doctorEntity, creatorEntity, startDate, endDate, createdAt);
            return appointmentRepo.save(appointment);
        } else {
            return null;
        }
    }

}
