package com.medieninformatik.patientcare.services;

import com.medieninformatik.patientcare.entities.Appointment;
import com.medieninformatik.patientcare.entities.Note;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

import java.time.LocalDateTime;

@Service
public class AppointmentService {
    private final BeanNameUrlHandlerMapping beanNameHandlerMapping;

    public AppointmentService(BeanNameUrlHandlerMapping beanNameHandlerMapping) {
        this.beanNameHandlerMapping = beanNameHandlerMapping;
    }
    //Methoden

    public void scheduleAppointment(){

    }

    public void editAppointment(){

    }

    public void addNote(Appointment appointment, Note note){
        appointment.addNote(note);
    }

    public void removeSingleNote(Appointment appointment,Note note){
        appointment.removeSingleNote(note);
    }

    public void removeAllNotes(Appointment appointment){
        appointment.removeAllNotes();
    }

    public void sendAppoinmentReminder(){

    }




    public boolean isStartAppointmentBeforeToEndAppointment(LocalDateTime startDateTime, LocalDateTime endDateTime){
        return endDateTime.isAfter(startDateTime);
    }

    public boolean isEndAppointmentBeforeStartAppointment(LocalDateTime startDateTime, LocalDateTime endDateTime){
        return !endDateTime.isAfter(startDateTime);
    }

    public boolean isDateInFuture(LocalDateTime startDateTime) {
        return startDateTime.isAfter(LocalDateTime.now());
    }

}
