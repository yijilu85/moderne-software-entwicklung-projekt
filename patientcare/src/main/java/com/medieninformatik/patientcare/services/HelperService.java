package com.medieninformatik.patientcare.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
}
