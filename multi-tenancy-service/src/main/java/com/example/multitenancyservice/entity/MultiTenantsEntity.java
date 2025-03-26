package com.example.multitenancyservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tenants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultiTenantsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "inchargedDr")
    private String inchargedDr;
    @Column(name = "dr-phone")
    private String drPhone;
    @Column(name = "clinic-code")
    private String clinicCode;
    @Column(name = "clinic-email")
    private String clinicEmail;
    @Column(name = "website")
    private String website;
}
