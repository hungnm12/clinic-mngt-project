package com.example.patientservice.service.impl;

import com.example.patientservice.dto.req.PatientReqDto;
import com.example.patientservice.dto.res.GeneralResponse;
import com.example.patientservice.entity.EmergencyContactEntity;
import com.example.patientservice.entity.PatientEntity;
import com.example.patientservice.repository.PatientRepository;
import com.example.patientservice.service.PatientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.util.Random;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public GeneralResponse addPatient(PatientReqDto patient) {
        if (patient == null) {
            return new GeneralResponse(HttpsURLConnection.HTTP_BAD_REQUEST, "", "No input for patient detail", null);
        }
        String patientCode = "Patient_" + new Random().nextInt(100);
        if (patientRepository.findByPatientCode(patientCode) != null) {
            return new GeneralResponse(HttpsURLConnection.HTTP_CONFLICT, "", "Patient already exists", null);
        }
        EmergencyContactEntity emergencyContact = EmergencyContactEntity
                .builder()
                .relationship(patient.getEmergencyContact().getRelationship())
                .phone(patient.getEmergencyContact().getPhone())
                .build();



        PatientEntity patientEntity = PatientEntity
                .builder()
                .patientCode(patientCode)
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .address(patient.getAddress())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .dateOfBirth(patient.getDateOfBirth())
                .emergencyContact(emergencyContact)
                .build();
        patientRepository.save(patientEntity);


        return null;
    }
}
