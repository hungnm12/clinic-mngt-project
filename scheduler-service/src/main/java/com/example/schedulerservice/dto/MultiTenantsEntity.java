package com.example.schedulerservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MultiTenantsEntity {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("schemaName")
    private String schemaName;
    @JsonProperty("dbUrl")
    private String dbUrl;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("clinicName")
    private String clinicName;
    @JsonProperty("address")
    private String address;
    @JsonProperty("representativeName")
    private String representativeName;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("clinicId")
    private String clinicCode;
    @JsonProperty("email")
    private String email;
    @JsonProperty("website")
    private String website;
}