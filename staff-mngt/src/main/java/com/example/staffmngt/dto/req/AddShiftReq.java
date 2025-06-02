package com.example.staffmngt.dto.req;

import com.example.staffmngt.dto.res.BookedPatientDto;
import com.example.staffmngt.entity.StaffEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddShiftReq {


    @JsonProperty("drName")
    String drName;

    @JsonProperty("shift_code")
    private String shiftCode;

    @JsonProperty("staff_code")
    private String staffCode;

    @JsonProperty("booked_patient")
    private String bookedPatient;

    @JsonProperty("booked_time")
    private ZonedDateTime bookedTime;
    @JsonProperty("note")
    private String note;
    @JsonProperty("status")
    private String status;
    @JsonProperty("schedulerCode")
    private String schedulerCode;
}
