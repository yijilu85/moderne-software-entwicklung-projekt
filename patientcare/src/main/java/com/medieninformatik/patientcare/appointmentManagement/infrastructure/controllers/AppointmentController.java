package com.medieninformatik.patientcare.appointmentManagement.infrastructure.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping(path = "/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepo userRepo;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, UserRepo userRepo) {
        this.appointmentService = appointmentService;
        this.userRepo = userRepo;
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
    @GetMapping(path = "/timeranges")
    public Map<String, List<Appointment>> findAllAppointmentsWithTimeranges(@RequestParam(required = false) Long userId) {
        return appointmentService.getAllAppointmentsForUserWithTimeranges(userId);
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
    @PostMapping("/cancel/")
    public ResponseEntity<String> cancelAppointment(@RequestBody String json) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        Optional<User> user = userRepo.findById(rootNode.get("userId").asLong());


        Long appointmentId = rootNode.get("appointmentId").asLong();

        if (!(user.get() instanceof User)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Termin nicht gefunden");
        }


        Optional<Appointment> appointmentOpt = appointmentService.getAppointment(appointmentId);

        if (appointmentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Termin nicht gefunden");
        }

        User userEntity = user.get();
        Appointment appointment = appointmentOpt.get();


        if (!(appointment.getDoctor().getId().equals(userEntity.getId()) || appointment.getPatient().getId().equals(userEntity.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Keine Berechtigung, diesen Termin zu stornieren");
        }

        try {
            appointmentService.cancelAppointment(appointment.getId());
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
