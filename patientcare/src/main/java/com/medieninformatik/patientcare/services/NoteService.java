package com.medieninformatik.patientcare.services;

import com.medieninformatik.patientcare.entities.Doctor;
import com.medieninformatik.patientcare.entities.Note;
import com.medieninformatik.patientcare.entities.Patient;
import com.medieninformatik.patientcare.entities.User;
import com.medieninformatik.patientcare.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class NoteService {

    private final PatientRepo patientRepo;

    @Autowired
    public NoteService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public Optional<Patient> getPatient(Long personId) {
        return patientRepo.findById(personId);
    }

    public int calcTest(int a, int b){
        int sum = a +b;
        return sum;
    }

//    public Note createNote(Patient patient, Doctor doctor, Date date, User creator) {
//    }
}
