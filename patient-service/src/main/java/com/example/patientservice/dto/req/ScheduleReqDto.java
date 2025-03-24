package com.example.patientservice.dto.req;

import com.example.patientservice.entity.DoctorEntity;
import com.example.patientservice.entity.PatientEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleReqDto {

    @JsonProperty("appointment_date")
    private Date appointmentDate;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("status")
    private String status;

    @JsonProperty("patient")
    private PatientEntity patient;

    @JsonProperty("doctor")
    private DoctorEntity doctor;
}
