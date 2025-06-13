package com.example.accountservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "roles")
    private String roles;
    @Column(name = "type")
    private String type;
    @Column(name = "status")
    private String status;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "staffCode")
    private String staffCode;
}
