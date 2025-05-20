package com.example.staffmngt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "record")
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //patient
    @Column(name = "patientName")
    private String patientName;

    @Column(name = "patientPhone")
    private String patientPhone;

    @Column(name = "patientDob")
    private String patientDob;

    @Column(name = "patientEmail")
    private String patientEmail;

    @Column(name = "serviceType")
    private String serviceType;

    @Column(name = "diagnose")
    private String diagnose;

    @Column(name = "assumption")
    private String assumption;
    @Column(name = "symptom")
    private String symptom;
    @Column(name = "assign")
    private String assign;
    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Column(name = "staffCode")
    private String staffCode;

    @Column(name = "staffName")
    private String staffName;

}
