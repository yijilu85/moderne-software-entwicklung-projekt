package com.medieninformatik.patientcare.appointmentManagement.infrastructure.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.services.DoctorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@RestController
@RequestMapping(path = "/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @CrossOrigin
    @GetMapping
    public List<Appointment> findAllAppointments(@RequestParam(required = false) Long userId) {
        if (userId == null) {
            return appointmentService.getAllAppointments();
        } else {
            return appointmentService.getAllAppointmentsForUser(userId);
        }
    }

    @CrossOrigin
    @GetMapping(path = "/{id}")
    public Optional<Appointment> findAppointment(@PathVariable("id") Long id) {
        return appointmentService.getAppointment(id);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id, @RequestParam Long userId) {
        Optional<Appointment> appointment = appointmentService.getAppointment(id);

        if (appointment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Termin nicht gefunden
        }

        // Prüfen, ob der Benutzer der Arzt ist, der den Termin erstellt hat
        if (!appointment.get().getDoctor().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Zugriff verweigert
        }

        try {
            appointmentService.deleteAppointment(id, userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Allgemeiner Fehler
        }
    }

    @CrossOrigin
    @PostMapping
    public Appointment createAppointmentSlot(@RequestBody String json) throws JsonProcessingException {
        return appointmentService.parseJSONCreateAppointmentSlot(json);
    }

    @CrossOrigin
    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id, @RequestParam Long userId) {
        Optional<Appointment> appointmentOpt = appointmentService.getAppointment(id);

        if (appointmentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Termin nicht gefunden");
        }

        Appointment appointment = appointmentOpt.get();

        // Überprüfung: Ist der Benutzer Arzt oder Patient dieses Termins?
        if (!appointment.getDoctor().getId().equals(userId) &&
                (appointment.getPatient() == null || !appointment.getPatient().getId().equals(userId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Keine Berechtigung, diesen Termin zu stornieren");
        }

        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.ok("Termin erfolgreich storniert");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Stornieren des Termins");
        }
    }

    @CrossOrigin
    @PostMapping(path = "/book")
    public Appointment bookAppointment(@RequestBody String json) throws JsonProcessingException {
        return appointmentService.parseJSONBookAppointmentSlot(json);
    }

}
