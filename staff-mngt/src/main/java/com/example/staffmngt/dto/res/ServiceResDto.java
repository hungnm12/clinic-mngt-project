package com.example.staffmngt.dto.res;


import com.example.staffmngt.entity.DepartmentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResDto {

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("service_code")
    private String serviceCode;

    @JsonProperty("price")
    private double price;

    @JsonProperty("department")
    private DepartmentEntity department;
}
