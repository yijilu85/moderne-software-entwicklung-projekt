package com.medieninformatik.patientcare.userManagement.infrastructure.repositories;

import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}