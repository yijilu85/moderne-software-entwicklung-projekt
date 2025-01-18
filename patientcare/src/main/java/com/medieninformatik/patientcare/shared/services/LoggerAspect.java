package com.medieninformatik.patientcare.shared.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Component
@Aspect
@EnableAspectJAutoProxy


public class LoggerAspect {

    private static final Logger logger = Logger.getLogger(LoggerAspect.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    static {
        try {
            FileHandler fileHandler = new FileHandler("applicationLog.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger FileHandler: " + e.getMessage());
        }
    }


    //Pointcut für die Methode in AppointmentService
    @Pointcut("execution(* com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService.parseJSONCreateAppointmentSlot(..))")
    public void createAppointmentPointcut() {}

    @Pointcut("execution(* com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService.parseJSONBookAppointmentSlot(..))")
    public void bookAppointmentPointcut() {}

    @Pointcut("execution(* com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService.deleteAppointment(..))")
    public void deleteAppointmentPointcut() {}

    @Pointcut("execution(* com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService.cancelAppointment(..))")
    public void cancelAppointmentPointcut() {}


    // Logging vor den Methoden
    @Before("createAppointmentPointcut()")
    public void logBeforeCreate(JoinPoint joinPoint) {
        logBefore(joinPoint, "Create Appointment");
    }

    @Before("bookAppointmentPointcut()")
    public void logBeforeBook(JoinPoint joinPoint) {
        logBefore(joinPoint, "Book Appointment");
    }

    @Before("deleteAppointmentPointcut()")
    public void logBeforeDelete(JoinPoint joinPoint) {
        logBefore(joinPoint, "Delete Appointment");
    }

    @Before("cancelAppointmentPointcut()")
    public void logBeforeCancel(JoinPoint joinPoint) {
        logBefore(joinPoint, "Cancel Appointment");
    }


    // Logging nach den Methoden
    @After("createAppointmentPointcut()")
    public void logAfterCreate(JoinPoint joinPoint) {
        logAfter(joinPoint, "Create Appointment");
    }

    @After("bookAppointmentPointcut()")
    public void logAfterBook(JoinPoint joinPoint) {
        logAfter(joinPoint, "Book Appointment");
    }

    @After("deleteAppointmentPointcut()")
    public void logAfterDelete(JoinPoint joinPoint) {
        logAfter(joinPoint, "Delete Appointment");
    }

    @After("cancelAppointmentPointcut()")
    public void logAfterCancel(JoinPoint joinPoint) {
        logAfter(joinPoint, "Cancel Appointment");
    }



    // Gemeinsame Logging-Methoden
    private void logBefore(JoinPoint joinPoint, String operation) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Before ").append(operation)
                .append(" - Method: ").append(methodName)
                .append(" - User: ");

        try {
            // Extrahiere User-ID basierend auf der Operation
            if (args.length > 0) {
                String userId = extractUserId(args[0], operation);
                logMessage.append(userId);
            }
        } catch (Exception e) {
            logMessage.append("Unknown User");
            logger.warning("Failed to extract user information: " + e.getMessage());
        }

        logger.info(logMessage.toString());
    }

    private String extractUserId(Object arg, String operation) throws JsonProcessingException {
        if (arg instanceof String && operation.contains("Create")) {
            JsonNode rootNode = objectMapper.readTree((String) arg);
            JsonNode creatorNode = rootNode.get("creator");
            return creatorNode != null ? "Creator ID: " + creatorNode.get("id").asLong() : "Unknown Creator";
        }
        else if (arg instanceof String && operation.contains("Book")) {
            JsonNode rootNode = objectMapper.readTree((String) arg);
            JsonNode patientId = rootNode.get("patientId");
            return patientId != null ? "Patient ID: " + patientId.asLong() : "Unknown Patient";
        }
        else if (arg instanceof String && operation.contains("Cancel")) {
            JsonNode rootNode = objectMapper.readTree((String) arg);
            JsonNode userId = rootNode.get("userId");
            return userId != null ? "User ID: " + userId.asLong() : "Unknown User";
        }
        else if (arg instanceof Long && operation.contains("Delete")) {
            return "User ID: " + arg;
        }
        return "Unknown User";
    }

    private void logAfter(JoinPoint joinPoint, String operation) {
        logger.info("After " + operation + " - Method: " + joinPoint.getSignature().getName() + " completed");
    }

}
