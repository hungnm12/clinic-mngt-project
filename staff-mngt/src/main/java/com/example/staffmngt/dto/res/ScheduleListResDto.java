package com.example.staffmngt.dto.res;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class ScheduleListResDto {
    @JsonProperty("shiftCode")
    private String shiftCode;
    @JsonProperty("staffCode")
    private String staffCode;
    @JsonProperty("bookedTime")
    private String bookedTime;
    @JsonProperty("bookedPatient")
    private String bookedPatient;
    @JsonProperty("note")
    private String note;
    @JsonProperty("status")
    private String status;
    @JsonProperty("schedulerCode")
    private String schedulerCode;
    // Constructor matching the query parameters
    public ScheduleListResDto(String shiftCode, String staffCode,
                              ZonedDateTime bookedTime, String bookedPatient, String note, String status,String schedulerCode) {
        this.shiftCode = shiftCode;
        this.staffCode = staffCode;
        this.bookedTime = bookedTime != null ? bookedTime.toString() : null;
        this.bookedPatient = bookedPatient;
        this.note = note;
        this.status = status;
        this.schedulerCode = schedulerCode;

    }

    // Keep the all-args constructor if needed
    public ScheduleListResDto(String shiftCode, String staffCode,
                              String bookedTime, String bookedPatient) {
        this.shiftCode = shiftCode;
        this.staffCode = staffCode;
        this.bookedTime = bookedTime;
        this.bookedPatient = bookedPatient;
    }
}
