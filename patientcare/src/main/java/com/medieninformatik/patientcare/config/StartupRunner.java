package com.medieninformatik.patientcare.config;

import com.medieninformatik.patientcare.entities.Appointment;
import com.medieninformatik.patientcare.entities.Doctor;
import com.medieninformatik.patientcare.entities.Patient;
import com.medieninformatik.patientcare.entities.User;
import com.medieninformatik.patientcare.repo.AppointmentRepo;
import com.medieninformatik.patientcare.repo.DoctorRepo;
import com.medieninformatik.patientcare.repo.PatientRepo;
import com.medieninformatik.patientcare.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;


@Component
public class StartupRunner implements ApplicationRunner {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private AppointmentService appointmentService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("running.........");

        Patient patient = new Patient("Hans", "Klaus");
        patientRepo.save(patient);
        System.out.println("patient saved: " + patient);
        System.out.println(patient.getLastName());

        Doctor doctor = new Doctor("Hallo", "Dr. House");
        doctorRepo.save(doctor);


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 15);

        Appointment appointment = appointmentService.createAppointmentSlot(doctor, doctor, startDateTime, endDateTime,
                now);
        appointmentService.scheduleAppointment(appointment, patient, Appointment.Type.OFFLINE);
    }


}
