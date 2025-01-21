package com.medieninformatik.patientcare.userManagement.services;

import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.infrastructure.Exceptions.InvalidCredentialsException;
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

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User loginUser(String email, String password) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                throw new InvalidCredentialsException("Invalid credentials");
            }
        } else {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
}
