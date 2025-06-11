package com.example.staffmngt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "staff_history")
public class StaffHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "role")
    private String role;
    @Column(name ="phone")
    private String phone;
    @Column(name ="specialty")
    private String specialty;
    @Column(name = "email")
    private String email;

    @Column(name = "staffCode")
    private String staffCode;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;


    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

}