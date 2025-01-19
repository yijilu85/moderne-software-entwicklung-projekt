package com.medieninformatik.patientcare.appointmentManagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.appointmentManagement.infrastructure.repositories.AppointmentRepo;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import com.medieninformatik.patientcare.shared.services.HelperService;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.sql.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private AppointmentRepo appointmentRepo;
    private UserRepo userRepo;
    private HelperService helperService;


    public AppointmentService(AppointmentRepo appointmentRepo, UserRepo userRepo, HelperService helperService) {
        this.appointmentRepo = appointmentRepo;
        this.userRepo = userRepo;
        this.helperService = helperService;
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

    public void deleteAppointment(Long appointmentId, Long userId) {
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if (appointment.isPresent()) {
            appointmentRepo.delete(appointment.get());
        } else {
            throw new EntityNotFoundException("Termin nicht gefunden mit ID: " + appointmentId);
        }
    }

    @Transactional
    public Appointment cancelAppointment(Long appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepo.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new EntityNotFoundException("Termin mit ID " + appointmentId + " nicht gefunden");
        }

        Appointment appointment = appointmentOpt.get();

        // Entferne den Patienten aus dem Termin
        /*appointment.setPatient(null);*/
        appointment.clearPatient();
        appointment.removeAllNotes();
        appointmentRepo.save(appointment);

        return appointment;
    }

    public void addNote(Appointment appointment, Note note) {
        System.out.println("Note: " + note.toString());
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

    public boolean belongsToPatient(Appointment appointment, Long patientId) {
        return appointment.getPatient().getId().equals(patientId);
    }

    public boolean belongsToDoctor(Appointment appointment, Long doctorId) {
        return appointment.getDoctor().getId().equals(doctorId);
    }

    private final AppointmentValidator pastValidator = appointment ->
        appointment.getStartDateTime().toLocalDate().isBefore(LocalDateTime.now().toLocalDate());

    private final AppointmentValidator todayValidator = appointment -> appointment.getStartDateTime().toLocalDate().isEqual(LocalDateTime.now().toLocalDate());

    private final AppointmentValidator futureValidator = appointment ->
            appointment.getStartDateTime().toLocalDate().isAfter(LocalDateTime.now().toLocalDate());


    public Map<String, List<Appointment>> getAllAppointmentsForUserWithTimeranges(Long userId) {
        List<Appointment> allAppointments = appointmentRepo.findAll();
        Optional<User> user = userRepo.findById(userId);

        if (user.isEmpty()) {
            throw new EntityNotFoundException("User mit ID " + userId + " nicht gefunden");
        }

        AppointmentValidator userValidator = appointment -> {
            User.UserType type = user.get().getUserType();
            if (type.equals(User.UserType.PATIENT)) {
                return belongsToPatient(appointment, userId);
            } else if (type.equals(User.UserType.DOCTOR)) {
                return belongsToDoctor(appointment, userId);
            }
            return false;
        };

        return allAppointments.stream()
                .filter(userValidator::isValid)
                .collect(Collectors.groupingBy(appointment -> {
                    if (pastValidator.isValid(appointment)) {
                        return "past";
                    } else if (todayValidator.isValid(appointment)) {
                        return "today";
                    } else if (futureValidator.isValid(appointment)) {
                        return "future";
                    } else {
                        throw new IllegalStateException("Invalid time range for appointment");
                    }
                }));
    }

    public Appointment parseJSONBookAppointmentSlot(String payload) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(payload);

        Long patientId = rootNode.get("patientId").asLong();
        Long appointmentid = rootNode.get("appointmentId").asLong();

        Optional<User> patient = userRepo.findById(patientId);
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentid);

        if (patient != null && appointment != null) {
            appointment.get().setPatient((Patient) patient.get());
            appointmentRepo.save(appointment.get());
            return appointment.get();
        } else {
            return null;
        }
    }

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepo.save(appointment);
    }

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

        LocalDateTime startDate = helperService.parseDateFromJSON(rootNode.get("startDateTime").asText());
        LocalDateTime endDate = helperService.parseDateFromJSON(rootNode.get("endDateTime").asText());
        LocalDateTime createdAt = LocalDateTime.now();

        boolean valid = false;

        if (isDateInFuture(startDate) && !isEndAppointmentBeforeStartAppointment(startDate, endDate)) {
            valid = true;
        }

        if (valid) {
            Appointment appointment = createAppointmentSlot(doctorEntity, creatorEntity, startDate, endDate, createdAt);

            Appointment.Type type = Appointment.Type.valueOf(rootNode.get("type").asText());
            if (type != null) {
                appointment.setType(type);
            }

            String title = rootNode.get("title").asText();
            if (title != null) {
                appointment.setTitle(title);
            }

            appointmentRepo.save(appointment);
            System.out.println(appointment.toString());
            return appointment;
        } else {
            return null;
        }
    }


}
