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
}
