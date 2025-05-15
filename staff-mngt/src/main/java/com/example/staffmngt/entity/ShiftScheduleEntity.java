package com.example.staffmngt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "shift_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private Long id;

    @Column(name = "shift_code")
    private String shiftCode;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    @Column(name = "booked_patient")
    private String bookedPatient;


    @Column(name = "booked_time")
    private ZonedDateTime bookedTime;

}
