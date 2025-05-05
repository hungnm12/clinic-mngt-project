package com.example.staffmngt.dto.req;

import com.example.staffmngt.entity.StaffEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddShiftReq {


    @JsonProperty("shift_code")
    private String shiftCode;

    @JsonProperty("staff_code")
    private String staffCode;

    @JsonProperty("shift_start")
    private String shiftStart; // Example: "08:00 AM"

    @JsonProperty("shift_end")
    private String shiftEnd; // Example: "04:00 PM"

    @JsonProperty("day_of_week")
    private String dayOfWeek; // Monday, Tuesday, etc.

    @JsonProperty("booked_time")
    private Date bookedTime;
}
