package com.example.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class MultiTenantsEntity {
    @JsonProperty("id")
    private Long id;
    @Column(name = "tenantId")
    private String tenantId;
    @Column(name = "schema_name")
    private String schemaName;
    @Column(name = "db_url")
    private String dbUrl;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "clinic-name")
    private String clinicName;
    @Column(name = "address")
    private String address;
    @Column(name = "representativeName")
    private String representativeName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "clinic-code")
    private String clinicCode;
    @Column(name = "email")
    private String email;
    @Column(name = "website")
    private String website;
}
