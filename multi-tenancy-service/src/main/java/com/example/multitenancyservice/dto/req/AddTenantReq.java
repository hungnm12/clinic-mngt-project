package com.example.multitenancyservice.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddTenantReq {
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("schemaName")
    private String schemaName;
    @JsonProperty("dburl")
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
