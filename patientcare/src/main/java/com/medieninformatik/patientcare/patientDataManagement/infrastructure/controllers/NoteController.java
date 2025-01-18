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
import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.valueObjects.File;
import com.medieninformatik.patientcare.patientDataManagement.services.NoteService;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping(path = "/notes")
public class NoteController {

    private final AppointmentService appointmentService;
    private final NoteService noteService;
    private final UserRepo userRepo;

    @Autowired
    public NoteController(AppointmentService appointmentService, NoteService noteService, UserRepo userRepo) {
        this.appointmentService = appointmentService;
        this.noteService = noteService;
        this.userRepo = userRepo;
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
                    // Validate presence of required fields in payload
                    if (!payloadNode.has("type") || !payloadNode.has("value")) {
                        return ResponseEntity.badRequest().body("Missing fields in payload for MEASUREMENT.");
                    }

                    // Get the measurement type and value
                    String measurementTypeString = payloadNode.get("type").asText();
                    String valueString = payloadNode.get("value").asText();

                    // Ensure that the value is a valid number
                    double value;
                    try {
                        value = Double.parseDouble(valueString); // Using Double.parseDouble for better error handling
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body("Value must be a valid number.");
                    }

                    // Convert the string to the Measurement.Type enum
                    Measurement.Type measurementType;
                    try {
                        measurementType = Measurement.Type.valueOf(measurementTypeString.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("Invalid measurement type: " + measurementTypeString);
                    }

                    // Create the measurement using the NoteService
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

                case "FILE":
                    if (!payloadNode.has("files")) {
                        return ResponseEntity.badRequest().body("Missing files array in payload for FILE.");
                    }
                    ArrayNode filesNode = (ArrayNode) payloadNode.get("files");
                    List<File> files = new ArrayList<>();
                    for (JsonNode fileNode : filesNode) {
                        if (!fileNode.has("fileName") || !fileNode.has("mimeType") || !fileNode.has("fileData")) {
                            return ResponseEntity.badRequest().body("Missing fields in file node.");
                        }
                        String fileName = fileNode.get("fileName").asText();
                        String mimeType = fileNode.get("mimeType").asText();
                        String base64FileData = fileNode.get("fileData").asText(); // Assuming file data is sent as a base64 string

                        // Decode the base64 string to byte array
                        byte[] fileData = Base64.getDecoder().decode(base64FileData);

                        if (!noteService.noteFileTypeIsValidMime(mimeType)) {
                            return ResponseEntity.badRequest().body("Unsupported file type: " + mimeType);
                        }

                        // Create the File instance
                        File file = noteService.createFile(mimeType, fileData, fileNode.get("description").asText());
                        files.add(file);
                    }
                    noteService.addFilesNote(patient, doctor, appointment, files);
                    break;

                default:
                    return ResponseEntity.badRequest().body("Invalid note type.");
            }

            // Save the updated appointment
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

    @CrossOrigin
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("appointmentId") Long appointmentId,
                                        @RequestParam("doctorId") Long doctorId,
                                        @RequestParam("patientId") Long patientId) {
        try {
            // Validate the file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded.");
            }

            // Validate appointment
            Optional<Appointment> optionalAppointment = appointmentService.getAppointment(appointmentId);
            if (optionalAppointment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
            }
            Appointment appointment = optionalAppointment.get();

            // Validate doctor
            Optional<Doctor> optionalDoctor = noteService.getDoctor(doctorId);
            if (optionalDoctor.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
            }
            Doctor doctor = optionalDoctor.get();

            // Validate patient
            Optional<Patient> optionalPatient = noteService.getPatient(patientId);
            if (optionalPatient.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
            }
            Patient patient = optionalPatient.get();

            // Check file type validity
            String mimeType = file.getContentType();
            if (!noteService.noteFileTypeIsValidMime(mimeType)) {
                return ResponseEntity.badRequest().body("Unsupported file type: " + mimeType);
            }

            // Create a File instance
            File noteFile = noteService.createFile(mimeType, file.getBytes(), file.getOriginalFilename());

            // Add the file note
            noteService.addFilesNote(patient, doctor, appointment, Collections.singletonList(noteFile));

            // Prepare a response (you can customize what you want to return)
            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}