package com.example.staffmngt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "staff")
public class StaffEntity {

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

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private DepartmentEntity department;

    @Column(name = "staff_code")
    private String staffCode;

    @Column(name = "password")
    private String password;


    @Column(name = "status")
    private String status;

}
