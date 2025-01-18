package com.medieninformatik.patientcare.appointmentManagement.services;

import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;

@FunctionalInterface
public interface AppointmentValidator {
    boolean isValid(Appointment appointment);

    default AppointmentValidator and(AppointmentValidator other) {
        return appointment -> this.isValid(appointment) && other.isValid(appointment);
    }
}
