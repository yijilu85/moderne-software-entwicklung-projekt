package com.medieninformatik.patientcare.userManagement.infrastructure.repositories;

import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
}