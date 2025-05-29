package com.example.schedulerservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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

    @Column(name = "schedulerCode")
    private String schedulerCode;

    // Patient
    @Column(name = "patientName")
    private String patientName;

    @Column(name = "patientTelephone")
    private String patientTelephone;

    @Column(name = "patientEmail")
    private String patientEmail;

    //Doctor

    @Column(name = "drName")
    private String drName;

    @Column(name = "orderedSrv")
    private String orderedSrv;

    // Date
    @Column(name = "dateApmt")
    private ZonedDateTime dateApmt;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private String status;

}
