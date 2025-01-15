package com.medieninformatik.patientcare.shared.services;


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

    static {
        try {
            // Set up FileHandler to write logs to a file
            FileHandler fileHandler = new FileHandler("application.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger FileHandler: " + e.getMessage());
        }
    }


    //Pointcut für die Methode in AppointmentService
    @Pointcut("execution(* com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService.parseJSONCreateAppointmentSlot(..))")
    public void createAppointmentPointcut() {}


    // Logging vor der Methode
    @Before("createAppointmentPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Before executing method: " + joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            StringBuilder argsLog = new StringBuilder("Arguments: ");
            for (Object arg : args) {
                argsLog.append("- ").append(arg).append("\n");
            }
            logger.info(argsLog.toString());
        }
    }

    // Logging nach der Methode
    @After("createAppointmentPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("After executing method: " + joinPoint.getSignature().getName());
    }
    /*

    // Pointcut für die Methode in AppointmentService
    @Pointcut("execution(* com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService.parseJSONCreateAppointmentSlot(..))")
    public void createAppointmentPointcut() {}


    // Logging vor der Methode
    @Before("createAppointmentPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before executing method: " + joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            System.out.println("Arguments: ");
            for (Object arg : args) {
                System.out.println("- " + arg);
            }
        }
    }


    // Logging nach der Methode
    @After("createAppointmentPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("After executing method: " + joinPoint.getSignature().getName());
    }

    */
}
