package com.example.patientservice.repository;

import com.example.patientservice.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    PatientEntity findByPatientCode(String patientCode);
}
