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
    @JsonProperty("schema_name")
    private String schemaName;
    @JsonProperty("db_url")
    private String dbUrl;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("clinicNname")
    private String clinicName;
    @JsonProperty("address")
    private String address;
    @JsonProperty("inchargedDr")
    private String inchargedDr;
    @JsonProperty("drPhone")
    private String drPhone;
    @JsonProperty("clinicCode")
    private String clinicCode;
    @JsonProperty("clinicEmail")
    private String clinicEmail;
    @JsonProperty("website")
    private String website;
}
