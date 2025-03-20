package com.example.staffmngt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    @Column(name = "base_salary", nullable = false)
    private Double baseSalary;

    @Column(name = "bonus")
    private Double bonus;

    @Column(name = "deductions")
    private Double deductions;

    @Column(name = "net_salary")
    private Double netSalary;
}
