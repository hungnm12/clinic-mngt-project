package com.example.schedulerservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "scheduler")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchedulerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scheduler_code")
    private String schedulerCode;

    // Patient
    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "patient_telephone")
    private String patientTelephone;

    @Column(name = "patient_email")
    private String patientEmail;

    //Doctor

    @Column(name = "dr_name")
    private String drName;

    @Column(name = "ordered_srv")
    private String orderedSrv;

    // Date
    @Column(name = "date_apmt")
    private Date dateApmt;

    @Column(name = "note")
    private String note;

}
