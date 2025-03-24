package com.example.patientservice.dto.req;

import com.example.patientservice.entity.EmergencyContactEntity;
import com.example.patientservice.entity.MedicalRecordEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.Date;

@Data
public class PatientReqDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("emergencyContact")
    private EmergencyContactEntity emergencyContact;

    @JsonProperty("medicalRecord")
    private MedicalRecordEntity medicalRecord;
}
