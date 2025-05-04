package com.example.schedulerservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "patients")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchedulerEntity {
    @Id
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
    @Column(name = "apmt_date")
    private Date apmtDate;

    @Column(name = "apmt_time")
    private Date apmtTime;

    @Column(name = "note")
    private String note;

}
