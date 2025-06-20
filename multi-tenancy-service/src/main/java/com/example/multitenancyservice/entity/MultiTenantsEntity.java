package com.example.multitenancyservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
//
//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    private byte[] image;
}
