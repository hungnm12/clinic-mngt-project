package com.example.staffmngt.dto.req;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSchedulerReq {

    // Patient
    @JsonProperty("patientName")
    private String patientName;

    @JsonProperty("patientTelephone")
    private String patientTelephone;

    @JsonProperty("patientEmail")
    private String patientEmail;

    //Doctor

    @JsonProperty("drName")
    private String drName;

    @JsonProperty("orderedSrv")
    private String orderedSrv;
    @JsonProperty("schedulerCode")
    private String schedulerCode;
    // Date
    @JsonProperty("apmtDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String apmtDate;

    @JsonProperty("apmtTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private String apmtTime;

    @JsonProperty("note")
    private String note;

    @JsonProperty("status")
    private String status;

}
