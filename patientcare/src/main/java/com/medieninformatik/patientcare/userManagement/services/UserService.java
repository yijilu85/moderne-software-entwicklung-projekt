package com.medieninformatik.patientcare.userManagement.services;

import com.medieninformatik.patientcare.userManagement.domain.model.Patient;
import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.PatientRepo;
import com.medieninformatik.patientcare.userManagement.infrastructure.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> getUser(Long userId) {
        return userRepo.findById(userId);
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }
}
