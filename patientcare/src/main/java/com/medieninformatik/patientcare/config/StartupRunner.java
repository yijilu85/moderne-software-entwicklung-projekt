package com.medieninformatik.patientcare.config;

import com.medieninformatik.patientcare.appointmentManagement.domain.model.Appointment;
import com.medieninformatik.patientcare.userManagement.domain.model.Doctor;
import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.appointmentManagement.infrastructure.repositories.AppointmentRepo;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.DoctorRepo;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.PatientRepo;
import com.medieninformatik.patientcare.appointmentManagement.services.AppointmentService;
import com.medieninformatik.patientcare.userManagement.services.FakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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

    @Autowired
    private FakerService fakerService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("running.........");

        /*Patient patient = new Patient("Hans", "Klaus");
        patientRepo.save(patient);
        System.out.println("patient saved: " + patient);
        System.out.println(patient.getLastName());*/

//        Doctor doctor = new Doctor("Hallo", "Dr. House");
//        doctorRepo.save(doctor);

        FakerService fakerService = new FakerService(doctorRepo, patientRepo);
//        fakerService.createDoctors(20);
//        fakerService.createPatients(20);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, Month.NOVEMBER, 25, 12, 15);
//
//        Appointment appointment = appointmentService.createAppointmentSlot(doctor, doctor, startDateTime, endDateTime,
//                now);
//        appointmentService.scheduleAppointment(appointment, patient, Appointment.Type.OFFLINE);
    }

   /*public void run(ApplicationArguments args) throws Exception {
       System.out.println("StartupRunner is running...");

       // Testpatient erstellen
       Patient patient = new Patient("Hans", "Klaus");
       patientRepo.save(patient);
       System.out.println("Testpatient gespeichert: " + patient);

       // FakerService verwenden, um Ärzte zu generieren
       fakerService.createDoctors(20);

       // Einen Testarzt aus der Datenbank abrufen
       Doctor doctor = doctorRepo.findAll().stream().findFirst().orElse(null);
       if (doctor == null) {
           System.err.println("Keine Ärzte in der Datenbank gefunden!");
           return;
       }
   }*/
}
