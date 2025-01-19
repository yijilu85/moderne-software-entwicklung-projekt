package com.medieninformatik.patientcare.patientDataManagement.infrastructure.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Diagnosis;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Measurement;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Treatment;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.NoteFile;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.repositories.NoteFileRepo;
import com.medieninformatik.patientcare.patientDataManagement.services.NoteService;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.util.*;


@RestController
@RequestMapping(path = "/notes")
public class NoteController {

    private final AppointmentService appointmentService;
    private final NoteService noteService;
    private final UserRepo userRepo;
    private final NoteFileRepo noteFileRepo;
    @Value("${upload.dir}")
    private String uploadDir;


    @Autowired
    public NoteController(AppointmentService appointmentService, NoteService noteService, UserRepo userRepo, NoteFileRepo noteFileRepo) {
        this.appointmentService = appointmentService;
        this.noteService = noteService;
        this.userRepo = userRepo;
        this.noteFileRepo = noteFileRepo;
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<?> addNoteToAppointment(@RequestBody String json) {
        System.out.println("JSON: " + json);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);

            // Common fields
            Long appointmentId = jsonNode.get("appointmentId").asLong();
            String noteType = jsonNode.get("noteType").asText();
            Long doctorId = jsonNode.get("doctorId").asLong();
            Long patientId = jsonNode.get("patientId").asLong();
            String creatorType = jsonNode.get("creator").asText();
            System.out.println("here 1: ");


            // Retrieve the appointment
            Optional<Appointment> optionalAppointment = appointmentService.getAppointment(appointmentId);
            if (optionalAppointment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
            }
            Appointment appointment = optionalAppointment.get();

            // Retrieve patient and doctor
            Optional<Patient> optionalPatient = noteService.getPatient(patientId);
            if (optionalPatient.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
            }
            Patient patient = optionalPatient.get();

            Optional<Doctor> optionalDoctor = noteService.getDoctor(doctorId);
            if (optionalDoctor.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
            }
            Doctor doctor = optionalDoctor.get();

            User creator = creatorType.equals("Doctor") ? doctor : patient;

            // Handle note type
            JsonNode payloadNode = jsonNode.get("payload"); // Retrieve payload node

            switch (noteType.toUpperCase()) {
                case "DIAGNOSIS":
                    if (!payloadNode.has("icdCode") || !payloadNode.has("recommendation")) {
                        return ResponseEntity.badRequest().body("Missing fields in payload for DIAGNOSIS.");
                    }
                    String icdCode = payloadNode.get("icdCode").asText();
                    String recommendation = payloadNode.get("recommendation").asText();
                    Diagnosis diagnosis = noteService.createDiagnosis(appointment.getPatient(), appointment.getDoctor(),
                            appointment,
                            new Date(),
                            icdCode,
                            recommendation);
                    noteService.addDiagnosisToAppointment(appointment, diagnosis);
                    break;

                case "MEASUREMENT":
                    if (!payloadNode.has("type") || !payloadNode.has("value")) {
                        return ResponseEntity.badRequest().body("Missing fields in payload for MEASUREMENT.");
                    }

                    String measurementTypeString = payloadNode.get("type").asText();
                    String valueString = payloadNode.get("value").asText();

                    double value;
                    try {
                        value = Double.parseDouble(valueString);
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body("Value must be a valid number.");
                    }

                    Measurement.Type measurementType;
                    try {
                        measurementType = Measurement.Type.valueOf(measurementTypeString.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("Invalid measurement type: " + measurementTypeString);
                    }

                    Measurement measurement = noteService.createMeasurement(patient, doctor, appointment, new Date(),
                            creator,
                            measurementType, value);
                    noteService.addMeasurementToAppointment(patient, doctor, appointment, measurement);
                    break;

                case "TREATMENT":
                    if (!payloadNode.has("diagnosis") || !payloadNode.get("diagnosis").has("icdCode") || !payloadNode.get("diagnosis").has("recommendation")) {
                        return ResponseEntity.badRequest().body("Missing fields in payload for TREATMENT.");
                    }
                    JsonNode diagnosisNode = payloadNode.get("diagnosis");
                    String treatmentIcdCode = diagnosisNode.get("icdCode").asText();
                    String treatmentRecommendation = diagnosisNode.get("recommendation").asText();

                    String action = payloadNode.get("action").asText();
                    Treatment treatment = noteService.createTreatment(patient, doctor, appointment, new Date(),
                            treatmentIcdCode, treatmentRecommendation, action);
                    noteService.addTreatmentToAppointment(appointment, treatment);
                    break;

                default:
                    return ResponseEntity.badRequest().body("Invalid note type.");
            }

            Appointment updatedAppointment = appointmentService.saveAppointment(appointment);

            return ResponseEntity.status(HttpStatus.CREATED).body(updatedAppointment);

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Transactional
    @CrossOrigin
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("description") String description,
                                        @RequestParam("appointmentId") Long appointmentId) {
        System.out.println("File received: " + file.getOriginalFilename());
        System.out.println("Content Type: " + file.getContentType());

        Optional<Appointment> optionalAppointment = appointmentService.getAppointment(appointmentId);
        if (optionalAppointment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        }
        Appointment appointment = optionalAppointment.get();

        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded.");
            }

            // Validate MIME type
            String mimeType = file.getContentType();
            if (!noteService.noteFileTypeIsValidMime(mimeType)) {
                return ResponseEntity.badRequest().body("Unsupported file type: " + mimeType);
            }

            // Define the upload directory
            File directory = new File(uploadDir);
            File absoluteDirectory = directory.getAbsoluteFile();
            System.out.println("Absolute directory path: " + absoluteDirectory.getAbsolutePath());
            if (!absoluteDirectory.exists()) {
                if (!absoluteDirectory.mkdirs()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to create upload directory.");
                }
            }

            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFileName;
            File targetFile = new File(absoluteDirectory, fileName);

            System.out.println("Target file path: " + targetFile.getAbsolutePath());

            file.transferTo(targetFile);

            String relativeUrl = "/uploads/" + fileName;

            NoteFile noteFile = noteService.createNoteFile(appointment.getPatient(), appointment.getDoctor(),
                    appointment,
                    relativeUrl,
                    description,
                    mimeType);
            noteService.addNoteFileToAppointment(appointment, noteFile);
            appointmentService.saveAppointment(appointment);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("File uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }
        @CrossOrigin
        @GetMapping("/download/{id}")
        public ResponseEntity<Resource> serveFile(@PathVariable Long id) {
            Optional<NoteFile> optionalNoteFile = noteFileRepo.findById(id);

            if (optionalNoteFile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            NoteFile noteFile = optionalNoteFile.get();
            String relativeUrl = noteFile.getUrl();
            String fileName = relativeUrl.substring(relativeUrl.lastIndexOf("/") + 1);
            File file = new File(uploadDir, fileName);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new FileSystemResource(file);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        }
}