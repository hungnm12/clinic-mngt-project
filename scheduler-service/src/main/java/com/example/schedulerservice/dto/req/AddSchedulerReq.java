package com.example.schedulerservice.dto.req;


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
    private Date apmtDate;

    @JsonProperty("apmt_time")
    private Date apmtTime;

    @JsonProperty("note")
    private String note;
}
