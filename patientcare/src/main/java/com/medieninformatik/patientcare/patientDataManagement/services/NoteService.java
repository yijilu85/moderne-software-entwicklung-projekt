package com.medieninformatik.patientcare.patientDataManagement.services;

import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;


import com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Diagnosis;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Measurement;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Treatment;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.shared.Note;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.NoteFile;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.repositories.NoteRepo;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.DoctorRepo;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.PatientRepo;
import com.medieninformatik.patientcare.shared.services.HelperService;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.util.regex.Pattern;

@Service
public class NoteService {

    private PatientRepo patientRepo;
    private NoteRepo noteRepo;
    private AppointmentService appointmentService;
    private DoctorRepo doctorRepo;
    private HelperService helperService;
    private static final Set<String> VALID_MIME_TYPES = Set.of(
            "text/plain",
            "application/pdf",
            "image/jpg",
            "image/jpeg",
            "image/png",
            "video/mp4",
            "audio/mpeg"
    );

    public NoteService(PatientRepo patientRepo, HelperService helperService, AppointmentService appointmentService,
                       NoteRepo noteRepo,
                       DoctorRepo doctorRepo) {
        this.patientRepo = patientRepo;
        this.helperService = helperService;
        this.appointmentService = appointmentService;
        this.noteRepo = noteRepo;
        this.doctorRepo = doctorRepo;
    }

    public Optional<Patient> getPatient(Long personId) {
        return patientRepo.findById(personId);
    }

    public Optional<Doctor> getDoctor(Long personId) {
        return doctorRepo.findById(personId);
    }

    public Note addFilesNote(Patient patient, Doctor doctor, Appointment appointment) {

        Note note = new Note(patient, doctor, appointment);

        return note;
    }
    public Diagnosis createDiagnosis(Patient patient, Doctor doctor, Appointment appointment, Date date,
                                     String icdCode, String recommendation) {
        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Patient and Doctor must not be null when creating a Diagnosis.");
        }

        System.out.println("Creating Diagnosis for Patient ID: " + patient.getId() + ", Doctor ID: " + doctor.getId());

