package com.example.staffmngt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    @Column(name = "shift_start", nullable = false)
    private String shiftStart; // Example: "08:00 AM"

    @Column(name = "shift_end", nullable = false)
    private String shiftEnd; // Example: "04:00 PM"

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek; // Monday, Tuesday, etc.
}
