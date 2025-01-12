package com.medieninformatik.patientcare.shared.services;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Pointcut;


@Component
@Aspect
@EnableAspectJAutoProxy


public class Logger {


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












}
