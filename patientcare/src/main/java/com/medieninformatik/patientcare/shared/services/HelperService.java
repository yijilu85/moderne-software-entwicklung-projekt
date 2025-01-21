package com.medieninformatik.patientcare.shared.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class HelperService {

    // Methode zum Validieren des Datums
    public boolean isValidDate(String dateStr, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return date.format(formatter).equals(dateStr); //gültiges Datum im korrekten Format

        } catch (DateTimeParseException e) {
            return false; // Ungültiges Datum
        }
    }

    public LocalDateTime parseDateFromJSON(String dateStr) {
        String strippedInput = dateStr.replace("Z", ""); // Remove the Z
        return LocalDateTime.parse(strippedInput);
    }
}
