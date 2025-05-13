package com.example.schedulerservice.dto.req;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSchedulerReq {

    // Patient
    @JsonProperty("patient_name")
    private String patientName;

    @JsonProperty("patient_telephone")
    private String patientTelephone;

    @JsonProperty("patient_email")
    private String patientEmail;

    //Doctor

    @JsonProperty("dr_name")
    private String drName;

    @JsonProperty("ordered_srv")
    private String orderedSrv;

    // Date
    @JsonProperty("apmt_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String apmtDate;

    @JsonProperty("apmt_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private String apmtTime;

    @JsonProperty("note")
    private String note;
}
