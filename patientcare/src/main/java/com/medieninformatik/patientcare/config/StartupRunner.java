package com.medieninformatik.patientcare.config;

import com.medieninformatik.patientcare.entities.Patient;
import com.medieninformatik.patientcare.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class StartupRunner implements ApplicationRunner {

    @Autowired
    private PatientRepo patientRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("running.........");

        Patient patient = new Patient("Hans", "Klaus");
        patientRepo.save(patient);
        System.out.println("patient saved: " + patient);
    }


}
