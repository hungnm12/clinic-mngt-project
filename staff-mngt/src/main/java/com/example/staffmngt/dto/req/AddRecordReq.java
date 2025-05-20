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
}
