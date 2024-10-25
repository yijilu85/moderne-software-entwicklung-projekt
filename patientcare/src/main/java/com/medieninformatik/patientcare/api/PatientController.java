package com.medieninformatik.patientcare.api;

import com.medieninformatik.patientcare.entities.Patient;
import com.medieninformatik.patientcare.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping(path = "/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @CrossOrigin
    @GetMapping(path = "/{id}")
    public Optional<Patient> findPatient(@PathVariable("id") Long id) {
        return patientService.getPatient(id);
    }
}
