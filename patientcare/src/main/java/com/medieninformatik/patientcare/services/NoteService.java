package com.medieninformatik.patientcare.services;
import com.medieninformatik.patientcare.services.HelperService;

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

    private PatientRepo patientRepo;
    private HelperService helperService;


    @Autowired
    public NoteService(PatientRepo patientRepo, HelperService helperService) {
        this.patientRepo = patientRepo;
        this.helperService = helperService;
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




    public void addNoteToAppointment(Appointment appointment, Diagnosis diagnosis) {
        // Überprüfung, ob die Eingabewerte null sind
        if (appointment == null || diagnosis == null) {
            throw new IllegalArgumentException("Appointment und Diagnosis dürfen nicht null sein.");
        }

        // Überprüfe, ob die Benutzer des Termins mit denen der Notiz übereinstimmen
        if (!helperService.noteUsersEqualsAppointmentUsers(appointment, diagnosis)) {
            throw new IllegalArgumentException("Die Benutzer des Termins stimmen nicht mit denen der Notiz überein.");
        }


        appointment.addNote(diagnosis);
    }

    private static final Set<String> VALID_MIME_TYPES = Set.of(
            "text/plain",
            "application/pdf",
            "image/jpg",
            "image/jpeg",
            "image/png",
            "video/mp4",
            "audio/mpeg"
    );

    public boolean noteFileTypeIsValidMime(String mimeType) {
        // Überprüfen, ob der MIME-Typ in der Liste der erlaubten Typen enthalten ist
        return VALID_MIME_TYPES.contains(mimeType);
    }
//    public Note createNote(Patient patient, Doctor doctor, Date date, User creator) {
//    }
}
