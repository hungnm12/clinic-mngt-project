package com.example.staffmngt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leave_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Column(name = "end_date", nullable = false)
    private String endDate;

    @Column(name = "leave_type", nullable = false)
    private String leaveType; // Sick leave, vacation, etc.

    @Column(name = "status")
    private String status; // Pending, Approved, Rejected
}
