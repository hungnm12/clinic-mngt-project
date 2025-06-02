package com.example.staffmngt.dto.req;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendReportMailReq {

    @JsonProperty("patientName")
    private String patientName;

    @JsonProperty("patientPhone")
    private String patientPhone;

    @JsonProperty("patientDob")
    private String patientDob;

    @JsonProperty("patientEmail")
    private String patientEmail;

    @JsonProperty("serviceType")
    private String serviceType;

    @JsonProperty("diagnose")
    private String diagnose;

    @JsonProperty("assumption")
    private String assumption;
    @JsonProperty("symptom")
    private String symptom;
    @JsonProperty("assign")
    private String assign;
    @JsonProperty("note")
    private String note;

    @JsonProperty("department")
    private String department;


    @JsonProperty("staffName")
    private String staffName;
    
    @JsonProperty("subject")
    String subject;
}
