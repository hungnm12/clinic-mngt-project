package com.example.patientservice.service;

import com.example.patientservice.dto.req.PatientReqDto;
import com.example.patientservice.dto.res.GeneralResponse;
import com.example.patientservice.entity.PatientEntity;

public interface PatientService {

    GeneralResponse addPatient(PatientReqDto patient);


}
