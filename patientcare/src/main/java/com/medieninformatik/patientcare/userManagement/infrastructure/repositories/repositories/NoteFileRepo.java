package com.medieninformatik.patientcare.userManagement.infrastructure.repositories.repositories;

import com.medieninformatik.patientcare.patientDataManagement.domain.model.NoteFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteFileRepo extends JpaRepository<NoteFile, Long> {
}