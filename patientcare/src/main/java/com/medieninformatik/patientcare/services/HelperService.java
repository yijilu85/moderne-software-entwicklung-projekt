package com.medieninformatik.patientcare.services;

import com.medieninformatik.patientcare.entities.Appointment;
import com.medieninformatik.patientcare.entities.Diagnosis;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class HelperService {

    // Methode zum Validieren des Datums
    public boolean isValidDate(String dateStr, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return true; // Datum ist gültig
        } catch (DateTimeParseException e) {
            return false; // Ungültiges Datum
        }
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
