package com.medieninformatik.patientcare.shared.services;

import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.patientDataManagement.domain.model.Diagnosis;
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
            return date.format(formatter).equals(dateStr); //gültiges Datum im korrekten Format

        } catch (DateTimeParseException e) {
            return false; // Ungültiges Datum
        }
    }
}
