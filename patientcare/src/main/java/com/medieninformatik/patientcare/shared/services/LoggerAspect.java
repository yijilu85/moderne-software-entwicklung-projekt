package com.medieninformatik.patientcare.shared.services;

import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.UserRepo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Component
@Aspect
@EnableAspectJAutoProxy
public class LoggerAspect {

    private static final Logger logger = Logger.getLogger(LoggerAspect.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepo userRepo;

    static {
        try {
            FileHandler fileHandler = new FileHandler("applicationLog.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger FileHandler: " + e.getMessage());
        }
    }

    // Pointcuts
    @Pointcut("execution(* com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService.parseJSONCreateAppointmentSlot(..))")
    public void createAppointmentPointcut() {}

    @Pointcut("execution(* com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService.parseJSONBookAppointmentSlot(..))")
    public void bookAppointmentPointcut() {}

    @Pointcut("execution(* com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService.deleteAppointment(..))")
    public void deleteAppointmentPointcut() {}

    @Pointcut("execution(* com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService.cancelAppointment(..))")
    public void cancelAppointmentPointcut() {}

    // Before Logging
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

    // After Logging
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

    private void logSeparator() {
        logger.info("\n----------------------------------------\n");
    }


    // Core Logging Logic
    private void logBefore(JoinPoint joinPoint, String operation) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Before ").append(operation)
                .append(" - Method: ").append(methodName)
                .append(" - User: ");

        try {
            if (args.length > 0) {
                String userInfo = extractUserInfo(args[0], operation);
                logMessage.append(userInfo);
            } else {
                logMessage.append("Unknown User");
            }
        } catch (Exception e) {
            logMessage.append("Unknown User");
            logger.warning("Failed to extract user information: " + e.getMessage());
        }

        logger.info(logMessage.toString());
        logSeparator();
    }

    private String extractUserInfo(Object arg, String operation) {
        if (arg instanceof String) {
            try {
                JsonNode rootNode = objectMapper.readTree((String) arg);
                Long userId = null;

                if (operation.contains("Create")) {
                    userId = extractIdFromJson(rootNode, "creator", "id");
                } else if (operation.contains("Book")) {
                    userId = extractIdFromJson(rootNode, null, "patientId");
                } else if (operation.contains("Cancel")) {
                    userId = extractIdFromJson(rootNode, null, "userId");
                }

                if (userId != null) {
                    final Long finalUserId = userId; // Explizit final deklarieren
                    Optional<User> user = userRepo.findById(finalUserId);
                    return user.map(u -> u.getUserType() + " ID: " + finalUserId)
                            .orElse("Unknown User ID: " + finalUserId);
                }


            } catch (Exception e) {
                logger.warning("Error parsing JSON for user info: " + e.getMessage());
            }
        } else if (arg instanceof Long && operation.contains("Delete")) {
            Long userId = (Long) arg;
            Optional<User> user = userRepo.findById(userId);
            return user.map(u -> u.getUserType() + " ID: " + userId).orElse("Unknown User ID: " + userId);
        }

        return "Unknown User";
    }

    private Long extractIdFromJson(JsonNode rootNode, String parentNodeName, String fieldName) {
        if (parentNodeName != null) {
            JsonNode parentNode = rootNode.get(parentNodeName);
            if (parentNode != null) {
                return parentNode.get(fieldName).asLong();
            }
        } else {
            JsonNode fieldNode = rootNode.get(fieldName);
            if (fieldNode != null) {
                return fieldNode.asLong();
            }
        }
        return null;
    }

    private void logAfter(JoinPoint joinPoint, String operation) {
        logger.info("After " + operation + " - Method: " + joinPoint.getSignature().getName() + " completed");
        logSeparator();
    }
}
