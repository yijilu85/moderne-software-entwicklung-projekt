package com.medieninformatik.patientcare.appointmentManagement.infrastructure.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@RestController
@RequestMapping(path = "/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @CrossOrigin
    @GetMapping
    public List<Appointment> findAllAppointments(@RequestParam(required = false) Long userId) {
        if (userId == null) {
            return appointmentService.getAllAppointments();
        } else {
            return appointmentService.getAllAppointmentsForUser(userId);
        }
    }

    @CrossOrigin
    @GetMapping(path = "/{id}")
    public Optional<Appointment> findAppointment(@PathVariable("id") Long id) {
        return appointmentService.getAppointment(id);
    }

    @CrossOrigin
    @PostMapping
    public Appointment createAppointmentSlot(@RequestBody String json) throws JsonProcessingException {
        return appointmentService.parseJSONCreateAppointmentSlot(json);
    }

    @CrossOrigin
    @PostMapping(path = "/book")
    public Appointment bookAppointment(@RequestBody String json) throws JsonProcessingException {
        return appointmentService.parseJSONBookAppointmentSlot(json);
    }

   /* @Configuration
    public class WebConfig {

        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**") // Erlaubt alle Endpunkte
                            .allowedOrigins("http://localhost:5179") // Erlaubt das Frontend
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Erlaubt spezifische Methoden
                            .allowedHeaders("*"); // Erlaubt alle Header
                }
            };
        }
    }*/
}
