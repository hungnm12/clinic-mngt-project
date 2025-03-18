package com.example.staffmngt.dto;

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
}