        Diagnosis diagnosis =  new Diagnosis(icdCode, recommendation);
        diagnosis.setPatient(patient);
        diagnosis.setDoctor(doctor);
        diagnosis.setAppointment(appointment);
        diagnosis.setTimestamp(date);
        return diagnosis;
    }

    public Treatment createTreatment(Patient patient, Doctor doctor, Appointment appointment, Date date,
                                     String icdCode,String recommendation,
                                     String action) {
        if (patient == null || doctor == null || date == null || icdCode == null || icdCode == null || action == null) {
            throw new IllegalArgumentException("Keiner der Eingabewerte darf null sein.");
        }

        Treatment treatment = new Treatment(icdCode, recommendation, action);
        treatment.setPatient(patient);
        treatment.setDoctor(doctor);
        treatment.setAppointment(appointment); // Set the appointment
        treatment.setTimestamp(date);

        return treatment;
    }

    public Measurement createMeasurement(Patient patient, Doctor doctor, Appointment appointment, Date date, User creator,
                                         Measurement.Type type, double value) {
        if (patient == null || doctor == null || appointment == null || date == null || creator == null || type == null) {
            throw new IllegalArgumentException("None of the input values can be null.");
        }

        Measurement measurement = new Measurement(type, value);
        measurement.setPatient(patient);
        measurement.setDoctor(doctor);
        measurement.setAppointment(appointment); // Set the appointment
        measurement.setTimestamp(date);

        return measurement; // Save it to the database where needed
    }

    public NoteFile createNoteFile(Patient patient, Doctor doctor, Appointment appointment, String url,
                                   String description, String mimeType) {
        if (mimeType == null || url == null || description == null) {
            throw new IllegalArgumentException("Mime type, url and data must not be null when creating a NoteFile.");
        }

        if (!VALID_MIME_TYPES.contains(mimeType)) {
            throw new IllegalArgumentException("Invalid mime type: " + mimeType);
        }

        NoteFile noteFile = new NoteFile(url, description, mimeType);
        noteFile.setPatient(patient);
        noteFile.setDoctor(doctor);
        noteFile.setAppointment(appointment);
        noteFile.setTimestamp(new Date());

        return noteFile;
    }

    public boolean isValidIcdCode(String icdCode) {
        // Muster für ICD-10 (Beispiel: J45.0) und ICD-11 (Beispiel: 5A11)
        String icd10Pattern = "^[A-Z][0-9]{2}\\.[0-9]$"; // Beispiel: J45.0
        String icd11Pattern = "^[0-9A-Z]{1,4}$";        // Beispiel: 5A11

        return Pattern.matches(icd10Pattern, icdCode) || Pattern.matches(icd11Pattern, icdCode);
    }

    public boolean actionHasMinLength(String action) {
        if (action == null) {
            return false;
        }
        return action.length() >= 10;
    }

    public void addDiagnosisToAppointment(Appointment appointment, Diagnosis diagnosis) {
        if (appointment == null || diagnosis == null) {
            throw new IllegalArgumentException("Appointment und Diagnosis dürfen nicht null sein.");
        }

        if (!this.noteUsersEqualsAppointmentUsers(appointment, diagnosis)) {
            throw new IllegalArgumentException("Die Benutzer des Termins stimmen nicht mit denen der Notiz überein.");
        }

        appointmentService.addNote(appointment, diagnosis);
    }

    public void addMeasurementToAppointment(Patient patient, Doctor doctor, Appointment appointment,
                                            Measurement measurement) {
        if (appointment == null || measurement == null) {
            throw new IllegalArgumentException("Appointment and Measurement must not be null.");
        }

        measurement.setAppointment(appointment);
        appointmentService.addNote(appointment, measurement);
        noteRepo.save(measurement);
    }

    public void addTreatmentToAppointment(Appointment appointment, Treatment treatment) {
        if (appointment == null || treatment == null) {
            throw new IllegalArgumentException("Appointment und Measurement dürfen nicht null sein.");
        }
        appointmentService.addNote(appointment, treatment);
    }

    public void addNoteFileToAppointment(Appointment appointment, NoteFile notefile) {
        if (appointment == null || notefile == null) {
            throw new IllegalArgumentException("Appointment und NoteFile dürfen nicht null sein.");
        }

        appointmentService.addNote(appointment, notefile);
    }

    public boolean noteFileTypeIsValidMime(String mimeType) {
        return this.VALID_MIME_TYPES.contains(mimeType);
    }

    public boolean noteUsersEqualsAppointmentUsers(Appointment appointment, Diagnosis diagnosis) {
        // Validierung: Falls appointment oder diagnosis null ist, gib false zurück
        if (appointment == null || diagnosis == null) {
            return false;
        }

        Patient appointmentPatient = appointment.getPatient();
        Doctor appointmentDoctor = appointment.getDoctor();
        Patient notePatient = diagnosis.getPatient();
        Doctor noteDoctor = diagnosis.getDoctor();

        // Debugging Logs
        System.out.println("Appointment Patient ID: " + (appointmentPatient != null ? appointmentPatient.getId() : "null"));
        System.out.println("Note Patient ID: " + (notePatient != null ? notePatient.getId() : "null"));
        System.out.println("Appointment Doctor ID: " + (appointmentDoctor != null ? appointmentDoctor.getId() : "null"));
        System.out.println("Note Doctor ID: " + (noteDoctor != null ? noteDoctor.getId() : "null"));

        // Überprüfen, ob der Patient und der Arzt in der Diagnose mit denen des Termins übereinstimmen
        boolean patientMatches = appointment.getPatient() != null &&
                diagnosis.getPatient() != null &&
                appointment.getPatient().getId().equals(diagnosis.getPatient().getId());

        boolean doctorMatches = appointment.getDoctor() != null &&
                diagnosis.getDoctor() != null &&
                appointment.getDoctor().getId().equals(diagnosis.getDoctor().getId());

        // Rückgabe true, wenn sowohl Patient als auch Arzt übereinstimmen, sonst false
        return patientMatches && doctorMatches;
    }

    public List<Note> getAllNotes(Appointment appointment) {
        return appointment.getNotes();
    }

    public NoteFile createFile(String mimeType, byte[] fileData, String description) {
        if (mimeType == null || fileData == null) {
            throw new IllegalArgumentException("Mime type and file data must not be null.");
        }

        if (!this.noteFileTypeIsValidMime(mimeType)) {
            throw new IllegalArgumentException("The mime type is not valid.");
        }

        // Generate a unique filename (consider a better approach)
        String fileName = "file_" + System.currentTimeMillis(); // Simplified
        Path filePath = Paths.get("path/to/save", fileName); // Adjust the path accordingly

        // Write the file to disk
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(fileData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file.", e);
        }

        // Generate the file URL
        String fileUrl = "http://yourserver.com/files/" + fileName; // Adjust accordingly

        // Create a new File instance
        NoteFile newNoteFile = new NoteFile(mimeType, fileUrl, description);
        return newNoteFile;
    }
}
