package com.medieninformatik.patientcare.services;

import com.medieninformatik.patientcare.entities.*;


import com.medieninformatik.patientcare.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import java.util.Set;
import java.util.regex.Pattern;

@Service
public class NoteService {

    private final PatientRepo patientRepo;

    @Autowired
    public NoteService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public Optional<Patient> getPatient(Long personId) {
        return patientRepo.findById(personId);
    }

    public int calcTest(int a, int b){
        int sum = a +b;
        return sum;
    }

    public Diagnosis createDiagnosis(Patient patient, Doctor doctor, Date date, User creator, String icdCode, String recommendation) {
        // Erstelle ein neues Diagnosis-Objekt mit den übergebenen Parametern
        return new Diagnosis(patient, doctor, creator, date, icdCode, recommendation);
    }

    public Treatment createTreatment(Patient patient, Doctor doctor, Date date, User creator, Diagnosis diagnosis, String action) {
        // Validierung: Überprüfe, ob die Eingaben nicht null sind
        if (patient == null || doctor == null || date == null || creator == null || diagnosis == null || action == null) {
            throw new IllegalArgumentException("Keiner der Eingabewerte darf null sein.");
        }

        // Erstelle ein neues Treatment-Objekt mit den übergebenen Parametern
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
            return false; // Falls die Eingabe null ist, soll die Methode false zurückgeben
        }
        return action.length() >= 10;
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

    public void addNoteToAppointment(Appointment appointment, Diagnosis diagnosis) {
        // Überprüfung, ob die Eingabewerte null sind
        if (appointment == null || diagnosis == null) {
            throw new IllegalArgumentException("Appointment und Diagnosis dürfen nicht null sein.");
        }

        // Überprüfe, ob die Benutzer des Termins mit denen der Notiz übereinstimmen
        if (!noteUsersEqualsAppointmentUsers(appointment, diagnosis)) {
            throw new IllegalArgumentException("Die Benutzer des Termins stimmen nicht mit denen der Notiz überein.");
        }


        appointment.addNote(diagnosis);
    }

    public boolean noteFileTypeIsValidMime(String mimeType) {
        // Erlaubte MIME-Typen
        Set<String> validMimeTypes = new HashSet<>();
        validMimeTypes.add("text/plain");
        validMimeTypes.add("application/pdf");
        validMimeTypes.add("image/jpg");
        validMimeTypes.add("image/jpeg");
        validMimeTypes.add("image/png");
        validMimeTypes.add("video/mp4");
        validMimeTypes.add("audio/mpeg");


        // Überprüfen, ob der MIME-Typ in der Liste der erlaubten Typen enthalten ist
        return validMimeTypes.contains(mimeType);
    }
//    public Note createNote(Patient patient, Doctor doctor, Date date, User creator) {
//    }
}
