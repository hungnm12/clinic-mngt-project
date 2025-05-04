package com.example.staffmngt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id;


    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_code")
    private String serviceCode;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private DepartmentEntity department;
}
