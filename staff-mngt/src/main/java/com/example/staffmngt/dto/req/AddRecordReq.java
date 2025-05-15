package com.example.staffmngt.dto.req;

import com.example.staffmngt.entity.DepartmentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRecordReq {


    @JsonProperty("patient_name")
    private String patientName;

    @JsonProperty("patient_phone")
    private String patientPhone;

    @JsonProperty("patient_dob")
    private String patientDob;

    @JsonProperty("patient_email")
    private String patientEmail;

    @JsonProperty("service_type")
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


    @JsonProperty("incharged_dr")
    private String inchargedDr;
}
