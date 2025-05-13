package com.example.staffmngt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "lastName")
    private String lastName;
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "role")
    private String role;

    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "specialty")
    private String specialty;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Column(name = "staffCode")
    private String staffCode;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShiftScheduleEntity> lstShift = new ArrayList<>();

}
