package com.medieninformatik.patientcare.patientDataManagement.services;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;


import com.medieninformatik.patientcare.patientDataManagement.domain.model.Diagnosis;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Treatment;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.PatientRepo;
import com.medieninformatik.patientcare.shared.services.HelperService;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import java.util.Set;
import java.util.regex.Pattern;

@Service
public class NoteService {

    private PatientRepo patientRepo;
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

    public NoteService(PatientRepo patientRepo, HelperService helperService) {
        this.patientRepo = patientRepo;
        this.helperService = helperService;
    }

    public Optional<Patient> getPatient(Long personId) {
        return patientRepo.findById(personId);
    }

    public Diagnosis createDiagnosis(Patient patient, Doctor doctor, Date date, User creator, String icdCode, String recommendation) {
        return new Diagnosis(patient, doctor, creator, date, icdCode, recommendation);
    }

    public Treatment createTreatment(Patient patient, Doctor doctor, Date date, User creator, Diagnosis diagnosis, String action) {
        if (patient == null || doctor == null || date == null || creator == null || diagnosis == null || action == null) {
            throw new IllegalArgumentException("Keiner der Eingabewerte darf null sein.");
        }

        return new Treatment(patient, doctor, creator, date, diagnosis, action);
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

    public void addNoteToAppointment(Appointment appointment, Diagnosis diagnosis) {
        if (appointment == null || diagnosis == null) {
            throw new IllegalArgumentException("Appointment und Diagnosis dürfen nicht null sein.");
        }

        if (!this.noteUsersEqualsAppointmentUsers(appointment, diagnosis)) {
            throw new IllegalArgumentException("Die Benutzer des Termins stimmen nicht mit denen der Notiz überein.");
        }

        appointment.addNote(diagnosis);
    }

    public boolean noteFileTypeIsValidMime(String mimeType) {
        return this.VALID_MIME_TYPES.contains(mimeType);
    }

    public boolean noteUsersEqualsAppointmentUsers(Appointment appointment, Diagnosis diagnosis) {
        // Validierung: Falls appointment oder diagnosis null ist, gib false zurück
        if (appointment == null || diagnosis == null) {
            return false;
        }

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
}
