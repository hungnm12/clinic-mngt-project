package com.example.staffmngt.dto.req;

import com.example.staffmngt.entity.DepartmentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddServiceReq extends SearchReqDto {


    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("service_code")
    private String serviceCode;
    @JsonProperty("note")
    private String note;
    @JsonProperty("price")
    private double price;
    @JsonProperty("department")
    private String department;
}
